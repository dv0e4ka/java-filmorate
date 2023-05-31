package ru.yandex.practicum.filmorate.dao.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FeedDao;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
public class FeedDaoImpl implements FeedDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FeedDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addEvent(Event event) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String eventSql = "INSERT INTO feed (user_id, event_type, operation_type, entity_id, timestamp) " +
                "VALUES(?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            String[] keyColumn = {"id"};
            PreparedStatement ps = connection.prepareStatement(eventSql, keyColumn);
            ps.setInt(1, (int) event.getUserId());
            ps.setString(2, event.getEventType().toString());
            ps.setString(3, event.getOperationType().toString());
            ps.setInt(4, (int) event.getEntityId());
            ps.setLong(5, event.getTimestamp());
            return ps;
        }, keyHolder);

//        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
//        return getEvent(id);
    }

    @Override
    public Event getEvent(long eventId) {
        String sql = "SELECT * FROM feed WHERE id = ?";
        Event event = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFeed(rs), eventId);
        //Можно сделать в одну строчку, но, на будущее решил оставить так
        return event;
    }

    @Override
    public List<Event> getEvents(long userId) {
        String sql = "SELECT * FROM feed WHERE user_id = ?";
        List<Event> eventList = jdbcTemplate.query(sql, (rs, rowNum) -> makeFeed(rs), userId);
        //Можно сделать в одну строчку, но, на будущее решил оставить так
        return eventList;
    }

    @Override
    public Event makeFeed(ResultSet resultSet) throws SQLException {
        Event event = new Event();
        event.setEventId(resultSet.getInt("id"));
        event.setEntityId(resultSet.getInt("user_id"));
        event.setEventType(EventType.valueOf(resultSet.getString("event_type")));
        event.setOperationType(OperationType.valueOf(resultSet.getString("operation_type")));
        event.setEntityId(resultSet.getInt("entity_id"));
        event.setTimestamp(resultSet.getLong("timestamp"));
        return event;
    }
}
