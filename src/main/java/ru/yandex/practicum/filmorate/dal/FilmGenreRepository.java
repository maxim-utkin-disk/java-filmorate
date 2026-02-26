package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;
import java.util.Optional;

@Repository
public class FilmGenreRepository extends BaseRepository<FilmGenre> {
    private static final String FIND_ALL_QUERY = "select genre_id, genre_name from genres";
    private static final String FIND_BY_ID_QUERY = "select genre_id, genre_name from genres where genre_id = ?";

    public FilmGenreRepository(JdbcTemplate jdbc, RowMapper<FilmGenre> mapper) {
        super(jdbc, mapper);
    }

    public List<FilmGenre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<FilmGenre> findById(Integer genreId) {
        return findOne(FIND_BY_ID_QUERY, genreId);
    }

}


