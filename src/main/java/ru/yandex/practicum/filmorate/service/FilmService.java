package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmDao filmDao;
    private final UserDao userDao;
    private final GenreService genreService;

    @Autowired
    public FilmService(FilmDao filmDao, UserDao userDao, GenreService genreService) {
        this.filmDao = filmDao;
        this.userDao = userDao;
        this.genreService = genreService;
    }

    public Film add(Film film) {
        ValidationService.validateFilm(film);
        Film addedFilm = filmDao.add(film);
        List<Genre> addedGenres = genreService.addGenre(addedFilm.getId(), film.getGenres());
        addedFilm.setGenres(addedGenres);
        return addedFilm;
    }

    public Film update(Film film) {
        ValidationService.validateFilm(film);
        Film updatedFilm = filmDao.update(film);
        List<Genre> updatedGenres = genreService.updateGenre(film.getId(), film.getGenres());
        updatedFilm.setGenres(updatedGenres);
        return updatedFilm;
    }

    public void delete(long id) {
        filmDao.delete(id);
        genreService.deleteGenre(id);
    }

    public Film getById(long id) {
        Film film = filmDao.getById(id);
        film.setGenres(genreService.getALlGenreByFilm(id));
        return film;
    }

    public List<Film> getAllFilms() {
        List<Film> filmList = filmDao.getAllFilms();
        for (Film film : filmList) {
            film.setGenres(genreService.getALlGenreByFilm(film.getId()));
        }
        return filmList;
    }

    public void addLike(long filmId, long userId) {
        if (userDao.getById(userId) == null) {
            throw new EntityNotFoundException("не найден пользователь с id " + userId);
        }
        if (!filmDao.isContains(filmId)) {
            throw new EntityNotFoundException("фильм с id " + filmId + " не найден");
        }
        filmDao.addLike(filmId, userId);
        log.info("Пользователь с id " + userId + " поставил лайк фильму с id " + filmId + "!");
    }

    public void deleteLike(long filmId, long userId) {
        if (userDao.getById(userId) == null) {
            throw new EntityNotFoundException("не найден пользователь с id " + userId);
        }
        if (!filmDao.isContains(filmId)) {
            throw new EntityNotFoundException("фильм с id " + filmId + " не найден");
        }
        filmDao.deleteLike(userId, filmId);
        log.info("Пользователь с id " + userId + " удалил лайк фильму с id " + filmId + "!");
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> filmList = filmDao.getMostPopularFilm(count);
        for (Film film : filmList) {
            film.setGenres(genreService.getALlGenreByFilm(film.getId()));
        }
        return filmList;
    }
}