package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {
    void addGenre(long id, List<Genre> genres);

    void updateGenre(long id, List<Genre> genres);

    void deleteGenre(long id);

    List<Genre> getALlGenreByFilm(long id);

    Genre getGenre(int id);

    List<Genre> getAllGenres();
}
