package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface FeedDao {
    void addFeed();

    List<Event> getFeed(long id);
}
