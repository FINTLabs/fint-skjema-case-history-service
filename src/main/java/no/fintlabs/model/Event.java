package no.fintlabs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@lombok.Data
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private long id;

    private String orgId;

    private String service;

    private String sourceApplication;

    private String sourceApplicationIntegrationId;

    private String sourceApplicationInstanceId;

    private long instanceId;

    private long correlationId;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private EventType type;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_id")
    @MapKeyColumn(name = "map_key")
    @JsonPropertyOrder(alphabetic = true)
    private Map<String, no.fintlabs.model.Data> data;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_id")
    private List<Error> errors;

}
