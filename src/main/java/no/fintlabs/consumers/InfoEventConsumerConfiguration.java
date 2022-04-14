package no.fintlabs.consumers;

import no.fintlabs.flyt.kafka.event.InstanceFlowEventConsumerFactoryService;
import no.fintlabs.kafka.event.topic.EventTopicNameParameters;
import no.fintlabs.model.Event;
import no.fintlabs.model.EventType;
import no.fintlabs.repositories.EventRepository;
import no.fintlabs.skjemakafka.InstanceFlowHeadersEmbeddableMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.CommonLoggingErrorHandler;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Configuration
public class InfoEventConsumerConfiguration {

    private final EventRepository eventRepository;
    private final InstanceFlowEventConsumerFactoryService instanceFlowEventConsumerFactoryService;
    private final InstanceFlowHeadersEmbeddableMapper instanceFlowHeadersEmbeddableMapper;


    public InfoEventConsumerConfiguration(
            EventRepository eventRepository,
            InstanceFlowEventConsumerFactoryService instanceFlowEventConsumerFactoryService,
            InstanceFlowHeadersEmbeddableMapper skjemaEventHeadersMapper) {
        this.eventRepository = eventRepository;
        this.instanceFlowEventConsumerFactoryService = instanceFlowEventConsumerFactoryService;
        this.instanceFlowHeadersEmbeddableMapper = skjemaEventHeadersMapper;
    }

//    @Bean
//    public ConcurrentMessageListenerContainer<String, String> newInstanceEventListener() {
//        return createInfoEventListener("new-instance");
//    }
//
//    @Bean
//    public ConcurrentMessageListenerContainer<String, Object> newCaseEventListener() {
//        return createInfoEventListener("new-or-updated-case");
//    }
//
//    @Bean
//    public ConcurrentMessageListenerContainer<String, String> dispatchCaseEventListener() {
//        return createInfoEventListener("dispatch-case");
//    }
//
//    @Bean
//    public ConcurrentMessageListenerContainer<String, String> caseDispatchedSuccessfullyEventListener() {
//        return createInfoEventListener("case-dispatched-successfully");
//    }

    private ConcurrentMessageListenerContainer<String, Object> createInfoEventListener(String eventName) {
        return instanceFlowEventConsumerFactoryService.createFactory(
                Object.class,
                instanceFlowConsumerRecord -> {
                    Event event = new Event();
                    event.setInstanceFlowHeaders(
                            instanceFlowHeadersEmbeddableMapper.getSkjemaEventHeaders(instanceFlowConsumerRecord.getInstanceFlowHeaders())
                    );
                    event.setName(eventName);
                    event.setType(EventType.INFO);
                    event.setTimestamp(LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(instanceFlowConsumerRecord.getConsumerRecord().timestamp()),
                            ZoneId.systemDefault() // TODO: 25/03/2022 ZoneId
                    ));

                    eventRepository.save(event);
                },
                new CommonLoggingErrorHandler(),
                false
        ).createContainer(createEventTopicNameParameters(eventName));
    }

    private EventTopicNameParameters createEventTopicNameParameters(String eventName) {
        return EventTopicNameParameters.builder()
                .eventName(eventName)
                .build();
    }

}
