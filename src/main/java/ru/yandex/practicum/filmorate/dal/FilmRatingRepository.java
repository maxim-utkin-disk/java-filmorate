package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmRating;

import java.util.List;
import java.util.Optional;

@Repository
public class FilmRatingRepository extends BaseRepository<FilmRating> {
    private static final String FIND_ALL_QUERY = "select rating_id, rating_name from mpa_film_ratings";
    private static final String FIND_BY_ID_QUERY = "select rating_id, rating_name from mpa_film_ratings where rating_id = ?";

    public FilmRatingRepository(JdbcTemplate jdbc, RowMapper<FilmRating> mapper) {
        super(jdbc, mapper);
    }

    public List<FilmRating> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<FilmRating> findById(Integer ratingId) {
        return findOne(FIND_BY_ID_QUERY, ratingId);
    }

}


