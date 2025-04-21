package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film getFilmById(int filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public ArrayList<Film> getFilmsList() {
        return filmStorage.getFilmsList();
    }

    Comparator<Film> compareFilmsByLikesCount = Comparator.comparing(f -> f.getLikesList().size());

    public List<Film> getTopPopularFilms(Integer count) {
        log.debug("Вызван метод getTopPopularFilms, count={}", count);
        ArrayList<Film> allFilmsList = this.getFilmsList();
        return allFilmsList
                .stream()
                .sorted(compareFilmsByLikesCount.reversed())
                .limit(Math.min((count == null ? 10 : count), allFilmsList.size()) - 1)
                .collect(Collectors.toList());
    }

    public Film addNewFilm(Film film) {
        return filmStorage.addNewFilm(film);
    }

    public Film updateExistingFilm(Film film) {
        return filmStorage.updateExistingFilm(film);
    }

    public void addLike(int filmId, int userId) {
        log.debug("Для фильма id={} добавляем лайк от юзера id={}", filmId, userId);
        filmStorage.getFilmById(filmId).addLike(userStorage.getUserById(userId));
    }

    public void removeLike(int filmId, int userId) {
        log.debug("У фильма id={} удаляем лайк от юзера id={}", filmId, userId);
        filmStorage.getFilmById(filmId).removeLike(userStorage.getUserById(userId));
    }

}
