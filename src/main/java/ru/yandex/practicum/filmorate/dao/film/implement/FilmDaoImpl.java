package ru.yandex.practicum.filmorate.dao.film.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.film.FilmDao;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.SqlQueryException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
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
        String filmSql = "INSERT INTO FILM (name, description, releaseDate, duration, mpa) VALUES(?, ?, ?, ?, ?)";
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

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return getById(id);
    }
    @Override
    public Film update(Film film) {
        String filmSql = "UPDATE FILM SET name=?, description=?, releaseDate=?, duration=?, mpa=? WHERE id=?";
        Date date = Date.valueOf(film.getReleaseDate());
        int sqlResult = jdbcTemplate.update(filmSql
                , film.getName()
                , film.getDescription()
                , date
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId()
        );
        if (sqlResult == 0) {
            throw new FilmNotFoundException("не найден фильм для обновления с id=" + film.getId());
        } else {
            log.info("обновлен фильм с id=" + film.getId());
        }
        return getById(film.getId());
    }

    @Override
    public void delete(long id) {
        int sqlResult = jdbcTemplate.update("DELETE FROM FILM WHERE id=?", id);
        if (sqlResult == 0) {
            throw new FilmNotFoundException("не найден фильм для удаления с id=" + id);
        } else {
            log.info("удален фильм с id=" + id);
        }
    }

    @Override
    public Film getById(long id) {
        String sqlFilm = "SELECT f.*, " +
                "m.name AS mpa_name " +
                "FROM FILM AS f " +
                "INNER JOIN RATING_MPA AS m ON f.MPA=m.ID " +
                "WHERE f.ID=?";
        try {
            return jdbcTemplate.queryForObject(sqlFilm, (rs, rowNum) -> makeFilm(rs), id);
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException("не найден фильм с id=" + id);
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT f.*, " +
                "m.name AS mpa_name " +
                "FROM FILM AS f " +
                "INNER JOIN RATING_MPA AS m ON f.mpa=m.ID";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public boolean isContains(long id) {
        String sql = "SELECT ID " +
                "FROM FILM " +
                "WHERE ID=?";
        try {
            jdbcTemplate.queryForObject(sql, Integer.class, id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private Film makeFilm(ResultSet resultSet) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getInt("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setDuration(resultSet.getInt("duration"));

        Date release = resultSet.getDate("releaseDate");
        film.setReleaseDate(release.toLocalDate());
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getInt("mpa"));
        mpa.setName(resultSet.getString("mpa_name"));
        film.setMpa(mpa);
        return film;
    }
}
