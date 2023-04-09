package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User add(User user);

    User update(User user);

    void delete(long id);

    User getById(long id);

    List<User> getAllUsers();

    boolean isContains(long id);
}

