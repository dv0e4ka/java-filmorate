package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDao {

    User add(User user);

    User update(User user);

    void delete(long id);

    User getById(long id);

    List<User> getAllUsers();

    boolean isContains(long id);

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    List<Long> getFriends(long id);
}

