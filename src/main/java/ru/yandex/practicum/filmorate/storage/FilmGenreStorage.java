package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmGenre;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface FilmGenreStorage {
    ArrayList<FilmGenre> getFilmGenreList();

    FilmGenre getFilmGenreById(Integer id);

    List<FilmGenre> getGenresByFilm(Integer filmId);
}
