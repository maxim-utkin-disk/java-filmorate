package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmGenreTest {
    private final FilmGenreStorage filmGenreStorage;

    @Test
    public void testFindFilmGenreById() {
        FilmGenre fg = filmGenreStorage.getFilmGenreById(1);
        assertThat(fg).isNotNull();
        assertThat(fg.getId()).isEqualTo(1);
        assertThat(fg.getName()).isEqualTo("Комедия");
    }

    @Test
    public void testFindAllFilmGenres() {
        ArrayList<FilmGenre> allFGList = filmGenreStorage.getFilmGenreList();
        assertThat(allFGList).hasSize(6);
        assertThat(allFGList.get(0).getName()).isEqualTo("Комедия");
        assertThat(allFGList.get(1).getName()).isEqualTo("Драма");
    }

    @Test
    public void testFindNotExistsFilmRatingById() {
        assertThrows(NotFoundException.class, () -> filmGenreStorage.getFilmGenreById(11));
    }

}
