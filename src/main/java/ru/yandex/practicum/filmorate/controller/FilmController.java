package ru.yandex.practicum.filmorate.controller;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private HashMap<Integer, Film> getFilms = new HashMap<>();
    public static final LocalDate MIN_FILM_REALISE_DATE = LocalDate.of(1895, 12, 28);
    private int id = 0;


    @PostMapping
    public Film addFilm(@NonNull @RequestBody Film film) throws ValidationException {
        log.info("Получен запрос на добавление фильма: " + film.getName());
        validateFilm(film);
        film.setId(++id);
        getFilms.put(film.getId(), film);
        log.info("Фильм" + film.getName() + "добавлен");
        return film;
    }

    @PutMapping
    public Film updateFilm(@NonNull @RequestBody Film film) throws ValidationException {
        int filmId = film.getId();
        log.info("Получен запрос на обновление фильма с Id " + filmId);
        validateFilm(film);
        if (!getFilms.containsKey(filmId)) {
            String exception = "Фильм с Id '" + filmId + "' не найден в сервисе";
            log.error(exception);
            throw new ValidationException(exception);
        }
        getFilms.put(filmId, film);
        log.info("Фильм c Id '" + filmId + "' обновлён");
        return film;
    }

    @GetMapping
    public List getFilms() {
        return new ArrayList(getFilms.values());

    }

    private boolean validateFilm(Film film) throws ValidationException {
        String exception = "";
        if (film.getName().isBlank()) {
            exception = "фильм введён без названия";
            log.error(exception);
            throw new ValidationException(exception);
        }

        if (film.getDescription().length() > 200) {
            exception = "описание фильма превышает 200 символов";
            log.error(exception);
            throw new ValidationException(exception);
        }
        if (film.getReleaseDate().isBefore(MIN_FILM_REALISE_DATE)) {
            exception = "фильм вышел раньше " + MIN_FILM_REALISE_DATE.toString();
            log.error(exception);
            throw new ValidationException(exception);
        }
        if (film.getDuration() <= 0) {
            log.error(exception);
            exception = "некорректные данные по продолжительности фильма";
            throw new ValidationException(exception);
        }
        return true;
    }
}