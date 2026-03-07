set referential_integrity false;

truncate table friendships restart identity;
truncate table films_likes restart identity;
truncate table films_genres restart identity;
truncate table users restart identity;
truncate table films restart identity;

set referential_integrity true;

merge into genres key(genre_id) values(1, 'Комедия');
merge into genres key(genre_id) values(2, 'Драма');
merge into genres key(genre_id) values(3, 'Мультфильм');
merge into genres key(genre_id) values(4, 'Триллер');
merge into genres key(genre_id) values(5, 'Документальный');
merge into genres key(genre_id) values(6, 'Боевик');

merge into mpa_film_ratings key (rating_id) values(1, 'G');
merge into mpa_film_ratings key (rating_id) values(2, 'PG');
merge into mpa_film_ratings key (rating_id) values(3, 'PG-13');
merge into mpa_film_ratings key (rating_id) values(4, 'R');
merge into mpa_film_ratings key (rating_id) values(5, 'NC-17');

merge into friendship_states key (state_id) values (1, 'рассматривается');
merge into friendship_states key (state_id) values (2, 'принято');
merge into friendship_states key (state_id) values (3, 'отказано');
