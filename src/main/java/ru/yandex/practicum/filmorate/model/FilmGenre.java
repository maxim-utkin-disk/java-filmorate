package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class FilmGenre {
    @EqualsAndHashCode.Include
    private Integer genreId;
    @NotNull
    @NotBlank
    private String genreName;
}
