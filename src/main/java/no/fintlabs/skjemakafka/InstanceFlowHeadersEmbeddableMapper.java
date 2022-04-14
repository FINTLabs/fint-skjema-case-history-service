package no.fintlabs.skjemakafka;

import no.fintlabs.flyt.kafka.headers.InstanceFlowHeaders;
import org.springframework.stereotype.Service;

@Service
public class InstanceFlowHeadersEmbeddableMapper {

    public InstanceFlowHeadersEmbeddable getSkjemaEventHeaders(InstanceFlowHeaders instanceFlowHeaders) {
        return InstanceFlowHeadersEmbeddable.builder()
                .orgId(instanceFlowHeaders.getOrgId())
                .service(instanceFlowHeaders.getService())
                .sourceApplication(instanceFlowHeaders.getSourceApplication())
                .sourceApplicationIntegrationId(instanceFlowHeaders.getSourceApplicationIntegrationId())
                .sourceApplicationInstanceId(instanceFlowHeaders.getSourceApplicationInstanceId())
                .correlationId(instanceFlowHeaders.getCorrelationId())
                .instanceId(instanceFlowHeaders.getInstanceId())
                .configurationId(instanceFlowHeaders.getConfigurationId())
                .caseId(instanceFlowHeaders.getCaseId())
                .dispatchId(instanceFlowHeaders.getDispatchId())
                .build();
    }

}
