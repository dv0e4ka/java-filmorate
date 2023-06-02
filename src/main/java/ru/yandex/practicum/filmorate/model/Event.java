package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private long eventId;               //Главный ключ

    @NotNull
    private long userId;

    @NotBlank
    private EventType eventType;
    @NotBlank
    private OperationType operation;

    @NotNull
    private long entityId;

    @NotNull
    private Long timestamp;             //Храню числом из-за тестов

}
