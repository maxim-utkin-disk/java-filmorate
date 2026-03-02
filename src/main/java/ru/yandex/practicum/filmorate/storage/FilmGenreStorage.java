package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmGenre;
import java.util.ArrayList;
import java.util.List;

public interface FilmGenreStorage {
    ArrayList<FilmGenre> getFilmGenreList();

    FilmGenre getFilmGenreById(Integer id);

    List<FilmGenre> getGenresByFilm(Integer filmId);
}
