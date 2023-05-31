package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface FeedDao {
    void addEvent(Event event);

    Event getEvent(long id);

    List<Event> getEvents(long id);

    Event makeFeed(ResultSet rs) throws SQLException;
}
