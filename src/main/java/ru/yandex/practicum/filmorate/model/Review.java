package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    long reviewId;
    String content;
    boolean isPositive;
    long userId;
    long filmId;
    long useful = 0;
}
