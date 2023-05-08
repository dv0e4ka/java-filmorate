package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDao {
    Film add(Film film);

    Film update(Film film);

    void delete(long id);

    Film getById(long id);

    List<Film> getAllFilms();

    boolean isContains(long id);
}
