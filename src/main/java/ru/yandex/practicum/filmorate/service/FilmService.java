package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.film.FilmDao;
import ru.yandex.practicum.filmorate.dao.user.UserDao;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmDao filmDao;
    private final UserDao userDao;

    @Autowired
    public FilmService(FilmDao filmDao, UserDao userDao) {
        this.filmDao = filmDao;
        this.userDao = userDao;
    }

    public Film add(Film film) {
        ValidationService.validateFilm(film);
        return filmDao.add(film);
    }

    public Film update(Film film) {
        ValidationService.validateFilm(film);
        return filmDao.update(film);
    }

    public void delete(long id) {
        filmDao.delete(id);
    }

    public Film getById(long id) {
        return filmDao.getById(id);
    }

    public List<Film> getAllFilms() {
        return filmDao.getAllFilms();
    }

    public Film addLike(long filmId, long userId) {
        if (userDao.getById(userId) == null) {
            throw new UserNotFoundException("не найден пользователь с id " + userId);
        }
        if (!filmDao.isContains(filmId)) {
            throw new FilmNotFoundException("фильм с id " + filmId + " не найден");
        }
        Film film = filmDao.getById(filmId);
        film.addLike(userId);
        log.info("Пользователь с id " + userId + " поставил лайк фильму с id " + filmId + "!");
        return film;
    }

    public Film deleteLike(long userId, long filmId) {
        if (userDao.getById(userId) == null) {
            throw new UserNotFoundException("не найден пользователь с id " + userId);
        }
        if (!filmDao.isContains(filmId)) {
            throw new FilmNotFoundException("фильм с id " + filmId + " не найден");
        }
        Film film = filmDao.getById(filmId);
        film.deleteLike(userId);
        log.info("Пользователь с id " + userId + " удалил лайк фильму с id " + filmId + "!");
        return film;
    }

    public List getPopularFilms(int count) {
        if (filmDao.getAllFilms().size() == 0) {
            throw new FilmNotFoundException("фильмов в библиотке нет");
        }
        List<Film> films = getAllFilms()
                .stream()
                .sorted(Comparator.comparingInt(film -> -1 * film.getLikes().size()))
                .limit(count).collect(Collectors.toList());
        return films;
    }
}