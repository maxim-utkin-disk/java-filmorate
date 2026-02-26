package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmGenre;
import java.util.ArrayList;

public interface FilmGenreStorage {
    ArrayList<FilmGenre> getFilmGenreList();

    FilmGenre getFilmGenreById(Integer id);
}
