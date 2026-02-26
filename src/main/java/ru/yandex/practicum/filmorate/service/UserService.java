package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("dbUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUserById(int userId) {
        return userStorage.getUserById(userId);
    }

    public ArrayList<User> getUsersList() {
        return userStorage.getUsersList();
    }

    public User updateExistingUser(User updUser) {
        return userStorage.updateExistingUser(updUser);
    }

    public User createNewUser(User newUser) {
        return userStorage.createNewUser(newUser);
    }

    public List<User> getAllFriendsPerUser(int userId) {
        log.debug("Запрос списка друзей пользователя id={}", userId);
        return userStorage.getUserFriendsList(userId);
    }

    /*public void removeFriend(int userId, int friendUserId) {
        log.debug("Удаление друга friendId={} у пользователя userId={}", friendUserId, userId);
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendUserId);
        user.removeFriend(friendUserId);
        friend.removeFriend(userId);
    }*/

    public void addFriend(int userId, int friendUserId) {
        log.debug("Добавление пользователя friendId={} в друзья к пользователю userId={}", friendUserId, userId);
        if (userId == friendUserId) {
            log.error("Нельзя добавиться в друзья к самому себе! userId={}", userId);
            throw new ValidationException("Нельзя добавиться в друзья к самому себе! userId=" + userId);
        }
        userStorage.addFriend(userId, friendUserId);
    }

    /*public ArrayList<User> getCommonFriendsList(int userId1st, int userId2nd) {
        log.debug("Запрос списка общих друзей для пользователей id={} и id={}", userId1st, userId2nd);
        ArrayList<User> friendsList1st = new ArrayList<>(this.getAllFriendsPerUser(userId1st));
        ArrayList<User> friendsList2nd = new ArrayList<>(this.getAllFriendsPerUser(userId2nd));
        friendsList1st.retainAll(friendsList2nd);
        return friendsList1st;
    }*/

}
