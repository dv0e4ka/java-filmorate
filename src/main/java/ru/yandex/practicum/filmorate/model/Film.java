package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    @Setter
    int id;
    String name;
    String description;
    LocalDate releaseDate;
    int duration;
}
