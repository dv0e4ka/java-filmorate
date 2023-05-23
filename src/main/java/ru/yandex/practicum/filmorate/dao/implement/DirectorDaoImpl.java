package ru.yandex.practicum.filmorate.dao.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class DirectorDaoImpl implements DirectorDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DirectorDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Director add(Director director) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String directorSql = "INSERT INTO DIRECTOR (name) " +
                "VALUES(?)";
        jdbcTemplate.update(connection -> {
            String[] keyColumn = {"id"};
            PreparedStatement ps = connection.prepareStatement(directorSql, keyColumn);
            ps.setString(1, director.getName());
            return ps;
        }, keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return getById(id);
    }

    @Override
    public List<Director> addDirectorFilm(long id, List<Director> directors) {
        String sql = "INSERT INTO DIRECTOR_FILM(ID_FILM, ID_DIRECTOR) " +
                "VALUES (?, ?)";
        try {
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, id);
                    ps.setInt(2, directors.get(i).getId());
                }

                @Override
                public int getBatchSize() {
                    return directors.size();
                }
            });
        } catch (DataAccessException e) {
            log.info("дублирование режиссера у фильма id={}", id);
        }
        return getAllDirectorsByFilm(id);
    }

    @Override
    public List<Director> updateDirectorFilm(long id, List<Director> directors) {
        deleteDirectorFilm(id);
        addDirectorFilm(id, directors);
        return getAllDirectorsByFilm(id);
    }

    @Override
    public void deleteDirectorFilm(long id) {
        String sql = "DELETE FROM DIRECTOR_FILM WHERE ID_FILM=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Director update(Director director) {
        String directorSql = "UPDATE DIRECTOR SET name=? WHERE id=?";
        int sqlResult = jdbcTemplate.update(directorSql,
                director.getName(),
                director.getId()
        );
        if (sqlResult == 0) {
            throw new DirectorNotFoundException("не найден режиссер для обновления с id=" + director.getId());
        } else {
            log.info("обновлен режиссер с id=" + director.getId());
        }
        return getById(director.getId());
    }

    @Override
    public void delete(int id) {
        int sqlResult = jdbcTemplate.update("DELETE FROM DIRECTOR WHERE id=?", id);
        if (sqlResult == 0) {
            throw new DirectorNotFoundException("не найден режиссер для удаления с id=" + id);
        } else {
            log.info("удален режиссер с id=" + id);
        }
    }

    @Override
    public List<Director> getAllDirectors() {
        String sql = "SELECT ID, NAME FROM DIRECTOR";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeDirector(rs));
    }

    @Override
    public List<Director> getAllDirectorsByFilm(long id) {
        String sql = "SELECT D.ID," +
                "D.NAME " +
                "FROM DIRECTOR_FILM AS DF " +
                "LEFT OUTER JOIN DIRECTOR AS D ON DF.ID_DIRECTOR=D.ID " +
                "WHERE ID_FILM=?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeDirector(rs), id);
    }

    @Override
    public Director getById(int id) {
        String sql = "SELECT ID, NAME FROM DIRECTOR WHERE ID=?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeDirector(rs), id);
        } catch (DataAccessException e) {
            throw new DirectorNotFoundException("не найден режиссер id=" + id);
        }
    }

    public boolean isDirectorExists(long id) {
        String sqlQuery = "SELECT id " +
                "FROM director WHERE id = ?;";
        boolean exists = jdbcTemplate.query(sqlQuery, new Object[]{id}, (ResultSet rs) -> {
            return rs.next();
        });
        return exists;
    }

    private Director makeDirector(ResultSet rs) throws SQLException {
        Director director = new Director();
        director.setId(rs.getInt("ID"));
        director.setName(rs.getString("NAME"));
        return director;
    }
}
