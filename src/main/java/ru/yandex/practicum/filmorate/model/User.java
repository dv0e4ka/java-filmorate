package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long id;
    @Pattern(regexp = "\\S+")
    @NotEmpty
    @NotBlank
    private String login;
    @Email
    private String email;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    private final Set<Long> friends = new HashSet<>();
}