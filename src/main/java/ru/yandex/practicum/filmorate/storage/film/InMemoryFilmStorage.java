package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.ValidationService;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> filmMap = new HashMap<>();
    private long id = 0;
    ValidationService validator;

    @Autowired
    public InMemoryFilmStorage(ValidationService validator) {
        this.validator = validator;
    }

    @Override
    public Film add(Film film) {
        validator.validateFilm(film);
        film.setId(++id);
        filmMap.put(film.getId(), film);
        log.info("Фильм " + film.getName() + " добавлен");
        return film;
    }

    @Override
    public Film update(Film film) {
        long filmId = film.getId();
        validator.validateFilm(film);
        if (!filmMap.containsKey(filmId)) {
            throw new FilmNotFoundException("Фильм с Id '" + filmId + "' не найден в сервисе");
        }
        filmMap.put(filmId, film);
        log.info("Фильм c Id '" + filmId + "' обновлён");
        return film;
    }

    @Override
    public void delete(long id) {
        if (!filmMap.containsKey(id)) {
            throw new FilmNotFoundException("Фильм с Id '" + id + "' не найден в сервисе");
        } else {
            filmMap.remove(id);
        }
    }

    @Override
    public Film getById(long id) {
        if (!filmMap.containsKey(id)) {
            throw new FilmNotFoundException("Фильм с Id '" + id + "' не найден в сервисе");
        }
        return filmMap.get(id);
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmMap.values());
    }

    @Override
    public Film addLike(long filmId, long userId) {
        if (!filmMap.containsKey(filmId)) {
            throw new FilmNotFoundException("фильм с id " + filmId + " не найден");
        }
        Film film = filmMap.get(filmId);
        film.addLike(userId);
        filmMap.put(filmId, film);
        log.info("Пользователь с id " + userId + " поставил лайк фильму с id " + filmId + "!");
        return film;
    }

    @Override
    public Film deleteLike(long userId, long filmId) {
        if (!filmMap.containsKey(filmId)) {
            throw new FilmNotFoundException("фильм с id " + filmId + " не найден");
        }
        Film film = filmMap.get(userId);
        film.deleteLike(userId);
        filmMap.put(filmId, film);
        log.info("Пользователь с id " + userId + " удалил лайк фильму с id " + filmId + "!");
        return film;
    }

    @Override
    public List getPopularFilms(int count) {
        if (filmMap.size() == 0) {
            throw new FilmNotFoundException("фильмов в библиотке нет");
        }
        List<Film> films = filmMap.values()
                .stream()
                .sorted(Comparator.comparingInt(film -> -1 * film.getLikes().size()))
                .limit(count).collect(Collectors.toList());
        return films;
    }
}