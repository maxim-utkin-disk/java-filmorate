package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.*;

@Repository
@Qualifier("dbFilmGenreStorage")
public class FilmGenreRepository extends BaseRepository<FilmGenre> {
    private static final String FIND_ALL_QUERY = "select genre_id, genre_name from genres";
    private static final String FIND_BY_ID_QUERY = "select genre_id, genre_name from genres where genre_id = ?";
    private static final String GET_GENRE_LIST_BY_FILM_QUERY = "select g.genre_id, g.genre_name from films_genres fg, genres g where fg.film_id = ? and fg.genre_id = g.genre_id";

    public FilmGenreRepository(JdbcTemplate jdbc, RowMapper<FilmGenre> mapper) {
        super(jdbc, mapper);
    }

    public List<FilmGenre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<FilmGenre> findById(Integer genreId) {
        return findOne(FIND_BY_ID_QUERY, genreId);
    }

    public List<FilmGenre> getGenresByFilm(Integer filmId) {
        return findMany(GET_GENRE_LIST_BY_FILM_QUERY, filmId);
    }

}


