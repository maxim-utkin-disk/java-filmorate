package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmRating;
import java.util.ArrayList;

public interface FilmRatingStorage {
    ArrayList<FilmRating> getFilmRatingList();

    FilmRating getFilmRatingById(Integer id);

}
