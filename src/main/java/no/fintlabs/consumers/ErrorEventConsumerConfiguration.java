package no.fintlabs.consumers;

import no.fintlabs.kafka.event.error.ErrorCollection;
import no.fintlabs.kafka.event.error.ErrorEventTopicNameParameters;
import no.fintlabs.kafka.event.error.FintKafkaErrorEventConsumerFactory;
import no.fintlabs.model.Error;
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
import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
public class ErrorEventConsumerConfiguration {


    @Value("${fint.org-id}")
    private String orgId;

    private final EventRepository eventRepository;
    private final FintKafkaErrorEventConsumerFactory fintKafkaErrorEventConsumerFactory;
    private final SkjemaEventHeadersMapper skjemaEventHeadersMapper;

    public ErrorEventConsumerConfiguration(
            EventRepository eventRepository,
            FintKafkaErrorEventConsumerFactory fintKafkaErrorEventConsumerFactory,
            SkjemaEventHeadersMapper skjemaEventHeadersMapper
    ) {
        this.eventRepository = eventRepository;
        this.fintKafkaErrorEventConsumerFactory = fintKafkaErrorEventConsumerFactory;
        this.skjemaEventHeadersMapper = skjemaEventHeadersMapper;
    }

//    @Bean
//    public ConcurrentMessageListenerContainer<String, ErrorCollection> instanceProcessingErrorListener() {
//        return createErrorEventListener("instance-processing-error");
//    }
//
//    @Bean
//    public ConcurrentMessageListenerContainer<String, ErrorCollection> instanceToCaseMappingErrorListener() {
//        return createErrorEventListener("instance-to-case-mapping-error");
//    }
//
//    @Bean
//    public ConcurrentMessageListenerContainer<String, ErrorCollection> caseDispatchingErrorListener() {
//        return createErrorEventListener("case-dispatching-error");
//    }

    private ConcurrentMessageListenerContainer<String, ErrorCollection> createErrorEventListener(String errorEventName) {
        return fintKafkaErrorEventConsumerFactory.createConsumer(
                createErrorEventTopicNameParameters(errorEventName),
                consumerRecord -> {
                    Event event = new Event();
                    event.setSkjemaEventHeaders(skjemaEventHeadersMapper.getSkjemaEventHeaders(consumerRecord));
                    event.setName(errorEventName);
                    event.setType(EventType.ERROR);
                    event.setTimestamp(LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(consumerRecord.timestamp()),
                            ZoneId.systemDefault() // TODO: 25/03/2022 ZoneId
                    ));
                    event.setErrors(mapToErrorEntities(consumerRecord.value()));
                    eventRepository.save(event);
                },
                new CommonLoggingErrorHandler()
        );
    }

    private Collection<Error> mapToErrorEntities(ErrorCollection errorCollection) {
        return errorCollection.getErrors().stream().map(this::mapToErrorEntity).collect(Collectors.toList());
    }

    private Error mapToErrorEntity(no.fintlabs.kafka.event.error.Error errorFromEvent) {
        Error error = new Error();
        error.setErrorCode(errorFromEvent.getErrorCode());
        error.setArgs(errorFromEvent.getArgs());
        return error;
    }

    private ErrorEventTopicNameParameters createErrorEventTopicNameParameters(String errorEventName) {
        return ErrorEventTopicNameParameters.builder()
                .orgId(orgId)
                .domainContext("skjema")
                .errorEventName(errorEventName)
                .build();
    }

}
