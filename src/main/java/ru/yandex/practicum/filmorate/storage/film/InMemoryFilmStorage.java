package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> filmMap = new HashMap<>();
    private long id = 0;

    @Override
    public Film add(Film film) {
        film.setId(++id);
        filmMap.put(film.getId(), film);
        log.info("Фильм " + film.getName() + " добавлен");
        return film;
    }

    @Override
    public Film update(Film film) {
        long filmId = film.getId();

        if (!isContains(filmId)) {
            throw new FilmNotFoundException("Фильм с Id '" + filmId + "' не найден");
        }
        filmMap.put(filmId, film);
        log.info("Фильм c Id '" + filmId + "' обновлён");
        return film;
    }

    @Override
    public void delete(long id) {
        if (!isContains(id)) {
            throw new FilmNotFoundException("Фильм с Id '" + id + "' не найден");
        } else {
            filmMap.remove(id);
        }
    }

    @Override
    public Film getById(long id) {
        if (!isContains(id)) {
            throw new FilmNotFoundException("Фильм с Id '" + id + "' не найден");
        }
        return filmMap.get(id);
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmMap.values());
    }

    @Override
    public boolean isContains(long id) {
        return filmMap.containsKey(id);
    }
}