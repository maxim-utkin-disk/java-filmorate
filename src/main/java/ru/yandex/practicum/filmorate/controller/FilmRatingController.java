package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.service.FilmRatingService;

import java.util.ArrayList;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class FilmRatingController {
    private final FilmRatingService filmRatingService;

    @GetMapping
    public ArrayList<FilmRating> getFilmRatingList() {
        return new ArrayList<FilmRating>(filmRatingService.getAllFilmRatingsList());
    }

    @GetMapping("/{id}")
    public FilmRating getFilmRatingById(@PathVariable int id) {
        return filmRatingService.getFilmRatingById(id);
    }

}
