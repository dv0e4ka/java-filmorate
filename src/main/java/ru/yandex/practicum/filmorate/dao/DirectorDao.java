package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorDao {
    Director add(Director director);

    List<Director> addDirectorFilm(long id, List<Director> directors);

    List<Director> updateDirectorFilm(long id, List<Director> directors);

    void deleteDirectorFilm(long id);

    Director update(Director director);

    void delete(int id);

    List<Director> getAllDirectors();

    List<Director> getAllDirectorsByFilm(long id);

    Director getById(int id);

    boolean isDirectorExists(long id);
}