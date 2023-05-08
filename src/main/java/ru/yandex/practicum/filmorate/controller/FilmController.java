package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        log.info("поступил запрос на добавление фильма с name=" + film.getName());
        return filmService.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("поступил запрос на обновление фильма с id " + film.getId());
        return filmService.update(film);
    }

    @DeleteMapping
    public void delete(int id) {
        log.info("поступил запрос на удаление фильма с id " + id);
        filmService.delete(id);
    }

    @GetMapping
    public List getAllFilms() {
        log.info("поступил запрос получение всех фильмов");
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable long id) {
        log.info("поступил запрос получения фильма с id " + id);
        return filmService.getById(id);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film addLike(@PathVariable long filmId, @PathVariable long userId) {
        log.info("поступил запрос на добавление лайка на фильм с id " + filmId + " от пользователя с id " + userId);
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable long filmId, @PathVariable long userId) {
        log.info("поступил запрос на добавление лайка на фильм с id " + filmId + " от пользователя с id " + userId);
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    List getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("получен запрос на получение 10 наиболее популярных фильмов");
        return filmService.getPopularFilms(count);
    }
}