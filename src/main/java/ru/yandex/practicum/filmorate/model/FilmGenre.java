package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class FilmGenre {
    @EqualsAndHashCode.Include
    private Integer id;
    @NotBlank
    private String name;
}
