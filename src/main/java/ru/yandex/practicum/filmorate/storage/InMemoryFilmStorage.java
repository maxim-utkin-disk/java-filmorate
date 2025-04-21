package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.FRUtils;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> filmsList = new HashMap<>();

    private Integer getFilmId() {
        return FRUtils.getNextIdForObjectsList(filmsList.keySet());
    }

    @Override
    public Film addNewFilm(Film newFilm) {
        log.debug("Запись нового фильма: {}", newFilm);
        newFilm.setId(getFilmId());

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

        filmsList.put(newFilm.getId(), newFilm);
        return filmsList.get(newFilm.getId());
    }

    @Override
    public Film updateExistingFilm (Film updFilm) {
        log.debug("Обновление фильма: {}", updFilm);
        if (updFilm.getId() == null) {
            log.error("Не указан id обновляемого фильма");
            throw new ValidationException("Не указан id обновляемого фильма");
        }

        if (!filmsList.containsKey(updFilm.getId())) {
            log.error("Попытка обновить данные о несуществующем фильме id = {}", updFilm.getId());
            throw new NotFoundException("Попытка обновить данные о несуществующем фильме id = " + updFilm.getId());
        }
        filmsList.replace(updFilm.getId(), updFilm);
        return filmsList.get(updFilm.getId());
    }

    @Override
    public Film getFilmById(int filmId) {
        log.debug("Запрос данных о фильме id={}", filmId);
        if (!filmsList.containsKey(filmId)) {
            log.error("В текущем списке не найден фильм с id = {}", filmId);
            throw new NotFoundException("В текущем списке не найден фильм с id = " + filmId);
        }
        return filmsList.get(filmId);
    }

    @Override
    public ArrayList<Film> getFilmsList() {
        log.debug("Запрос полного списка фильмов");
        return new ArrayList<>(filmsList.values());
    }
}
