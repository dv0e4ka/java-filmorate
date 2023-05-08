package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.user.UserDao;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {


    // TODO перенести проверки isContain сюда!


    private UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User add(User user) {
        return userDao.add(user);
    }

    public User update(User user) {
        return userDao.update(user);
    }

    public void delete(long id) {
        userDao.delete(id);
    }

    public User getById(long id) {
        return userDao.getById(id);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public void addFriend(long userId, long friendId) {
        User user = userDao.getById(userId);
        User friend = userDao.getById(friendId);

        if (user == null) {
            throw new UserNotFoundException("Пользователь с Id '" + userId + "' не найден");
        } else if (friend == null) {
            throw new UserNotFoundException("Пользователь с Id '" + friendId + "' не найден");
        } else {
            user.addFriend(friendId);
            friend.addFriend(userId);
            log.info("Пользователь с Id '" + userId + " и пользователь с Id '" + friendId + " теперь друзья!");
        }
    }

    public void deleteFriend(long userId, long friendId) {
        User user = userDao.getById(userId);
        User friend = userDao.getById(friendId);

        if (user == null) {
            throw new UserNotFoundException("Пользователь с Id '" + userId + "' не найден");
        } else if (friend == null) {
            throw new UserNotFoundException("Пользователь с Id '" + friendId + "' не найден");
        } else {
            user.deleteFriend(friendId);
            friend.deleteFriend(userId);
            log.info("Пользователь с Id '" + userId + " и пользователь с Id '" + friendId + " больше не друзья!");
        }
    }

    public List getUserFriends(long id) {
        if (!userDao.isContains(id)) {
            throw new UserNotFoundException("Пользователь с Id '" + id + "' не найден в сервисе");
        } else {
            return userDao.getById(id).getFriends().stream()
                    .map(userId -> userDao.getById(userId)).collect(Collectors.toList());
        }
    }

    public List getCommonFriends(long id, long otherId) {
        if (userDao.isContains(id) && userDao.isContains(otherId)) {
            return userDao.getById(id).getFriends().stream()
                    .filter(userDao.getById(otherId).getFriends()::contains)
                    .map(userId -> userDao.getById(userId))
                    .collect(Collectors.toList());
        } else {
            throw new UserNotFoundException("У пользователей с id " + id + " и " + otherId + " нет общих друзей");
        }
    }
}
