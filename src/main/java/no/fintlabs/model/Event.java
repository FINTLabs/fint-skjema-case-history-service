package no.fintlabs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.fintlabs.skjemakafka.SkjemaEventHeaders;

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
    private SkjemaEventHeaders skjemaEventHeaders; // TODO: 26/03/2022 Move to skjema-fint-kafka

    private String name;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private EventType type;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_id")
    private Collection<Error> errors;

}
