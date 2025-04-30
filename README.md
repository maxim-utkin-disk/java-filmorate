# Спринт 12.
2025-04-30
Добавляем схему данных и приводим пример некоторых запросов.


## 1. ссылка на ER-диаграмму 
![FILMS](https://github.com/maxim-utkin-disk/java-filmorate/blob/main/uma-yaprak-sprint12-dbscheme-02.jpg)

## 2. Примеры запросов:

### 2.1 список пользователей, поставивших лайки для фильма film_id = NNN
```sql
select
  u.*
from films_likes fl
left join users u on fl.user_id = user_id
where fl.film_id = NNN;
```

### 2.2 скольким фильмам поставил лайки пользователь id = NNN
```sql
select 
  count(fl.film_id) as likes_cnt  
from films_likes fl
where fl.user_id = NNN
group by fl.user_id;
```

### 2.3 запрос списка друзей пользователя с id = NNN (в любом статусе дружбы)
```sql
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
```

### 2.4 Top-10 самых популярных фильмов
```sql
select 
  f.*
from films f2
where f2.film_id in 
  (select 
    f1.film_id,
    count(fl.user_id) as likes_count
  from films f1
  left join films_likes fl on f1.film_id = fl.film_id
  group by f1.film_id
  order by likes_count desc
  limit 10);
```

### 2.5 Общие друзья для пользователей id = NNN и MMM (в любом статусе дружбы)
```sql
select
  u.*
from
  (select f.user2_id as user_id from friendships f where f.user1_id = NNN
  union
  select f.user1_id as user_id from friendships f where f.user2_id = NNN) as u1
  inner join
  (select f.user2_id as user_id from friendships f where f.user1_id = MMM
  union
  select f.user1_id as user_id from friendships f where f.user2_id = MMM) as u2
  on u1.user_id = u2.user2_id
  inner join users u on u2.user_id = u.user_id;
```


