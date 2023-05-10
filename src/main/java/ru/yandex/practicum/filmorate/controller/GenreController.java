package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@Slf4j
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable int id){
        log.info("поступил запрос на получение жанра id={}", id);
        return genreService.getGenre(id);
    }

    @GetMapping()
    public List<Genre> getAllGenres (){
        log.info("поступил запрос на получение списка жанров");
        return genreService.getAllGenres();
    }
}
