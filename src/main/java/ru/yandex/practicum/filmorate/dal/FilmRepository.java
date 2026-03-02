package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class FilmRepository extends BaseRepository<Film> {

    private static final String FIND_ALL_QUERY =
            "select f.film_id, f.film_name, f.description, f.release_date, f.duration, f.rating_id,  mfr.rating_name " +
                    "from films f left join mpa_film_ratings mfr " +
                    " on f.rating_id = mfr.rating_id";
    private static final String FIND_BY_ID_QUERY =
            "select f.film_id, f.film_name, f.description, f.release_date, f.duration, f.rating_id,  mfr.rating_name " +
                    "from films f left join mpa_film_ratings mfr  " +
                    " on f.rating_id = mfr.rating_id  " +
                    " where f.film_id = ?";
    private static final String INSERT_QUERY = "insert into films(film_name, description, release_date, duration, rating_id) values(?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "update films f set film_name = ?, description = ?, release_date = ?, duration = ?, rating_id = ?  where f.film_id = ?";
    private static final String CREATE_FILM_GENRE_QUERY = "merge into films_genres key(film_id, genre_id) values(?, ?)";
    private static final String ADD_LIKE_QUERY = "insert into films_likes(film_id, user_id) values(?, ?)";
    private static final String DEL_LIKE_QUERY = "delete from films_likes fl where fl.film_id = ? and fl.user_id = ?";
    private static final String GET_TOP_POP_FILMS_QUERY =
            "select f.film_id, f.film_name, f.description, f.duration, f.release_date, " +
                    " f.rating_id, (select mfr.rating_name from mpa_film_ratings mfr where mfr.rating_id =  f.rating_id) as rating_name, " +
                    " count(1) as likes_cnt " +
                    " from films f, films_likes fl " +
                    " where fl.film_id = f.film_id " +
                    " group by f.film_id, f.film_name, f.description, f.duration, f.rating_id, f.release_date " +
                    " order by likes_cnt desc limit ?";


    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Film> findById(Integer filmId) {
        return findOne(FIND_BY_ID_QUERY, filmId);
    }

    public Film create(Film film) {
        Integer id = insert(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        addFilmGenre(film);
        return film;
    }

    public Film update(Film film) {
        update(UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        return film;
    }

    public void addFilmGenre(Film film) {
        for (FilmGenre genre : film.getGenres()) {
            update(CREATE_FILM_GENRE_QUERY, film.getId(), genre.getId());
        }
    }

    public void addLike(User u, Film f) {
        update(ADD_LIKE_QUERY, f.getId(), u.getId());
    }

    public void removeLike(User u, Film f) {
        update(DEL_LIKE_QUERY, f.getId(), u.getId());
    }

    public List<Film> getTopPopularFilms(Integer count) {
        return findMany(GET_TOP_POP_FILMS_QUERY, count);

    }

}
