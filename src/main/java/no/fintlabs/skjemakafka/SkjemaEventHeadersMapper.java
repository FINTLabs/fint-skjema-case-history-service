package no.fintlabs.skjemakafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SkjemaEventHeadersMapper {

    private final ObjectMapper objectMapper;

    public SkjemaEventHeadersMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public SkjemaEventHeaders getSkjemaEventHeaders(ConsumerRecord<?, ?> consumerRecord) {
        try {
            return objectMapper.readValue(
                    consumerRecord.headers().lastHeader("skjema.event.headers").value(),
                    SkjemaEventHeaders.class
            );
        } catch (IOException e) {
            // TODO: 26/03/2022 Handle exception
            return null;
        }
    }

}
