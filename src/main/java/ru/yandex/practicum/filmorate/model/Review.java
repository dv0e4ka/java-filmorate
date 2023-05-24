package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private long reviewId;
    @NotBlank
    private String content;
    private Boolean isPositive;
    private Long userId;
    private Long filmId;
    private long useful = 0;
}