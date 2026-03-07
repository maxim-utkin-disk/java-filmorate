package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.service.FilmGenreService;

import java.util.ArrayList;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class FilmGenreController {
    private final FilmGenreService filmGenreService;

    @GetMapping
    public ArrayList<FilmGenre> getFilmGenreList() {
        return new ArrayList<FilmGenre>(filmGenreService.getAllFilmGenreList());
    }

    @GetMapping("/{id}")
    public FilmGenre getFilmRatingById(@PathVariable int id) {
        return filmGenreService.getFilmGenreById(id);
    }

}
