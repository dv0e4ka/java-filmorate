package ru.yandex.practicum.filmorate.dao;

import java.util.List;

public interface FeedDao {
    void addFeed();

    List getFeed(int id);
}
