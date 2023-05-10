package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.UserDao;

import java.util.List;

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

    public void addLike(long filmId, long userId) {
        if (userDao.getById(userId) == null) {
            throw new UserNotFoundException("не найден пользователь с id " + userId);
        }
        if (!filmDao.isContains(filmId)) {
            throw new FilmNotFoundException("фильм с id " + filmId + " не найден");
        }
        filmDao.addLike(filmId, userId);
        log.info("Пользователь с id " + userId + " поставил лайк фильму с id " + filmId + "!");
    }

    public void deleteLike(long filmId, long userId) {
        if (userDao.getById(userId) == null) {
            throw new UserNotFoundException("не найден пользователь с id " + userId);
        }
        if (!filmDao.isContains(filmId)) {
            throw new FilmNotFoundException("фильм с id " + filmId + " не найден");
        }
        filmDao.deleteLike(userId, filmId);
        log.info("Пользователь с id " + userId + " удалил лайк фильму с id " + filmId + "!");
    }

    public List<Film> getPopularFilms(int count) {
        return filmDao.getMostPopularFilm(count);
    }
}