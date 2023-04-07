package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.util.DurationAdapterUtil;
import ru.yandex.practicum.filmorate.util.LocalDateTypeAdapterUtil;
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
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapterUtil())
                .registerTypeAdapter(Duration.class, new DurationAdapterUtil())
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
        User filmReturn = userController.add(defaultUser);
        assertEquals(defaultUser, filmReturn);
    }

    @Test
    void shouldUpdateUser() throws ValidationException {
        long id = userController.add(defaultUser).getId();
        defaultUser.setId(id);
        defaultUser.setName("nameNameName");
        defaultUser.setLogin("loginLoginLogin");
        defaultUser.setEmail("mailMailMail@gmail.com");
        defaultUser.setBirthday(LocalDate.of(2012, 12, 12));
        User userReturn = userController.update(defaultUser);
        assertEquals(defaultUser, userReturn);
    }

    @Test
    void shouldUpdateUserWithWrongId() throws ValidationException {
        long id = userController.add(defaultUser).getId() + 999;
        defaultUser.setId(id);
        defaultUser.setName("nameNameName");
        defaultUser.setLogin("loginLoginLogin");
        defaultUser.setEmail("mailMailMail@gmail.com");
        defaultUser.setBirthday(LocalDate.of(2012, 12, 12));
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userController.update(defaultUser)
        );
        assertEquals("Пользователь с Id '" + id + "' не найден в сервисе", exception.getMessage());
    }
}







