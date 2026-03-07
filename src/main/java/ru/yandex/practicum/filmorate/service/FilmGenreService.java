package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.DbFilmGenreStorage;

import java.util.ArrayList;

@Service
public class FilmGenreService {

    private final DbFilmGenreStorage filmGenreStorage;

    @Autowired
    public FilmGenreService(DbFilmGenreStorage filmGenreStorage) {
        this.filmGenreStorage = filmGenreStorage;
    }

    public ArrayList<FilmGenre> getAllFilmGenreList() {
        return filmGenreStorage.getFilmGenreList();
    }

    public FilmGenre getFilmGenreById(int id) {
        return filmGenreStorage.getFilmGenreById(id);
    }


}