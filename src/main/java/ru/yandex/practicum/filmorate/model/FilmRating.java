package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class FilmRating {
    @EqualsAndHashCode.Include
    private Integer id;
    @NotBlank
    private String name;

    public FilmRating(Integer ratingId, String ratingName) {
        this.id = ratingId;
        this.name = ratingName;
    }
}

