//package no.fintlabs;
//
//import no.fintlabs.kafka.event.EventTopicNameParameters;
//import no.fintlabs.kafka.event.FintKafkaEventConsumerFactory;
//import no.fintlabs.model.Data;
//import no.fintlabs.model.Error;
//import no.fintlabs.model.Event;
//import no.fintlabs.model.EventType;
//import no.fintlabs.repositories.EventRepository;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.common.header.Headers;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.listener.CommonLoggingErrorHandler;
//import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
//
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//
//@Configuration
//public class ConsumerConfiguration {
//
//    @Value("${fint.org-id}")
//    private String orgId;
//
//    private final EventRepository eventRepository;
//
//    private final FintKafkaEventConsumerFactory fintKafkaEventConsumerFactory;
//
//    public ConsumerConfiguration(EventRepository eventRepository, FintKafkaEventConsumerFactory fintKafkaEventConsumerFactory) {
//        this.eventRepository = eventRepository;
//        this.fintKafkaEventConsumerFactory = fintKafkaEventConsumerFactory;
//    }
//
//    private EventTopicNameParameters createEventTopicNameParameters(String eventName) {
//        return EventTopicNameParameters.builder()
//                .orgId(orgId)
//                .domainContext("skjema")
//                .eventName(eventName)
//                .build();
//    }
//
//    @Bean
//    public ConcurrentMessageListenerContainer<String, String> newInstanceListener() {
//        return fintKafkaEventConsumerFactory.createConsumer(
//                createEventTopicNameParameters("new-instance"),
//                String.class,
//                consumerRecord -> {
//                    Headers headers = consumerRecord.headers();
//
//                    Event event = createEventWithHeaderProperties(headers);
//                    event.setType(EventType.INFO);
//                    event.setDescription("New instance");
//                    event.setTimestamp(LocalDateTime.parse(getHeaderValue(headers, "timestamp")));
//
//                    Data instanceData = new Data();
//                    instanceData.setContentType("Instance"); // TODO: 23/03/2022 Info with JSON/kafka headers?
//                    instanceData.setContent(consumerRecord.value());
//                    event.getData().put("instance", instanceData);
//
//                    eventRepository.save(event);
//                },
//                new CommonLoggingErrorHandler()
//        );
//    }
//
//    @Bean
//    public ConcurrentMessageListenerContainer<String, Error> instanceProcessingErrorListener() {
//        return fintKafkaEventConsumerFactory.createConsumer(
//                createEventTopicNameParameters("instance-processing-error"),
//                Error.class,
//                (ConsumerRecord<String, Error> consumerRecord) -> processError(consumerRecord, "Instance processing error"),
//                new CommonLoggingErrorHandler()
//        );
//    }
//
//    @Bean
//    public ConcurrentMessageListenerContainer<String, Error> instanceToCaseMappingErrorListener() {
//        return fintKafkaEventConsumerFactory.createConsumer(
//                createEventTopicNameParameters("instance-to-case-mapping-error"),
//                Error.class,
//                (ConsumerRecord<String, Error> consumerRecord) -> processError(consumerRecord, "Instance to case mapping error"),
//                new CommonLoggingErrorHandler()
//        );
//    }
//
//    @Bean
//    public ConcurrentMessageListenerContainer<String, Error> caseDispatchingErrorListener() {
//        return fintKafkaEventConsumerFactory.createConsumer(
//                createEventTopicNameParameters("case-dispatching-error"),
//                Error.class,
//                (ConsumerRecord<String, Error> consumerRecord) -> processError(consumerRecord, "Case dispatching error"),
//                new CommonLoggingErrorHandler()
//        );
//    }
//
//    private void processError(ConsumerRecord<String, Error> consumerRecord, String description) {
//        Headers headers = consumerRecord.headers();
//        Error error = consumerRecord.value();
//
//        Event event = createEventWithHeaderProperties(headers);
//        event.setType(EventType.ERROR);
//        event.setDescription(description);
//        event.setTimestamp(error.getTimestamp());
//
//        eventRepository.save(event);
//    }
//
//    private Event createEventWithHeaderProperties(Headers headers) {
//        Event event = new Event();
//        event.setOrgId("orgId"); // TODO: 22/03/2022 Application prop?
//        event.setCorrelationId(Long.parseLong(getHeaderValue(headers, "correlation.id")));
//        event.setSourceApplication(getHeaderValue(headers, "source.application"));
//        event.setSourceApplicationIntegrationId(getHeaderValue(headers, "source.application.integration.id"));
//        event.setSourceApplicationInstanceId(getHeaderValue(headers, "source.application.instance.id"));
//        event.setInstanceId(Long.parseLong(getHeaderValue(headers, "instance.id")));
//        event.setService(getHeaderValue(headers, "origin.application.id"));
//        return event;
//    }
//
//    private String getHeaderValue(Headers headers, String headerKey) {
//        return new String(headers.lastHeader(headerKey).value(), StandardCharsets.UTF_8);
//    }
//
//}
