package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film add(Film film);

    Film update(Film film);

    void delete(long id);

    Film getById(long id);

    List<Film> getAllFilms();

    Film addLike(long userId, long filmId);

    Film deleteLike(long userId, long filmId);

    List getPopularFilms(int count);
}
