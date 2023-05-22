package ru.yandex.practicum.filmorate.dao.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpa(int id) {
        String sql = "SELECT ID, NAME FROM rating_mpa  WHERE id=?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeMpa(rs), id);
        } catch (EmptyResultDataAccessException e) {
            throw new MpaNotFoundException("не найден рейтинг MPA id=" + id);
        }
    }

    @Override
    public List<Mpa> getMpaAll() {
        String sql = "SELECT ID, NAME FROM rating_mpa ORDER BY id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("id"));
        mpa.setName(rs.getString("name"));
        return mpa;
    }
}