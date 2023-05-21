package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmDao {
    Film add(Film film);

    Film update(Film film);

    void delete(long id);

    Film getById(long id);

    List<Film> getAllFilms();

    Set<Film> getCommonFilms(long userId, long friendId);

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    List<Film> getMostPopularFilm(int count, int genreId, int year);

    boolean isContains(long id);
}
