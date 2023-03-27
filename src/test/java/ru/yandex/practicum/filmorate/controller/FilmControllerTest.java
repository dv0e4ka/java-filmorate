package ru.yandex.practicum.filmorate.controller;

import com.google.gson.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.config.DurationAdapter;
import ru.yandex.practicum.filmorate.config.LocalDateTypeAdapter;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.yandex.practicum.filmorate.controller.FilmController.MIN_FILM_REALISE_DATE;

@SpringBootTest
class FilmControllerTest {
    @Autowired
    private FilmController filmController;
    private static Gson gson;
    private Film filmMatrix;

    @BeforeAll
    static void setUpGson() {
         gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
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
        Film filmReturn = filmController.addFilm(filmMatrix);
        assertEquals(filmMatrix, filmReturn);
    }

    @Test
    void shouldGetWrongName() {
        Film noNameFilm = filmMatrix;
        noNameFilm.setName("");
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(noNameFilm)
        );
        assertEquals("фильм введён без названия", exception.getMessage());
    }

    @Test
    void shouldGetWrongDescription() {
        Film filmDescriptionError = filmMatrix;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 201; i++) {
            builder.append("X");
        }
        String longDescription = builder.toString();
        filmDescriptionError.setDescription(longDescription);
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(filmDescriptionError)
        );
        assertEquals("описание фильма превышает 200 символов", exception.getMessage());
    }

    @Test
    void shouldGetWrongReleaseDate() {
        Film filmReleaseError = filmMatrix;
        filmReleaseError.setReleaseDate(LocalDate.of(1895, 12, 27));
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(filmReleaseError)
        );
        assertEquals("фильм вышел раньше " + MIN_FILM_REALISE_DATE.toString(), exception.getMessage());
    }

    @Test
    void shouldGetWrongZeroDuration() {
        Film filmReleaseError = filmMatrix;
        filmReleaseError.setDuration(0);
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(filmReleaseError)
        );
        assertEquals("некорректные данные по продолжительности фильма" , exception.getMessage());
    }

    @Test
    void shouldGetWrongNegativeDuration() {
        Film filmReleaseError = filmMatrix;
        filmReleaseError.setDuration(-1);
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(filmReleaseError)
        );
        assertEquals("некорректные данные по продолжительности фильма", exception.getMessage());
    }

    @Test
    void shouldUpdateFilm() throws ValidationException {
        Film filmReturn = filmController.addFilm(filmMatrix);
        int idFilmReturned = filmReturn.getId();
        filmReturn.setId(idFilmReturned);
        filmReturn.setName("Triangle of Sadness");
        filmReturn.setDescription("fresh");
        filmReturn.setDuration(147);
        filmReturn.setReleaseDate(LocalDate.of(2022, 12, 1));
        Film updatedFilm = filmController.updateFilm(filmReturn);
        assertEquals(filmMatrix, filmReturn);
    }

    @Test
    void shouldMissIdWhenUpdate() throws ValidationException {
        int newId = filmController.addFilm(filmMatrix).getId() + 999;
        filmMatrix.setId(newId);
        filmMatrix.setName("new description");
        System.out.println(newId);
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.updateFilm(filmMatrix)
        );
        assertEquals("Фильм с Id '" + newId + "' не найден в сервисе", exception.getMessage());
    }
}