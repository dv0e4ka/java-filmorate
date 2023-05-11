package ru.yandex.practicum.filmorate.dao;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDaoImplTest {
    private final FilmDao filmDao;

    @Test
    public void FilmShouldBeAdded() {
        Film film = filmCreate();
        assertThat(filmDao.add(film)).hasFieldOrPropertyWithValue("id", 1L);
    }

    private Film filmCreate() {
        return Film.builder()
                .name("Terminator")
                .description("wow")
                .releaseDate(LocalDate.of(1984, 10, 26))
                .duration(107)
                .mpa(new Mpa(1))
                .genres(new ArrayList<>())
                .likes(new ArrayList<>())
                .build();
    }
}