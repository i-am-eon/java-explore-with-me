package ru.practicum.ewm.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.model.enums.EventState;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String annotation;

    @Column(length = 7000)
    private String description;

    private LocalDateTime createdOn;
    private LocalDateTime eventDate;
    private LocalDateTime publishedOn;

    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(length = 120)
    private String title;

    @ManyToOne
    private Category category;

    @ManyToOne
    private User initiator;

    @Embedded
    private Location location;
}