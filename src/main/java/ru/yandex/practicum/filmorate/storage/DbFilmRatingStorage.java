package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FilmRatingRepository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.FilmRating;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Repository
public class DbFilmRatingStorage extends FilmRatingRepository implements FilmRatingStorage {

    public DbFilmRatingStorage(JdbcTemplate jdbc, RowMapper<FilmRating> mapper) {
        super(jdbc, mapper);
    }

    public ArrayList<FilmRating> getFilmRatingList() {
        log.debug("Запрос из БД всего списка рейтингов фильмов");
        return (ArrayList<FilmRating>) findAll();
    }

    public FilmRating getFilmRatingById(Integer filmRatingId) {
        log.debug("Запрос из БД рейтинга фильма по id={}", filmRatingId);
        Optional<FilmRating> ofr = findById(filmRatingId);
        if (ofr.isPresent()) {
            return ofr.get();
        } else {
            log.error("В текущих данных БД не найден рейтинг с id = {}", filmRatingId);
            throw new NotFoundException("В текущих данных БД не найден рейтинг с id = " + filmRatingId);
        }
    }

}
