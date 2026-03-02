package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class FilmRating {
    @EqualsAndHashCode.Include
    private Integer id;
    @NotNull
    @NotBlank
    private String name;
}

