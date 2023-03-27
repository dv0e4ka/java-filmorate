package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class User {
    @Setter
    int id;
    String email;
    String login;
    @Setter
    String name;
    LocalDate birthday;
}
