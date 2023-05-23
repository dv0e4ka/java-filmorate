package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.implement.FilmDaoImpl;
import ru.yandex.practicum.filmorate.dao.implement.ReviewDaoImpl;
import ru.yandex.practicum.filmorate.dao.implement.UserDaoImpl;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Service
@Slf4j
public class ReviewService {
    private final ReviewDaoImpl reviewDao;
    private final UserDaoImpl userDao;
    private final FilmDaoImpl filmDao;

    @Autowired
    public ReviewService(ReviewDaoImpl reviewDao, UserDaoImpl userDao, FilmDaoImpl filmDao) {
        this.reviewDao = reviewDao;
        this.userDao = userDao;
        this.filmDao = filmDao;
    }

    public Review add(Review review) {
        if ((review.getUserId() == null) || (review.getFilmId() == null)) {
            throw new ValidationException("Поле 'id' не должно быть пустым.");
        }
        if (!userDao.isContains(review.getUserId())) {
            throw new UserNotFoundException("Пользователь с указанным id не найден");
        }
        if (!filmDao.isContains(review.getFilmId())) {
            throw new FilmNotFoundException("Фильм с указанным id не найден");
        }
        if (review.getIsPositive() == null) {
            throw new ValidationException("Поле 'isPositive' не должно быть пустым.");
        }
        log.info("Добавлен отзыв к фильму с id = {}", review.getFilmId());
        return reviewDao.add(review);
    }

    public Review update(Review review) {
        if (reviewDao.isContainReview(review.getReviewId())) {
            throw new ReviewNotFoundException("Отзыв с указанным id не найден");
        }
        log.info("Обновлена информация о фильме в отзыве с id = {}", review.getReviewId());
        return reviewDao.update(review);
    }

    public void delete(long id) {
        reviewDao.delete(id);
    }

    public Review getById(long id) {
        if (reviewDao.isContainReview(id)) {
            throw new ReviewNotFoundException("Отзыв с указанным id не найден");
        }
        return reviewDao.getById(id);
    }

    public List<Review> getAllReviews(long filmId, int count) {
        if ((filmId != 0) && (!(filmDao.isContains(filmId)))) {
            throw new FilmNotFoundException("Фильм с указанным id не найден");
        }
        if (count <= 0) {
            throw new ValidationException("count. Значение параметра запроса не должно быть меньше 1.");
        }
        return reviewDao.getAllReviews(filmId, count);
    }

    public void addLike(long id, long userId) {
        isExist(id, userId);
        if (reviewDao.hasAlreadyBeenLiked(id, userId)) {
            throw new ReviewHasAlreadyBeenRatedException("Отзыву с id = " + id + " уже была поставлена оценка.");
        }
        if (reviewDao.hasAlreadyBeenDisliked(id, userId)) {
            deleteDislike(id, userId);
        }
        reviewDao.addLike(id, userId);
    }

    public void deleteLike(long id, long userId) {
        isExist(id, userId);
        if (!reviewDao.hasAlreadyBeenLiked(id, userId)) {
            throw new ReviewHasNotBeenRatedException("Отзыву  с id = " + id + " не была поставлена оценка.");
        }
        reviewDao.deleteLike(id, userId);
    }

    public void addDislike(long id, long userId) {
        isExist(id, userId);
        if (reviewDao.hasAlreadyBeenDisliked(id, userId)) {
            throw new ReviewHasAlreadyBeenRatedException("Отзыву  с id = " + id + " уже была поставлена оценка.");
        }
        if (reviewDao.hasAlreadyBeenLiked(id, userId)) {
            deleteLike(id, userId);
        }
        reviewDao.addDislike(id, userId);
    }

    public void deleteDislike(long id, long userId) {
        isExist(id, userId);
        if (!reviewDao.hasAlreadyBeenDisliked(id, userId)) {
            throw new ReviewHasNotBeenRatedException("Отзыву  с id = " + id + " не была поставлена оценка.");
        }
        reviewDao.deleteDislike(id, userId);
    }

    private boolean isExist(long id, long userId) {
        if (reviewDao.isContainReview(id)) {
            throw new ReviewHasNotBeenRatedException("Отзыв с указанным id не найден");
        }
        if (!userDao.isContains(userId)) {
            throw new UserNotFoundException("Пользователь с указанным id не найден");
        }
        return true;
    }
}