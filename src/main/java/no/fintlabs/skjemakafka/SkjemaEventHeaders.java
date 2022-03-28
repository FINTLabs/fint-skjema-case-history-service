package no.fintlabs.skjemakafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Embeddable
public class SkjemaEventHeaders {

    private String orgId;

    private String service;

    private String sourceApplication;

    private String sourceApplicationIntegrationId;

    private String sourceApplicationInstanceId;

    private Long correlationId;

    private Long instanceId;

    private Long configurationId;

    private Long caseId;

    private Long dispatchId;

}
