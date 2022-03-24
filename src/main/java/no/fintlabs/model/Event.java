package no.fintlabs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@lombok.Data
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "service")
    private String service;

    @Column(name = "source_application")
    private String sourceApplication;

    @Column(name = "source_application_integration_id")
    private String sourceApplicationIntegrationId;

    @Column(name = "source_application_instance_id")
    private String sourceApplicationInstanceId;

    @Column(name = "instance_id")
    private long instanceId;

    @Column(name = "correlation_id")
    private long correlationId;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private EventType type;

    @Column(name = "description")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_id")
    @MapKeyColumn(name = "map_key")
    @JsonPropertyOrder(alphabetic = true)
    private Map<String, no.fintlabs.model.Data> data;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_id")
    private List<Error> errors ;

}
