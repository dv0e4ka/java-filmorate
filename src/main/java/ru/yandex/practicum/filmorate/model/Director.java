package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Director {
    @NotNull
    private Integer id;
    @NotBlank
    @NotEmpty
    private String name;
}