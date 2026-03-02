package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FilmGenreRepository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class DbFilmGenreStorage extends FilmGenreRepository implements FilmGenreStorage {

    public DbFilmGenreStorage(JdbcTemplate jdbc, RowMapper<FilmGenre> mapper) {
        super(jdbc, mapper);
    }

    public ArrayList<FilmGenre> getFilmGenreList() {
        log.debug("Запрос из БД всего списка жанров фильмов");
        return (ArrayList<FilmGenre>) findAll();
    }

    public FilmGenre getFilmGenreById(Integer filmGenreId) {
        log.debug("Запрос из БД жанра фильма по id = {}", filmGenreId);
        Optional<FilmGenre> ofg = findById(filmGenreId);
        if (ofg.isPresent()) {
            return ofg.get();
        } else {
            log.error("В текущих данных БД не найден жанр с id = {}", filmGenreId);
            throw new NotFoundException("В текущих данных БД не найден жанр с id = " + filmGenreId);
        }
    }

    public List<FilmGenre> getGenresByFilm(Integer filmId) {
        return super.getGenresByFilm(filmId);
    }

}
