package ru.yandex.practicum.filmorate.dao.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film add(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String filmSql = "INSERT INTO FILM (name, description, releaseDate, duration, mpa) " +
                "VALUES(?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            String[] keyColumn = {"id"};
            PreparedStatement ps = connection.prepareStatement(filmSql, keyColumn);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            Date date = Date.valueOf(film.getReleaseDate());
            ps.setDate(3, date);
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return getById(id);
    }

    @Override
    public Film update(Film film) {
        String filmSql = "UPDATE FILM SET name=?, " +
                "description=?, " +
                "releaseDate=?, " +
                "duration=?, " +
                "mpa=? " +
                "WHERE id=?";
        Date date = Date.valueOf(film.getReleaseDate());
        int sqlResult = jdbcTemplate.update(filmSql,
                film.getName(),
                film.getDescription(),
                date,
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        if (sqlResult == 0) {
            throw new EntityNotFoundException("не найден фильм для обновления с id=" + film.getId());
        } else {
            log.info("обновлен фильм с id=" + film.getId());
        }
        return getById(film.getId());
    }

    @Override
    public void delete(long id) {
        int sqlResult = jdbcTemplate.update("DELETE FROM FILM WHERE id=?", id);
        if (sqlResult == 0) {
            throw new EntityNotFoundException("не найден фильм для удаления с id=" + id);
        } else {
            log.info("удален фильм с id=" + id);
        }
    }

    @Override
    public Film getById(long id) {
        String sqlFilm = "SELECT f.id, f.name, f.description, f.releasedate, f.duration, f.mpa, " +
                "m.name AS mpa_name " +
                "FROM FILM AS f " +
                "INNER JOIN RATING_MPA AS m ON f.MPA=m.ID " +
                "WHERE f.ID=?";
        try {
            return jdbcTemplate.queryForObject(sqlFilm, (rs, rowNum) -> makeFilm(rs), id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("не найден фильм с id=" + id);
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT f.id, f.name, f.description, f.releasedate, f.duration, f.mpa, " +
                "m.name AS mpa_name " +
                "FROM FILM AS f " +
                "INNER JOIN RATING_MPA AS m ON f.mpa=m.ID";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public void addLike(long filmId, long userId) {
        String sql = "INSERT INTO LIKES (ID_FILM, ID_USER) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        String sql = "DELETE FROM LIKES WHERE ID_FILM=? AND ID_USER=?";
        jdbcTemplate.update(sql, userId, filmId);
    }

    @Override
    public List<Film> getMostPopularFilm(int count) {
        String sql = "SELECT L.ID_FILM as likes_count, " +
                "F.ID, " +
                "F.NAME, " +
                "F.DESCRIPTION, " +
                "F.RELEASEDATE, " +
                "F.DURATION, " +
                "F.MPA, " +
                "RM.NAME AS MPA_NAME " +
                "FROM FILM AS F " +
                "LEFT JOIN LIKES L on F.ID = L.ID_FILM " +
                "INNER JOIN RATING_MPA RM on F.MPA = RM.ID " +
                "ORDER BY likes_count DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), count);
    }

    @Override
    public boolean isContains(long id) {
        String sql = "SELECT ID " +
                "FROM FILM " +
                "WHERE ID=?";
        try {
            jdbcTemplate.queryForObject(sql, Long.class, id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private Film makeFilm(ResultSet resultSet) throws SQLException {
        long id = resultSet.getInt("id");
        Film film = new Film();
        film.setId(id);
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setDuration(resultSet.getInt("duration"));
        film.setReleaseDate(resultSet.getDate("releaseDate").toLocalDate());
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getInt("mpa"));
        mpa.setName(resultSet.getString("mpa_name"));
        film.setMpa(mpa);
        film.setLikes(getLikes(id));
        return film;
    }

    private List<Long> getLikes(long id) {
        String sql = "SELECT id_user FROM likes WHERE id_film=?";
        return jdbcTemplate.queryForList(sql, Long.class, id);
    }
}
