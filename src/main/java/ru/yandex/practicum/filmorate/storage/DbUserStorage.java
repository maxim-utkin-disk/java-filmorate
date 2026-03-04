package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Repository
@Component("DbUserStorage")
@Qualifier("dbUserStorage")
public class DbUserStorage extends UserRepository implements UserStorage {

    public DbUserStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public ArrayList<User> getUsersList() {
        log.debug("Запрос из БД всего списка пользователей");
        return (ArrayList<User>) findAll();
    }

    public User getUserById(int userId) {
        log.debug("Запрос из БД пользователя по id={}", userId);
        Optional<User> ofr = findById(userId);
        if (ofr.isPresent()) {
            return ofr.get();
        } else {
            log.error("В текущих данных БД не найден пользователь с id = {}", userId);
            throw new NotFoundException("В текущих данных БД не найден пользовтаель с id = " + userId);
        }
    }

    @Override
    public User createNewUser(User newUser) {
        log.debug("Создание пользователя user_name = {}", newUser.getName());
        return create(newUser);
    }

    @Override
    public User updateExistingUser(User updUser) {
        log.debug("Обновление пользователя user_id = {}", updUser.getId());
        return update(updUser);
    }

    //@Override
    public void addFriend(int userId, int friendUserId) {
        log.debug("Добавление пользователя friendUserId = {} в друзья к пользователю user_id = {}", friendUserId, friendUserId);
        super.addFriend(userId, friendUserId);
    }

    //@Override
    public ArrayList<User> getUserFriendsList(int userId) {
        log.debug("1-Запрос из БД списка всех друзей пользователя");
        return (ArrayList<User>)super.getUserFriendsList(userId);
    }

    public ArrayList<User> getAllFriendsPerUser(int userId) {
        log.debug("2-Запрос из БД списка всех друзей пользователя");
        return (ArrayList<User>)getUserFriendsList(userId);
    }

    public void removeFriend(int userId, int friendUserId) {
        log.debug("Удаление пользователя friendUserId = {} из друзей к пользователя user_id = {}", friendUserId, userId);
        super.removeFriend(userId, friendUserId);
    }

    public ArrayList<User> getCommonFriendsList(int id, int otherId) {
        log.debug("Выборка общих друзей пользователя user_id = {} и пользователя user_id = {}", id, otherId);
        return (ArrayList<User>)super.getCommonFriendsList(id, otherId);
    }

}