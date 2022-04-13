package no.fintlabs.repositories;

import no.fintlabs.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Collection<Event> findAllByInstanceFlowHeadersInstanceId(String instanceId);

    Collection<Event> findAllByInstanceFlowHeadersCorrelationId(String correlationId);

    Collection<Event> findAllByInstanceFlowHeadersSourceApplicationIntegrationId(String sourceApplicationIntegrationId);

}
