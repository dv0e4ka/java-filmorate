package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Pattern(regexp = "\\S+")
    @NotEmpty
    @NotBlank
    private String login;
    private int id;
    @Email
    private String email;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
}