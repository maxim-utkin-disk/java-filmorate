package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

public interface FilmStorage {

    Film addNewFilm(Film newFilm);

    Film updateExistingFilm(Film updFilm);

    Film getFilmById(int filmId);

    ArrayList<Film> getFilmsList();

    List<Film> getTopPopularFilms(Integer count);

    void addLikeToFilm(User user, Film film);

    void removeLikeFromFilm(User user, Film film);

}
