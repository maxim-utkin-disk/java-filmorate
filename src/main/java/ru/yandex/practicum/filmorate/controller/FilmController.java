package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.FRUtils;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> filmsList = new HashMap<>();

    private Integer getFilmId() {
        return FRUtils.getNextIdForObjectsList(filmsList.keySet());
    }

    @GetMapping
    private ArrayList<Film> getFilmsList() {
        log.info("Запрос списка фильмов");
        return new ArrayList<>(filmsList.values());
    }

    @PostMapping
    private Film postFilm(@Valid @RequestBody Film film) {
        log.info("Запись нового фильма: {}", film.getName());
        film.setId(getFilmId());

        if (film.getName() == null
                || film.getName().isBlank()
                || film.getName().isEmpty()) {
            log.error("Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getDescription().length() > 200) {
            log.error("Максимальная длина описания - 200 символов");
            throw new ValidationException("Максимальная длина описания - 200 символов");
        }

        if (film.getReleaseDate() == null
                || film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.error("Дата релиза должна быть не раньше 28 декабря 1895г (передано {}}", film.getReleaseDate());
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895г (передано " +
                    film.getReleaseDate() + ")");
        }

        if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть положительным числом");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }

        filmsList.put(film.getId(), film);
        return filmsList.get(film.getId());
    }

    @PutMapping
    private Film putFilm(@Valid @RequestBody Film film) {
        log.info("Обновление фильма: {}", film.getName());
        if (film.getId() == null) {
            log.error("Не указан id обновляемого фильма");
            throw new ValidationException("Не указан id обновляемого фильма");
        }

        if (!filmsList.containsKey(film.getId())) {
            log.error("Попытка обновить данные о несуществующем фильме id = {}", film.getId());
            throw new ValidationException("Попытка обновить данные о несуществующем фильме id = " + film.getId());
        }
        filmsList.replace(film.getId(), film);
        return filmsList.get(film.getId());
    }
}
