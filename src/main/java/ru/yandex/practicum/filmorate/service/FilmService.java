package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film add(Film film) {
        return filmStorage.add(film);
    }

    public Film update(Film film) {
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
        return filmStorage.addLike(filmId, userId);
    }

    public Film deleteLike(long userId, long filmId) {
        if (userStorage.getById(userId) == null) {
            throw new UserNotFoundException("не найден пользователь с id " + userId);
        }
        return filmStorage.deleteLike(userId, filmId);
    }

    public List getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }
}