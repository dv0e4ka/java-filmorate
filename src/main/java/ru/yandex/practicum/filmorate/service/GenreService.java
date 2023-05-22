package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
public class GenreService {
    private final GenreDao genreDao;

    @Autowired
    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public List<Genre> addGenre(long id, List<Genre> genres) {
        return genreDao.addGenre(id, genres);
    }

    public List<Genre> updateGenre(long id, List<Genre> genres) {
        return genreDao.updateGenre(id, genres);
    }

    public void deleteGenre(long id) {
        genreDao.deleteGenre(id);
    }

    public List<Genre> getALlGenreByFilm(long id) {
        return genreDao.getALlGenreByFilm(id);
    }

    public Genre getGenre(int id) {
        return genreDao.getGenre(id);
    }

    public List<Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }
}