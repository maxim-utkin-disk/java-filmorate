package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
   private FilmRating mpa;
   @EqualsAndHashCode.Exclude
   private Set<Integer> likesList = new HashSet<>();
   private List<FilmGenre> genres = new ArrayList<FilmGenre>();


   public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.releaseDate = releaseDate;
      this.duration = duration;
   }

   public Film(){};

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration, Integer ratingId, String ratingName ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.setMpa(new FilmRating(ratingId, ratingName));
    }

   public void addLike(User user) {
      likesList.add(user.getId());
   }

   public void removeLike(User user) {
      likesList.remove(user.getId());
   }

}
