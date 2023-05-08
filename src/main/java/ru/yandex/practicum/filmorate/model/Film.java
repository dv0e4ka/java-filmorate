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
public class Film {
    @NotBlank
    private String name;
    private long id;
    @Size(min = 1, max = 200)
    private String description;
    private LocalDate releaseDate;
    @Min(1)
    private int duration;
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();
    private Set<Long> likes = new HashSet<>();

    public void addLike(long id) {
        likes.add(id);
    }
    public void deleteLike(long id) {
        likes.remove(id);
    }
}