package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film add(Film film) {
        ValidationService.validateFilm(film);
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        ValidationService.validateFilm(film);
        return filmStorage.update(film);
    }

    public void delete(long id) {
        filmStorage.delete(id);
    }

    public Film getById(long id) {
        return filmStorage.getById(id);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addLike(long filmId, long userId) {
        if (userStorage.getById(userId) == null) {
            throw new UserNotFoundException("не найден пользователь с id " + userId);
        }
        if (!filmStorage.isContains(filmId)) {
            throw new FilmNotFoundException("фильм с id " + filmId + " не найден");
        }
        Film film = filmStorage.getById(filmId);
        film.addLike(userId);
        log.info("Пользователь с id " + userId + " поставил лайк фильму с id " + filmId + "!");
        return film;
    }

    public Film deleteLike(long userId, long filmId) {
        if (userStorage.getById(userId) == null) {
            throw new UserNotFoundException("не найден пользователь с id " + userId);
        }
        if (!filmStorage.isContains(filmId)) {
            throw new FilmNotFoundException("фильм с id " + filmId + " не найден");
        }
        Film film = filmStorage.getById(filmId);
        film.deleteLike(userId);
        log.info("Пользователь с id " + userId + " удалил лайк фильму с id " + filmId + "!");
        return film;
    }

    public List getPopularFilms(int count) {
        if (filmStorage.getAllFilms().size() == 0) {
            throw new FilmNotFoundException("фильмов в библиотке нет");
        }
        List<Film> films = getAllFilms()
                .stream()
                .sorted(Comparator.comparingInt(film -> -1 * film.getLikes().size()))
                .limit(count).collect(Collectors.toList());
        return films;
    }
}