package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmRating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmRatingRowMapper implements RowMapper<FilmRating> {
    @Override
    public FilmRating mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        FilmRating filmRating = new FilmRating();
        filmRating.setRatingId(resultSet.getInt("rating_id"));
        filmRating.setRatingName(resultSet.getString("rating_name"));

        return filmRating;
    }
}