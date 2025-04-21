package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

public interface FilmStorage   {
    Film addNewFilm(Film newFilm);
    Film updateExistingFilm (Film updFilm);
    Film getFilmById(int filmId);
    ArrayList<Film> getFilmsList();
}
