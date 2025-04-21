package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.FRUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> usersList = new HashMap<>();

    private Integer getUserId() {
        return FRUtils.getNextIdForObjectsList(usersList.keySet());
    }

    @Override
    public User createNewUser(User newUser) {
        log.debug("Добавление нового пользователя: {}", newUser);
        newUser.setId(getUserId());

        if (newUser.getEmail() == null
                || newUser.getEmail().isBlank()
                || newUser.getEmail().isEmpty()
                || newUser.getEmail().indexOf("@") < 0) {
            log.error("Адрес эл.почты не может быть пустым и должен содержать символ @");
            throw new ValidationException("Адрес эл.почты не может быть пустым и должен содержать символ @");
        }

        if (newUser.getLogin() == null
                || newUser.getLogin().isBlank()
                || newUser.getLogin().isEmpty()
                || newUser.getLogin().indexOf(" ") >= 0) {
            log.error("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }

        if (newUser.getName() == null
                || newUser.getName().isBlank()
                || newUser.getName().isEmpty()) {
            newUser.setName(newUser.getLogin());
        }

        if (newUser.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем: {}", newUser.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        usersList.put(newUser.getId(), newUser);
        return usersList.get(newUser.getId());
    }

    @Override
    public User updateExistingUser(User updUser) {
        log.debug("Обновление пользователя: {}", updUser);
        if (updUser.getId() == null) {
            log.error("Не указан id обновляемого пользователя");
            throw new ValidationException("Не указан id обновляемого пользователя");
        }
        if (!usersList.containsKey(updUser.getId())) {
            log.error("Попытка обновить несуществующего пользователя id = {}", updUser.getId());
            throw new NotFoundException("Попытка обновить несуществующего пользователя id = " + updUser.getId());
        }
        usersList.replace(updUser.getId(), updUser);
        return usersList.get(updUser.getId());
    }

    @Override
    public ArrayList<User> getUsersList() {
        log.debug("Запрос списка полного пользователей");
        return new ArrayList<>(usersList.values());
    }

    @Override
    public User getUserById(int userId) {
        log.debug("Запрос пользователя по id={}", userId);
        if (!usersList.containsKey(  userId)) {
            log.error("В текущем списке не найден пользователь с id = {}", userId);
            throw new NotFoundException("В текущем списке не найден пользователь с id = " + userId);
        }
        return usersList.get(userId);
    }


}
