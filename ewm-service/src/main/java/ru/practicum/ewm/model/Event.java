package ru.practicum.ewm.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.ewm.model.enums.EventState;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String annotation;
    private String description;
    private LocalDateTime createdOn;
    private LocalDateTime eventDate;
    private LocalDateTime publishedOn;

    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventState state;

    private String title;

    @ManyToOne
    private Category category;

    @ManyToOne
    private User initiator;

    @Embedded
    private Location location;
}