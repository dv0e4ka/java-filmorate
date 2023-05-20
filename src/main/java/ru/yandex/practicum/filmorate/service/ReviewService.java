package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.implement.ReviewDaoImpl;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Service
@Slf4j
public class ReviewService {
    private final ReviewDaoImpl reviewDao;

    @Autowired
    public ReviewService(ReviewDaoImpl reviewDao) {
        this.reviewDao = reviewDao;
    }

    public Review add(Review review) {
       return reviewDao.add(review);
    }

    public Review update(Review review) {
        return reviewDao.update(review);
    }

    public void delete(long id) {
        reviewDao.delete(id);
    }

    public Review getById(long id) {
        return reviewDao.getById(id);
    }

    public List getAllReviews(long filmId, int count) {
        return reviewDao.getAllReviews(filmId, count);
    }

    public void addLike(long id, long userId) {
      reviewDao.addLike(id, userId);
    }

    public void deleteLike(long id, long userId) {
        reviewDao.deleteLike(id, userId);
    }

    public void addDislike(long id, long userId) {
        reviewDao.addDislike(id, userId);
    }

    public void deleteDislike(long id, long userId) {
        reviewDao.deleteDislike(id, userId);
    }
}
