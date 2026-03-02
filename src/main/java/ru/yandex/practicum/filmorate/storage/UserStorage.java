package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

public interface UserStorage {

    User createNewUser(User newUser);

    User updateExistingUser(User updUser);

    User getUserById(int userId);

    ArrayList<User> getUsersList();

    void addFriend(int userId, int friendUserId);

    void removeFriend(int userId, int friendUserId);

    ArrayList<User> getCommonFriendsList(int id, int otherId);

    ArrayList<User> getUserFriendsList(int userId);

}
