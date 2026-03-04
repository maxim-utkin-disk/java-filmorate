package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Component("DbFilmStorage")
@Qualifier("dbFilmStorage")
public class DbFilmStorage extends FilmRepository implements FilmStorage {

    public DbFilmStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Film addNewFilm(Film newFilm) {
        log.debug("Создание фильма film_name = {}", newFilm.getName());
        if (newFilm.getName() == null
                || newFilm.getName().isBlank()
                || newFilm.getName().isEmpty()) {
            log.error("Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (newFilm.getDescription().length() > 200) {
            log.error("Максимальная длина описания - 200 символов");
            throw new ValidationException("Максимальная длина описания - 200 символов");
        }

        if (newFilm.getReleaseDate() == null
                || newFilm.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.error("Дата релиза должна быть не раньше 28 декабря 1895г (передано {})", newFilm.getReleaseDate());
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895г (передано " +
                    newFilm.getReleaseDate() + ")");
        }

        if (newFilm.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть положительным числом");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }

        return create(newFilm);
    }

    @Override
    public Film updateExistingFilm(Film updFilm) {
        log.debug("Редактирование фильма film_id = {}", updFilm.getId());
        if (updFilm.getName() == null
                || updFilm.getName().isBlank()
                || updFilm.getName().isEmpty()) {
            log.error("Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (updFilm.getDescription().length() > 200) {
            log.error("Максимальная длина описания - 200 символов");
            throw new ValidationException("Максимальная длина описания - 200 символов");
        }

        if (updFilm.getReleaseDate() == null
                || updFilm.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.error("Дата релиза должна быть не раньше 28 декабря 1895г (передано {})", updFilm.getReleaseDate());
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895г (передано " +
                    updFilm.getReleaseDate() + ")");
        }

        if (updFilm.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть положительным числом");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }

        return update(updFilm);
    }

    @Override
    public Film getFilmById(int filmId) {
        log.debug("Запрос из БД фильмов по id={}", filmId);
        Optional<Film> ofr = findById(filmId);
        if (ofr.isPresent()) {
            return ofr.get();
        } else {
            log.error("В текущих данных БД не найден фильм с id = {}", filmId);
            throw new NotFoundException("В текущих данных БД не найден фильм с id = " + filmId);
        }
    }

    @Override
    public ArrayList<Film> getFilmsList() {
        log.debug("Запрос из БД всего списка фильмов");
        return (ArrayList<Film>) findAll();
    }

    @Override
    public void addLikeToFilm(User u, Film f) {
        log.debug("Пользователь user_id={} ставит лайк фильму film_id={}", u.getId(), f.getId());
        super.addLike(u, f);
    }

    @Override
    public void removeLikeFromFilm(User u, Film f) {
        log.debug("Пользователь user_id={} убирает ранее поставленный лайк фильму film_id={}", u.getId(), f.getId());
        super.removeLike(u, f);
    }

    public List<Film> getTopPopularFilms(Integer count) {
        log.debug("Запрос TOP-{} популярных фильмов", count);
        return super.getTopPopularFilms(count);
    }

}
