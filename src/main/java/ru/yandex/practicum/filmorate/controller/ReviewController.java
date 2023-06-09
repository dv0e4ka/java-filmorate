package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public Review add(@Valid @RequestBody Review review) {
        log.info("Поступил запрос на добавление отзыва с id = {}", review.getReviewId());
        return reviewService.add(review);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        log.info("Поступил запрос на обновление отзыва с id = {}", review.getReviewId());
        return reviewService.update(review);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        log.info("Поступил запрос на удаление отзыва с id = {}", id);
        reviewService.delete(id);
    }

    @GetMapping("/{id}")
    public Review getById(@PathVariable long id) {
        log.info("Поступил запрос получения отзыва с id = {}", id);
        return reviewService.getById(id);
    }

    @GetMapping
    public List<Review> getAllReviews(@RequestParam(defaultValue = "0") long filmId, @RequestParam(defaultValue = "10") int count) {
        log.info("Поступил запрос на получение всех отзывов с заданными параметрами");
        return reviewService.getAllReviews(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Поступил запрос на добавление лайка к отзыву с id = {} от пользователя с id = {}", id, userId);
        reviewService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Поступил запрос на удаление лайка к отзыву с id = {} от пользователя с id = {}", id, userId);
        reviewService.deleteLike(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislike(@PathVariable long id, @PathVariable long userId) {
        log.info("Поступил запрос на добавление дизлайка к отзыву с id = {} от пользователя с id = {}", id, userId);
        reviewService.addDislike(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislike(@PathVariable long id, @PathVariable long userId) {
        log.info("Поступил запрос на удаление дизлайка к отзыву с id = {} от пользователя с id = {}", id, userId);
        reviewService.deleteDislike(id, userId);
    }
}