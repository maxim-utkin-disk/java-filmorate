CREATE TABLE IF NOT EXISTS friendship_states (
  state_id INT PRIMARY KEY,
  state_name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS genres (
  genre_id INT PRIMARY KEY,
  genre_name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS mpa_film_ratings (
  rating_id INT PRIMARY KEY,
  rating_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
  film_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  film_name VARCHAR(500) NOT NULL,
  description VARCHAR(1000),
  release_date DATE,
  duration INT,
  rating_id INT REFERENCES mpa_film_ratings(rating_id)
  CONSTRAINT chk_release_date CHECK (release_date >= DATE '1895-12-28'),
  CONSTRAINT chk_duration CHECK (duration > 0)
);

CREATE TABLE IF NOT EXISTS films_genres (
  film_id INT REFERENCES films(film_id) NOT NULL,
  genre_id INT REFERENCES genres(genre_id) NOT NULL,
  PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS users (
  user_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  user_name VARCHAR(50) UNIQUE NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  login VARCHAR(50) UNIQUE NOT NULL,
  birthday DATE
);

CREATE TABLE IF NOT EXISTS friendships (
  user1_id INT REFERENCES users(user_id) NOT NULL,
  user2_id INT REFERENCES users(user_id) NOT NULL,
  state_id INT REFERENCES friendship_states(state_id) NOT NULL,
  PRIMARY KEY (user1_id, user2_id),
  CONSTRAINT check_unique_friends CHECK (user1_id != user2_id)
);

CREATE TABLE IF NOT EXISTS films_likes (
  film_id INT REFERENCES films(film_id) NOT NULL,
  user_id INT REFERENCES users(user_id) NOT NULL,
  PRIMARY KEY (film_id, user_id)
);


COMMENT ON TABLE films IS 'список фильмов';

COMMENT ON TABLE genres IS 'справочник жанров';

COMMENT ON TABLE films_genres IS 'связь многие-ко-многим фильмов и жанров';

COMMENT ON TABLE mpa_film_ratings IS 'справочник рейтингов фильмов';

COMMENT ON TABLE users IS 'список пользователей';

COMMENT ON TABLE friendships IS 'дружба пользователей, многие-ко-многим, промежуточная таблица';

COMMENT ON TABLE films_likes IS 'лайки, поставленные фильму пользователями. Промежуточная таблица для связи многие=-ко-многим';

COMMENT ON TABLE friendship_states IS 'справочник статусов дружбы';