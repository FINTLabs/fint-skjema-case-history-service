package no.fintlabs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.fintlabs.skjemakafka.InstanceFlowHeadersEmbeddable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

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

    @Embedded
    private InstanceFlowHeadersEmbeddable instanceFlowHeaders;

    private String name;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private EventType type;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_id")
    private Collection<Error> errors;

}
