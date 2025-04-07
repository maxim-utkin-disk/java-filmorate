package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
   private Integer id;
   @NotEmpty
   private String name;
   @Size(max = 200)
   private String description;
   @NotNull
   @JsonFormat
   private LocalDate releaseDate;
   @NotNull
   @Min(1)
   private Integer duration;

   public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.releaseDate = releaseDate;
      this.duration = duration;
   }

}
