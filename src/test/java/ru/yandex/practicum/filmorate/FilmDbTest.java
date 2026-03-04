package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.DbFilmStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
@Import({DbFilmStorage.class})
public class FilmDbTest {

    private final FilmStorage filmStorage;
    private Film film4Test;

    @Autowired
    public FilmDbTest(@Qualifier("dbFilmStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @BeforeEach
    public void prepareData4Test() {
        film4Test = new Film(null,
                "film name",
                "film description",
                LocalDate.of(1970,1,1),
                100,
                1,
                "G");
        FilmGenre fg1 = new FilmGenre();
        fg1.setId(1);
        fg1.setName("Комедия");
        FilmGenre fg3 = new FilmGenre();
        fg3.setId(3);
        fg3.setName("Мультфильм");
        film4Test.setGenres(List.of(fg1, fg3));
    }

    @Test
    public void testCreateFilmAndGetById() {
        Film createdFilm = filmStorage.addNewFilm(film4Test);
        Film foundFilm = filmStorage.getFilmById(createdFilm.getId());

        assertThat(foundFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("genres", "likesUser")
                .isEqualTo(createdFilm);
    }

    @Test
    public void testGetFilmsList() {
        Film film1 = filmStorage.addNewFilm(film4Test);
        Film film2 = filmStorage.addNewFilm(
               new Film(null,
               "film name 2",
               "film description 2",
               LocalDate.of(1970,2,2),
               102,
               1,
               "G")
       );
        Film film3 = filmStorage.addNewFilm(
                new Film(null,
                        "film name 3",
                        "film description 3",
                        LocalDate.of(1970,3,3),
                        103,
                        1,
                        "G")
        );

        List<Film> fl = filmStorage.getFilmsList();
        assertThat(fl).hasSize(3);
    }


    @Test
    public void updateExistingFilm() {
        Film createdFilm = filmStorage.addNewFilm(film4Test);
        Film updatedFilm = new Film(createdFilm.getId(),
                "upd film name",
                "upd film description",
                LocalDate.of(1970,2,2),
                102,
                2,
                "PG"
        );
        Film updResult = filmStorage.updateExistingFilm(updatedFilm);

        assertThat(updResult)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updatedFilm);
    }


}
