package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.UserDao;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserDao userDao;
    private final FeedService feedService;
    private final FilmService filmService;

    @Autowired
    public UserService(UserDao userDao, FeedService feedService, FilmService filmService) {
        this.userDao = userDao;
        this.feedService = feedService;
        this.filmService = filmService;
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
            userDao.addFriend(userId, friendId);
            feedService.addFriendEvent(userId, friendId);
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
            userDao.deleteFriend(userId, friendId);
            feedService.deleteFriendEvent(userId, friendId);
            log.info("Пользователь с Id '" + userId + " и пользователь с Id '" + friendId + " больше не друзья!");
        }
    }

    public List<User> getUserFriends(long id) {
        if (!userDao.isContains(id)) {
            throw new UserNotFoundException("Пользователь с Id '" + id + "' не найден в сервисе");
        } else {
            List<Long> friendIds = userDao.getFriends(id);
            return friendIds.stream().map(this::getById).collect(Collectors.toList());
        }
    }

    public List<User> getCommonFriends(long id, long friendId) {
        if (userDao.isContains(id) && userDao.isContains(friendId)) {
            List<Long> userFriends = userDao.getFriends(id);
            List<Long> friendFriends = userDao.getFriends(friendId);
            userFriends.retainAll(friendFriends);
            return userFriends.stream().map(this::getById).collect(Collectors.toList());
        } else {
            throw new UserNotFoundException("Пользователь с Id " + id
                    + " или пользователь с Id " + friendId + " не найден в сервисе");
        }
    }

    public List<Film> getRecommendations(long id) {
        if (!userDao.isContains(id)) {
            throw new UserNotFoundException("Пользователь с Id '" + id + "' не найден в сервисе");
        }
        List<Long> ids = userDao.getRecommendations(id);
        List<Film> films = ids.stream().map(filmId -> filmService.getById(filmId)).collect(Collectors.toList());
        return films;
    }
}
