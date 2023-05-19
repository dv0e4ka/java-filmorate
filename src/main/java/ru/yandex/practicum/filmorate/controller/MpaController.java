package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
@Slf4j
public class MpaController {
    private final MpaService mpaService;

    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable int id) {
        log.info("получен запрос на получение рейтинга MPA id={}", id);
        return mpaService.getMpa(id);
    }

    @GetMapping
    public List<Mpa> getMpaAll() {
        log.info("получен запрос на получение списка рейтинга MPA");
        return mpaService.getMpaAll();
    }
}
