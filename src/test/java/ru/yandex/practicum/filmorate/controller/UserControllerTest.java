package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.config.DurationAdapter;
import ru.yandex.practicum.filmorate.config.LocalDateTypeAdapter;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {
    @Autowired
    UserController userController;
    private static Gson gson;
    private User sa;
    private User defaultUser;

    @BeforeAll
    static void setUpGson() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    @BeforeEach
    void setUpNewUser() {
        defaultUser = User.builder()
                .email("name@gmail.com")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
    }

    @Test
    void shouldAddUser() throws ValidationException {
        User filmReturn = userController.addUser(defaultUser);
        assertEquals(defaultUser, filmReturn);
    }

    @Test
    void shouldSetEmptyMail() {
        defaultUser.setEmail("  ");
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.addUser(defaultUser)
        );
        assertEquals("неккоректно введена почта пользователя", exception.getMessage());
    }

    @Test
    void shouldSetMailWithoutMainSymbol() {
        defaultUser.setEmail("name0gmail.com");
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.addUser(defaultUser)
        );
        assertEquals("неккоректно введена почта пользователя", exception.getMessage());
    }

    @Test
    void shouldBlankLogin() {
        defaultUser.setLogin(" ");
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.addUser(defaultUser)
        );
        assertEquals("неккоректно введен логин пользователя", exception.getMessage());
    }

    @Test
    void shouldSpaceInLogin() {
        defaultUser.setLogin(" ");
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.addUser(defaultUser)
        );
        assertEquals("неккоректно введен логин пользователя", exception.getMessage());
    }

    @Test
    void shouldBirthdayInFuture() {
        defaultUser.setBirthday(LocalDate.of(3333, 1, 1));
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.addUser(defaultUser)
        );
        assertEquals("указана дата день рождения пользователя из будущего времени", exception.getMessage());
    }

    @Test
    void shouldUpdateUser() throws ValidationException {
        int id = userController.addUser(defaultUser).getId();
        defaultUser.setId(id);
        defaultUser.setName("nameNameName");
        defaultUser.setLogin("loginLoginLogin");
        defaultUser.setEmail("mailMailMail@gmail.com");
        defaultUser.setBirthday(LocalDate.of(2012, 12, 12));
        User userReturn = userController.updateUser(defaultUser);
        assertEquals(defaultUser, userReturn);
    }

    @Test
    void shouldUpdateUserWithWrongId() throws ValidationException {
        int id = userController.addUser(defaultUser).getId() + 999;
        defaultUser.setId(id);
        defaultUser.setName("nameNameName");
        defaultUser.setLogin("loginLoginLogin");
        defaultUser.setEmail("mailMailMail@gmail.com");
        defaultUser.setBirthday(LocalDate.of(2012, 12, 12));
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.updateUser(defaultUser)
        );
        assertEquals("Пользователь с Id '" + id + "' не найден в сервисе", exception.getMessage());
    }
}







