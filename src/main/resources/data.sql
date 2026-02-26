delete from genres;
insert into genres(genre_id, genre_name) values(1, 'Комедия');
insert into genres(genre_id, genre_name) values(2, 'Драма');
insert into genres(genre_id, genre_name) values(3, 'Мультфильм');
insert into genres(genre_id, genre_name) values(4, 'Триллер');
insert into genres(genre_id, genre_name) values(5, 'Документальный');
insert into genres(genre_id, genre_name) values(6, 'Боевик');
insert into genres(genre_id, genre_name) values(7, 'Мелодрама');
insert into genres(genre_id, genre_name) values(8, 'Авторское кино');

delete from mpa_film_ratings;
insert into mpa_film_ratings(rating_id, rating_name) values(1, 'G');
insert into mpa_film_ratings(rating_id, rating_name) values(2, 'PG');
insert into mpa_film_ratings(rating_id, rating_name) values(3, 'PG-13');
insert into mpa_film_ratings(rating_id, rating_name) values(4, 'R');
insert into mpa_film_ratings(rating_id, rating_name) values(5, 'NC-17');

delete from friendship_states;
insert into friendship_states(state_id, state_name) values(1, 'рассматривается');
insert into friendship_states(state_id, state_name) values(2, 'принято');
insert into friendship_states(state_id, state_name) values(3, 'отказано');