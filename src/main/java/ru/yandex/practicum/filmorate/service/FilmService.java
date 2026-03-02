package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.FilmRatingStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final FilmRatingStorage filmRatingStorage;

    @Autowired
    public FilmService(@Qualifier("dbFilmStorage")FilmStorage filmStorage,
                       @Qualifier("dbUserStorage")UserStorage userStorage,
                       @Qualifier("dbFilmGenreStorage")FilmGenreStorage filmGenreStorage,
                       @Qualifier("dbFilmRatingStorage")FilmRatingStorage filmRatingStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmGenreStorage = filmGenreStorage;
        this.filmRatingStorage = filmRatingStorage;
    }

    public Film getFilmById(int filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public ArrayList<Film> getFilmsList() {
        return filmStorage.getFilmsList();
    }

    private Comparator<Film> compareFilmsByLikesCount = Comparator.comparing(f -> f.getLikesList().size());

    public List<Film> getTopPopularFilms(Integer count) {
        log.debug("Вызван метод getTopPopularFilms, count={}", count);
        if (count <= 0) {
            throw new ValidationException("Запрос TOP популярных фильмов в количестве " + count + " штук не имеет смысла!");
        }
        return filmStorage.getTopPopularFilms(count);
    }

    public Film addNewFilm(Film film) {
        FilmRating fr = filmRatingStorage.getFilmRatingById(film.getMpa().getId());
        List<FilmGenre> lfg = filmGenreStorage.getFilmGenreList();
        /*if ()*/
        return filmStorage.addNewFilm(film);
    }

    public Film updateExistingFilm(Film film) {
        return filmStorage.updateExistingFilm(film);
    }

    public void addLike(int filmId, int userId) {
        log.debug("Для фильма id={} добавляем лайк от юзера id={}", filmId, userId);
        User u = userStorage.getUserById(userId);
        Film f = filmStorage.getFilmById(filmId);
        filmStorage.addLikeToFilm(u, f);
    }

    public void removeLike(int filmId, int userId) {
        log.debug("У фильма id={} удаляем лайк от юзера id={}", filmId, userId);
        User u = userStorage.getUserById(userId);
        Film f = filmStorage.getFilmById(filmId);
        filmStorage.removeLikeFromFilm(u, f);
    }

}
