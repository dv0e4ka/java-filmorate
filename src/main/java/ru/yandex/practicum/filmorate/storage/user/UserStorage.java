package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User add(User user);

    User update(User user);

    void delete(long id);

    User getById(long id);

    List<User> getAllUsers();

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    List getUserFriends(long id);

    List getCommonFriends(long id, long otherId);
}

