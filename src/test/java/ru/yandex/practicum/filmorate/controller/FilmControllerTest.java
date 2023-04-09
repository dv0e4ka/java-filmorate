package ru.yandex.practicum.filmorate.controller;

import com.google.gson.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.util.DurationAdapterUtil;
import ru.yandex.practicum.filmorate.util.LocalDateTypeAdapterUtil;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.yandex.practicum.filmorate.service.ValidationService.MIN_FILM_REALISE_DATE;

@SpringBootTest
class FilmControllerTest {
    @Autowired
    private FilmController filmController;
    private static Gson gson;
    private Film filmMatrix;

    @BeforeAll
    static void setUpGson() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapterUtil())
                .registerTypeAdapter(Duration.class, new DurationAdapterUtil())
                .create();
    }

    @BeforeEach
    void setUpNewFilm() {
        filmMatrix = Film.builder()
                .name("The Matrix")
                .description("classic")
                .releaseDate(LocalDate.of(1999, 3, 31))
                .duration(136)
                .build();
    }

    @Test
    void shouldAddFilm() throws ValidationException {
        Film filmReturn = filmController.add(filmMatrix);
        assertEquals(filmMatrix, filmReturn);
    }

    @Test
    void shouldGetWrongReleaseDate() {
        Film filmReleaseError = filmMatrix;
        filmReleaseError.setReleaseDate(LocalDate.of(1895, 12, 27));
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.add(filmReleaseError)
        );
        assertEquals("фильм вышел раньше " + MIN_FILM_REALISE_DATE.toString(), exception.getMessage());
    }

    @Test
    void shouldUpdateFilm() throws ValidationException {
        Film filmReturn = filmController.add(filmMatrix);
        long idFilmReturned = filmReturn.getId();
        filmReturn.setId(idFilmReturned);
        filmReturn.setName("Triangle of Sadness");
        filmReturn.setDescription("fresh");
        filmReturn.setDuration(147);
        filmReturn.setReleaseDate(LocalDate.of(2022, 12, 1));
        Film updatedFilm = filmController.update(filmReturn);
        assertEquals(filmMatrix, filmReturn);
    }

    @Test
    void shouldMissIdWhenUpdate() throws ValidationException {
        long newId = filmController.add(filmMatrix).getId() + 999;
        filmMatrix.setId(newId);
        filmMatrix.setName("new description");
        FilmNotFoundException exception = assertThrows(
                FilmNotFoundException.class,
                () -> filmController.update(filmMatrix)
        );
        assertEquals("Фильм с Id '" + newId + "' не найден", exception.getMessage());
    }
}