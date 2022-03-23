package no.fintlabs;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.model.Data;
import no.fintlabs.model.Error;
import no.fintlabs.model.Event;
import no.fintlabs.model.EventType;
import no.fintlabs.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Component
public class TestDataGenerator {

    public TestDataGenerator(EventRepository eventRepository, @Value("${generateTestData}") boolean generateTestData) {
        if (!generateTestData) {
            return;
        }
        eventRepository.saveAll(List.of(
                createEvent(EventType.INFO, "info event 1", 1L, 1L, 2, 0),
                createEvent(EventType.INFO, "info event 2", 1L, 1L, 1, 0),
                createEvent(EventType.INFO, "info event 3", 1L, 1L, 3, 0),
                createEvent(EventType.ERROR, "error event", 2L, 2L, 1, 2),
                createEvent(EventType.INFO, "info event from retry", 3L, 2L, 1, 0)
        ));
    }

    private Event createEvent(EventType type, String description, long correlationId, long instanceId, int numOfData, int numOfErrors) {
        Event event = new Event();
        event.setType(type);
        event.setTimestamp(LocalDateTime.now());
        event.setOrgId("orgId");
        event.setDescription(description);
        event.setCorrelationId(correlationId);
        event.setSourceApplication("sourceApplication");
        event.setSourceApplicationIntegrationId("sourceApplicationIntegrationId");
        event.setSourceApplicationInstanceId("sourceApplicationInstanceId");
        event.setInstanceId(instanceId);
        event.setService("service");

        Map<String, Data> dataMap = IntStream.range(0, numOfData)
                .mapToObj(this::createDataEntry)
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        event.setData(dataMap);

        List<Error> errors = IntStream.range(0, numOfErrors)
                .mapToObj(this::createError)
                .collect(Collectors.toList());
        event.setErrors(errors);

        return event;
    }

    private AbstractMap.SimpleEntry<String, Data> createDataEntry(int num) {
        Data data = new Data();
        data.setContentType("Object");
        data.setContent("data" + num + "Content");
        return new AbstractMap.SimpleEntry<>("data" + num + "name", data);
    }

    private Error createError(int num) {
        Error error = new Error();
        error.setErrorCode("error" + num + "Code");
        error.setTimestamp(LocalDateTime.now().minusHours(1));
        error.setDescription("error" + num + " description");
        error.setArgs(Map.of(
                "arg1", "arg1value",
                "arg2", "arg2Value"
        ));
        return error;
    }
}
