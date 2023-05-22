package ru.yandex.practicum.filmorate.dao.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Repository
public class ReviewDaoImpl implements ReviewDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review add(Review review) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String reviewSql = "INSERT INTO REVIEWS (content, is_positive, user_id, film_id, useful) " +
                "VALUES(?, ?, ?, ?, ? )";
        jdbcTemplate.update(connection -> {
            String[] keyColumn = {"id"};
            PreparedStatement ps = connection.prepareStatement(reviewSql, keyColumn);
            ps.setString(1, review.getContent());
            ps.setBoolean(2, review.getIsPositive());
            ps.setLong(3, review.getUserId());
            ps.setLong(4, review.getFilmId());
            ps.setLong(5, review.getUseful());
            return ps;
        }, keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return getById(id);
    }

    @Override
    public Review update(Review review) {
        String sqlReview = "UPDATE reviews " +
                "SET content = ?, " +
                "is_positive = ? " +
                "WHERE ID = ?";
        jdbcTemplate.update(sqlReview, review.getContent(), review.getIsPositive(), review.getReviewId());
        return getById(review.getReviewId());
    }

    @Override
    public void delete(long id) {
        int sqlResult = jdbcTemplate.update("DELETE FROM reviews WHERE id = ?", id);
        if (sqlResult == 0) {
            throw new EntityNotFoundException("Отзыв с указанным id не найден");
        }
    }

    @Override
    public Review getById(long id) {
        String sqlReview = "SELECT r.id, r.content, r.is_positive, r.user_id, r.film_id, r.useful " +
                "FROM reviews as r " +
                "WHERE r.id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlReview, (rs, rowNum) -> makeReview(rs), id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Отзыв с указанным id не найден");
        }
    }

    @Override
    public List<Review> getAllReviews(long filmId, int count) {
        String sql = "SELECT * FROM reviews ";
        if (filmId != 0) {
            sql += "WHERE film_id = " + filmId + " ";
        }
        sql += "ORDER BY useful DESC " +
                "LIMIT " + count;
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeReview(rs));
    }

    @Override
    public void addLike(long id, long userId) {
        jdbcTemplate.update("INSERT INTO review_useful (review_id, user_id, liked, disliked) " +
                "VALUES (?, ?, true, false)", id, userId);

        jdbcTemplate.update("UPDATE reviews SET useful = useful + 1 " +
                "WHERE id = ?", id);
    }

    @Override
    public void deleteLike(long id, long userId) {
        jdbcTemplate.update("DELETE FROM review_useful " +
                "WHERE review_id = ? " +
                "AND user_id = ?", id, userId);
        jdbcTemplate.update("UPDATE reviews SET useful = useful - 1 " +
                "WHERE id = ?", id);
    }

    @Override
    public void addDislike(long id, long userId) {
        jdbcTemplate.update("INSERT INTO review_useful (review_id, user_id, liked, disliked) " +
                "VALUES (?, ?, false, true)", id, userId);

        jdbcTemplate.update("UPDATE reviews SET useful = useful - 1 " +
                "WHERE id = ?", id);
    }

    @Override
    public void deleteDislike(long id, long userId) {
        jdbcTemplate.update("DELETE FROM review_useful " +
                "WHERE review_id = ? " +
                "AND user_id = ?", id, userId);
        jdbcTemplate.update("UPDATE reviews SET useful = useful + 1 " +
                "WHERE id = ?", id);
    }

    private Review makeReview(ResultSet resultSet) throws SQLException {
        return new Review(resultSet.getInt("id"),
                resultSet.getString("content"),
                resultSet.getBoolean("is_positive"),
                resultSet.getLong("user_id"),
                resultSet.getLong("film_id"),
                resultSet.getLong("useful"));
    }

    @Override
    public boolean isContainReview(long id) {
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet("SELECT ID " +
                "FROM REVIEWS " +
                "WHERE ID = ?", id);
        return !reviewRows.next();
    }

    @Override
    public boolean hasAlreadyBeenLiked(long id, long userId) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * " +
                "FROM review_useful " +
                "WHERE review_id = ? " +
                "AND user_id = ?", id, userId);
        if (rowSet.next()) {
            boolean liked = rowSet.getBoolean("liked");
            return liked;
        }
        return false;
    }

    @Override
    public boolean hasAlreadyBeenDisliked(long id, long userId) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * " +
                "FROM review_useful " +
                "WHERE review_id = ? " +
                "AND user_id = ?", id, userId);
        if (rowSet.next()) {
            boolean disliked = rowSet.getBoolean("disliked");
            return disliked;
        }
        return false;
    }
}