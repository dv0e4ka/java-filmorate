package ru.yandex.practicum.filmorate.dao.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addGenre(long id, List<Genre> genres) {
        for (Genre genre : genres) {
            String sql = "INSERT INTO GENRE_FILM (ID_FILM, ID_GENRE) " +
                    "VALUES (?, ?)";
            try {
                jdbcTemplate.update(sql, id, genre.getId());
            } catch (Exception e) {
                log.info("дублирование жанра {}, у фильма id={}", genre.getId(), id);
            }
        }
    }

    @Override
    public void updateGenre(long id, List<Genre> genres) {
        deleteGenre(id);
        addGenre(id, genres);
    }

    @Override
    public void deleteGenre(long id) {
        String sql = "DELETE FROM GENRE_FILM WHERE ID_FILM=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Genre> getALlGenreByFilm(long id) {
        String sql = "SELECT GT.ID," +
                "GT.NAME " +
                "FROM GENRE_FILM AS GL " +
                "LEFT OUTER JOIN GENRE_TYPES AS GT ON GL.ID_GENRE=GT.ID " +
                "WHERE ID_FILM=?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), id);
    }

    @Override
    public Genre getGenre(int id) {
        String sql = "SELECT * FROM GENRE_TYPES WHERE ID=?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeGenre(rs), id);
        } catch (DataAccessException e) {
            throw new GenreNotFoundException("не найден жанр id=" + id);
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql = "SELECT * FROM GENRE_TYPES ORDER BY ID";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("ID"));
        genre.setName(rs.getString("NAME"));
        return genre;
    }
}
