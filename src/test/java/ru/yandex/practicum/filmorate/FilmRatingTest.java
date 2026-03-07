package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.storage.FilmRatingStorage;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmRatingTest {
    private final FilmRatingStorage filmRatingStorage;

    @Test
    public void testFindFimRatingById() {
        FilmRating fr = filmRatingStorage.getFilmRatingById(1);
        assertThat(fr).isNotNull();
        assertThat(fr.getId()).isEqualTo(1);
        assertThat(fr.getName()).isEqualTo("G");
    }

    @Test
    public void testFindAllFilmRatings() {
        ArrayList<FilmRating> allFRList = filmRatingStorage.getFilmRatingList();
        assertThat(allFRList).hasSize(5);
        assertThat(allFRList.get(0).getName()).isEqualTo("G");
        assertThat(allFRList.get(1).getName()).isEqualTo("PG");
    }

    @Test
    public void testFindNotExistsFilmRatingById() {
        assertThrows(NotFoundException.class, () -> filmRatingStorage.getFilmRatingById(11));
    }
}
