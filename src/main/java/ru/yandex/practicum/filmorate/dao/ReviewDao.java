package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewDao {
    Review add(Review review);

    Review update(Review review);

    void delete(long id);

    Review getById(long id);

   List<Review> getAllReviews(long filmId, int count);

    void addLike(long id, long userId);

    void deleteLike(long id, long userId);

    void addDislike(long id, long userId);

    void deleteDislike(long id, long userId);

     boolean isContainReview(long id);

     boolean hasAlreadyBeenLiked(long id, long userId);

     boolean hasAlreadyBeenDisliked(long id, long userId);
}
