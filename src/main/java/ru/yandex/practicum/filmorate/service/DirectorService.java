package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

@Service
@Slf4j
public class DirectorService {
    private final DirectorDao directorDao;

    @Autowired
    public DirectorService(DirectorDao directorDao) {
        this.directorDao = directorDao;
    }

    public Director add(Director director) {
        ValidationService.validateDirector(director);
        return directorDao.add(director);
    }

    public List<Director> addDirectorFilm(long id, List<Director> directors) {
        return directorDao.addDirectorFilm(id, directors);
    }

    public List<Director> updateDirectorFilm(long id, List<Director> directors) {
        return directorDao.updateDirectorFilm(id, directors);
    }

    public Director update(Director director) {
        return directorDao.update(director);
    }

    public void delete(int id) {
        directorDao.delete(id);
    }

    public List<Director> getAllDirectors() {
        return directorDao.getAllDirectors();
    }

    public List<Director> getAllDirectorsByFilm(long id) {
        return directorDao.getAllDirectorsByFilm(id);
    }

    public Director getById(int id) {
        return directorDao.getById(id);
    }

    public boolean isDirectorExists(long id) {
        return directorDao.isDirectorExists(id);
    }
}