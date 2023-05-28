package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class FilmService {
    private final FilmDao filmDao;
    private final UserDao userDao;
    private final GenreService genreService;
    private final DirectorService directorService;

    @Autowired
    public FilmService(FilmDao filmDao, UserDao userDao, GenreService genreService, DirectorService directorService) {
        this.filmDao = filmDao;
        this.userDao = userDao;
        this.genreService = genreService;
        this.directorService = directorService;
    }

    public Film add(Film film) {
        ValidationService.validateFilm(film);
        Film addedFilm = filmDao.add(film);
        List<Genre> addedGenres = genreService.addGenre(addedFilm.getId(), film.getGenres());
        addedFilm.setGenres(addedGenres);
        List<Director> addedDirectors = directorService.addDirectorFilm(addedFilm.getId(), film.getDirectors());
        addedFilm.setDirectors(addedDirectors);
        return addedFilm;
    }

    public Film update(Film film) {
        ValidationService.validateFilm(film);
        Film updatedFilm = filmDao.update(film);
        List<Genre> updatedGenres = genreService.updateGenre(film.getId(), film.getGenres());
        updatedFilm.setGenres(updatedGenres);
        List<Director> updatedDirectors = directorService.updateDirectorFilm(updatedFilm.getId(), film.getDirectors());
        updatedFilm.setDirectors(updatedDirectors);
        return updatedFilm;
    }

    public void delete(long id) {
        //Поменял местами, из-за того, как устроена данная БД.
        genreService.deleteGenre(id);
        filmDao.delete(id);
    }

    public Film getById(long id) {
        Film film = filmDao.getById(id);
        film.setGenres(genreService.getALlGenreByFilm(id));
        film.setDirectors(directorService.getAllDirectorsByFilm(id));
        return film;
    }

    public List<Film> getAllFilms() {
        List<Film> filmList = filmDao.getAllFilms();
        for (Film film : filmList) {
            film.setGenres(genreService.getALlGenreByFilm(film.getId()));
            film.setDirectors(directorService.getAllDirectorsByFilm(film.getId()));
        }
        return filmList;
    }

    public Set<Film> getCommonFilms(long userId, long friendId) {
        Set<Film> filmList = filmDao.getCommonFilms(userId, friendId);
        for (Film film : filmList) {
            film.setGenres(genreService.getALlGenreByFilm(film.getId()));
            film.setDirectors(directorService.getAllDirectorsByFilm(film.getId()));
        }
        return filmList;
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

    public List<Film> getPopularFilms(int count, int genreId, int year) {
        List<Film> filmList = filmDao.getMostPopularFilm(count, genreId, year);
        for (Film film : filmList) {
            film.setGenres(genreService.getALlGenreByFilm(film.getId()));
            film.setDirectors(directorService.getAllDirectorsByFilm(film.getId()));
        }
        return filmList;
    }

    public List<Film> getFilmsByDirector(long directorId, String sortBy) {
        if (!directorService.isDirectorExists(directorId)) {
            throw new DirectorNotFoundException("не найден режиссер для получения фильмов с id=" + directorId);
        }
        List<Film> filmList = filmDao.getFilmsByDirector(directorId, sortBy);
        for (Film film : filmList) {
            film.setGenres(genreService.getALlGenreByFilm(film.getId()));
            film.setDirectors(directorService.getAllDirectorsByFilm(film.getId()));
        }
        return filmList;
    }

    public List<Film> searchFilms(String query, List<String> by) {
        List<Film> filmList = filmDao.searchFilms(query, by);
        for (Film film : filmList) {
            film.setGenres(genreService.getALlGenreByFilm(film.getId()));
            film.setDirectors(directorService.getAllDirectorsByFilm(film.getId()));
        }
        return filmList;
    }
}