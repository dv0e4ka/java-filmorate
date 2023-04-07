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
    @Pattern(regexp = "\\S+")
    @NotEmpty
    @NotBlank
    private String login;
    private long id;
    @Email
    private String email;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    final private Set<Long> friends = new HashSet<>();

    public void addFriend(long id) {
        friends.add(id);
    }

    public void deleteFriend(long id) {
        friends.remove(id);
    }
}