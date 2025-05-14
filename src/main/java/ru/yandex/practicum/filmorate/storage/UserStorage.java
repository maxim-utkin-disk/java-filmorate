package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

public interface UserStorage {

    User createNewUser(User newUser);

    User updateExistingUser(User updUser);

    User getUserById(int userId);

    ArrayList<User> getUsersList();

}
