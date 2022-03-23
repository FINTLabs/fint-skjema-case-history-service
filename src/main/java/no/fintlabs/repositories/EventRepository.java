package no.fintlabs.repositories;

import no.fintlabs.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Collection<Event> findAllByInstanceId(long instanceId);

    Collection<Event> findAllByCorrelationId(long correlationId);

    Collection<Event> findAllBySourceApplicationIntegrationId(String sourceApplicationIntegrationId);

}
