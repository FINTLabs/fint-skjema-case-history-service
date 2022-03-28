package no.fintlabs.consumers;

import no.fintlabs.kafka.event.EventTopicNameParameters;
import no.fintlabs.kafka.event.FintKafkaEventConsumerFactory;
import no.fintlabs.model.Event;
import no.fintlabs.model.EventType;
import no.fintlabs.repositories.EventRepository;
import no.fintlabs.skjemakafka.SkjemaEventHeadersMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.CommonLoggingErrorHandler;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Configuration
public class InfoEventConsumerConfiguration {

    @Value("${fint.org-id}")
    private String orgId;

    private final EventRepository eventRepository;
    private final FintKafkaEventConsumerFactory fintKafkaEventConsumerFactory;
    private final SkjemaEventHeadersMapper skjemaEventHeadersMapper;


    public InfoEventConsumerConfiguration(
            EventRepository eventRepository,
            FintKafkaEventConsumerFactory fintKafkaEventConsumerFactory,
            SkjemaEventHeadersMapper skjemaEventHeadersMapper) {
        this.eventRepository = eventRepository;
        this.fintKafkaEventConsumerFactory = fintKafkaEventConsumerFactory;
        this.skjemaEventHeadersMapper = skjemaEventHeadersMapper;
    }

//    @Bean
//    public ConcurrentMessageListenerContainer<String, String> newInstanceEventListener() {
//        return createInfoEventListener("new-instance");
//    }
//
//    @Bean
//    public ConcurrentMessageListenerContainer<String, String> newCaseEventListener() {
//        return createInfoEventListener("new-case");
//    }
//
//    @Bean
//    public ConcurrentMessageListenerContainer<String, String> updatedCaseEventListener() {
//        return createInfoEventListener("updated-case");
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

    private ConcurrentMessageListenerContainer<String, String> createInfoEventListener(String eventName) {
        return fintKafkaEventConsumerFactory.createConsumer(
                createEventTopicNameParameters(eventName),
                String.class,
                consumerRecord -> {
                    Event event = new Event();
                    event.setSkjemaEventHeaders(skjemaEventHeadersMapper.getSkjemaEventHeaders(consumerRecord));
                    event.setName(eventName);
                    event.setType(EventType.INFO);
                    event.setTimestamp(LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(consumerRecord.timestamp()),
                            ZoneId.systemDefault() // TODO: 25/03/2022 ZoneId
                    ));

                    eventRepository.save(event);
                },
                new CommonLoggingErrorHandler()
        );
    }

    private EventTopicNameParameters createEventTopicNameParameters(String eventName) {
        return EventTopicNameParameters.builder()
                .orgId(orgId)
                .domainContext("skjema")
                .eventName(eventName)
                .build();
    }

}
