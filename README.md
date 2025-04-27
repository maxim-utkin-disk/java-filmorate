# java-filmorate
Template repository for Filmorate project.

Спринт 12.
2025-04-27
Добавляем схему данных и приводим пример некоторых запросов.


1. ссылка на ER-диаграмму 
![FILMS](https://github.com/maxim-utkin-disk/java-filmorate/blob/main/uma-yaprak-sprint12-dbscheme-01.jpg)

2. Примеры запросов:
-- список пользователей, поставивших лайки для фильма film_id = NNN
select
  fl.film_id,
  (select f.film_name from films f where f.film_id = fl.film_id) as film_name,
  u.*
from films_likes fl
left join users u on fl.user_id = user_id
where fl.film_id = NNN;

-- скольким фильмам поставил лайки пользователь id = NNN
select 
  fl.user_id,
  (select u.user_name from users u where u.user_id = fl.user_id) as user_name,
  count(fl.film_id) as likes_cnt  
from films_likes fl
where fl.user_id = NNN
group by fl.user_id;

-- запрос списка друзей пользователя с id = NNN
select 
  u.*
from users u 
where u.user_id in (
  select fs.user2_id
  from friendships fs
  where fs.user1_id = NNN
  union 
  select fs.user1_id
  from friendships fs
  where fs.user2_id = NNN);

-- ... остальные запросы мне кажутся достаточно очевидными, чтобы перечислять их здесь
