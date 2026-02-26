package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.storage.DbFilmRatingStorage;

import java.util.ArrayList;

@Slf4j
@Service
public class FilmRatingService {

    private final DbFilmRatingStorage filmRatingStorage;

    @Autowired
    public FilmRatingService(DbFilmRatingStorage filmRatingStorage) {
        this.filmRatingStorage = filmRatingStorage;
    }

    public ArrayList<FilmRating> getAllFilmRatingsList() {
        return filmRatingStorage.getFilmRatingList();
    }

    public FilmRating getFilmRatingById(int id) {
        return filmRatingStorage.getFilmRatingById(id);
    }


}