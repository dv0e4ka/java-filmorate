package ru.yandex.practicum.filmorate.dao.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Repository
@Slf4j
public class ReviewDaoImpl implements ReviewDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review add(Review review) {
        return review;
    }

    @Override
    public Review update(Review review) {
        return review;
    }

    @Override
    public void delete(long id) {
    }

    @Override
    public Review getById(long id) {
        return null;
    }

    @Override
    public List getAllReviews(long filmId, int count) {
        return null;
    }

    @Override
    public void addLike(long id, long userId) {
    }

    @Override
    public void deleteLike(long id, long userId) {
    }

    @Override
    public void addDislike(long id, long userId) {

    }

    @Override
    public void deleteDislike(long id, long userId) {

    }
}
