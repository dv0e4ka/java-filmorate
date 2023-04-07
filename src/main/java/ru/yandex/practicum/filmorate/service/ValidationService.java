package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Service
@Slf4j
public class ValidationService {
    public static final LocalDate MIN_FILM_REALISE_DATE = LocalDate.of(1895, 12, 28);

    public boolean validateFilm(Film film) {
        String exception = "";
        if (film.getReleaseDate().isBefore(MIN_FILM_REALISE_DATE)) {
            exception = "фильм вышел раньше " + MIN_FILM_REALISE_DATE.toString();
            log.error(exception);
            throw new ValidationException(exception);
        }
        return true;
    }
}
