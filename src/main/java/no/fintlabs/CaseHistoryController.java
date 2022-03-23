package no.fintlabs;

import no.fintlabs.model.Event;
import no.fintlabs.repositories.EventRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/sakshistorikk")
public class CaseHistoryController {

    private final EventRepository eventRepository;

    public CaseHistoryController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping("hendelser")
    public ResponseEntity<Collection<Event>> getEvents() {
        return ResponseEntity.ok(eventRepository.findAll());
    }

    @GetMapping("hendelser/correlationId/{correlationId}")
    public ResponseEntity<Collection<Event>> getEventsByCorrelationId(@PathVariable Long correlationId) {
        return ResponseEntity.ok(eventRepository.findAllByCorrelationId(correlationId));
    }

    @GetMapping("hendelser/instanceId/{instanceId}")
    public ResponseEntity<Collection<Event>> getEventsByInstanceId(@PathVariable Long instanceId) {
        return ResponseEntity.ok(eventRepository.findAllByInstanceId(instanceId));
    }

    @GetMapping("hendelser/integrationId/{integrationId}")
    public ResponseEntity<Collection<Event>> getEventsByIntegrationId(@PathVariable String integrationId) {
        return ResponseEntity.ok(eventRepository.findAllBySourceApplicationIntegrationId(integrationId));
    }

}