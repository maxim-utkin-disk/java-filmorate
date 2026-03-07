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
            "select \n" +
                    "  f.film_id, f.film_name, f.description, f.release_date, f.duration, \n" +
                    "  mfr.rating_id, mfr.rating_name,\n" +
                    "  '['||group_concat(distinct '{\"id\":' ||g.genre_id||', \"name\":\"'||g.genre_name||'\"}')||']' as film_genres_list\n" +
                    "from films f\n" +
                    "\tleft join mpa_film_ratings mfr on f.rating_id = mfr.rating_id\n" +
                    "\tleft join films_genres fg on f.film_id = fg.film_id\n" +
                    "\tleft join genres g on fg.genre_id = g.genre_id\n" +
                    "group by f.film_id, f.film_name, f.description, f.release_date, f.duration, \n" +
                    "  \t\tmfr.rating_id, mfr.rating_name";
    private static final String FIND_BY_ID_QUERY =
            "select \n" +
                    "  f.film_id, f.film_name, f.description, f.release_date, f.duration, \n" +
                    "  mfr.rating_id, mfr.rating_name,\n" +
                    "  '['||group_concat(distinct '{\"id\":' ||g.genre_id||', \"name\":\"'||g.genre_name||'\"}')||']' as film_genres_list\n" +
                    "from films f\n" +
                    "\tleft join mpa_film_ratings mfr on f.rating_id = mfr.rating_id\n" +
                    "\tleft join films_genres fg on f.film_id = fg.film_id\n" +
                    "\tleft join genres g on fg.genre_id = g.genre_id\n" +
                    "where f.film_id = ?\n" +
                    "group by f.film_id, f.film_name, f.description, f.release_date, f.duration, \n" +
                    "  \t\tmfr.rating_id, mfr.rating_name";
    private static final String INSERT_QUERY = "insert into films(film_name, description, release_date, duration, rating_id) values(?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "update films f set film_name = ?, description = ?, release_date = ?, duration = ?, rating_id = ?  where f.film_id = ?";
    private static final String CREATE_FILM_GENRE_QUERY = "merge into films_genres key(film_id, genre_id) values(?, ?)";
    private static final String ADD_LIKE_QUERY = "insert into films_likes(film_id, user_id) values(?, ?)";
    private static final String DEL_LIKE_QUERY = "delete from films_likes fl where fl.film_id = ? and fl.user_id = ?";
    private static final String GET_TOP_POP_FILMS_QUERY =
            "select \n" +
                    "  f.film_id, f.film_name, f.description, f.release_date, f.duration, \n" +
                    "  mfr.rating_id, mfr.rating_name,\n" +
                    "  '['||group_concat(distinct '{\"id\":' ||g.genre_id||', \"name\":\"'||g.genre_name||'\"}')||']' as film_genres_list,\n" +
                    "  count(1) as likes_cnt \n" +
                    "from films f\n" +
                    "\tleft join mpa_film_ratings mfr on f.rating_id = mfr.rating_id\n" +
                    "\tleft join films_genres fg on f.film_id = fg.film_id\n" +
                    "\tleft join genres g on fg.genre_id = g.genre_id\n" +
                    "\tleft join films_likes fl on f.film_id = fl.film_id\n" +
                    "group by f.film_id, f.film_name, f.description, f.release_date, f.duration, \n" +
                    "  \t\tmfr.rating_id, mfr.rating_name\n" +
                    "order by likes_cnt desc limit ?";


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
