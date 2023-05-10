package ru.yandex.practicum.filmorate.dao.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User add(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO USER_MAN(NAME, LOGIN, EMAIL, BIRTHDAY) VALUES(?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            String[] keyColumn = {"ID"};
            PreparedStatement ps = connection.prepareStatement(sql, keyColumn);
            ps.setString(1, user.getName());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getEmail());
            Date date = Date.valueOf(user.getBirthday());
            ps.setDate(4, date);
            return ps;
        }, keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return getById(id);
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE USER_MAN SET NAME=?, LOGIN=?, EMAIL=?, BIRTHDAY=? WHERE ID=?";
        Date date = Date.valueOf(user.getBirthday());
        int isUpdated = jdbcTemplate.update(sql,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                date,
                user.getId()
        );
        if (isUpdated == 0) {
            throw new UserNotFoundException("не найден юзер с id=" + user.getId());
        }
        return getById(user.getId());
    }

    @Override
    public void delete(long id) {
        int sqlResult = jdbcTemplate.update("DELETE FROM USER_MAN WHERE ID=?", id);
        if (sqlResult == 0) {
            throw new UserNotFoundException("не найден юзер с id=" + id);
        } else {
            log.info("удален пользователь с id=" + id);
        }
    }

    @Override
    public User getById(long id) {
        String query = "SELECT * FROM USER_MAN WHERE ID=?";
        try {
            return jdbcTemplate.queryForObject(query, (rs, rowNum) -> makeUser(rs), id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("не найден юзер с id=" + id);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM USER_MAN", (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public boolean isContains(long id) {
        String sql = "SELECT ID FROM USER_MAN WHERE ID=?";
        try {
            jdbcTemplate.queryForObject(sql, Integer.class, id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public void addFriend(long userId, long friendId) {
        boolean isFriendship;
        String sqlIsFriend = "SELECT * " +
                "FROM friends " +
                "WHERE id_user=? AND id_friend=?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlIsFriend, friendId, userId);
        isFriendship = sqlRowSet.next();
        String sqlAddFriend = "INSERT INTO friends (id_user, id_friend, friendship_status) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlAddFriend, userId, friendId, isFriendship);
        if (isFriendship) {
            String sqlStatus = "UPDATE friends SET friendship_status = true " +
                    "WHERE id_user=? AND id_friend=?";
            jdbcTemplate.update(sqlStatus, friendId, userId);
        }
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        String sql = "DELETE FROM FRIENDS WHERE ID_USER=? AND ID_FRIEND=?";
        jdbcTemplate.update(sql, userId, friendId);
        String sqlStatus = "UPDATE friends SET friendship_status=false " +
                "WHERE id_user=? AND id_friend=?";
        jdbcTemplate.update(sqlStatus, friendId, userId);
    }

    @Override
    public List<Long> getFriends(long id) {
        String sql = "SELECT ID_FRIEND FROM FRIENDS WHERE ID_USER=?";
        return jdbcTemplate.queryForList(sql, Long.class, id);
    }

    private User makeUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setLogin(resultSet.getString("login"));
        user.setEmail(resultSet.getString("email"));
        Date date = resultSet.getDate("birthday");
        user.setBirthday(date.toLocalDate());
        return user;
    }
}
