package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.FRUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> usersList = new HashMap<>();

    private Integer getUserId() {
        return FRUtils.getNextIdForObjectsList(usersList.keySet());
    }

    @GetMapping
    private ArrayList<User> getUsersList() {
        log.info("Запрос списка пользователей");
        return new ArrayList<>(usersList.values());
    }

    @PostMapping
    private User postUser(@Valid @RequestBody User user) {
        log.info("Добавление нового пользователя: {}", user.getLogin());
        user.setId(getUserId());

        if (user.getEmail() == null
            || user.getEmail().isBlank()
            || user.getEmail().isEmpty()
            || user.getEmail().indexOf("@") < 0) {
            log.error("Адрес эл.почты не может быть пустым и должен содержать символ @");
            throw new ValidationException("Адрес эл.почты не может быть пустым и должен содержать символ @");
        }

        if (user.getLogin() == null
                || user.getLogin().isBlank()
                || user.getLogin().isEmpty()
                || user.getLogin().indexOf(" ") >= 0) {
            log.error("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }

        if (user.getName() == null
                || user.getName().isBlank()
                || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем: {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        usersList.put(user.getId(), user);
        return usersList.get(user.getId());
    }

    @PutMapping
    private User putUser(@Valid @RequestBody User user) {
        log.info("Обновление пользователя: {}", user.getLogin());
        if (user.getId() == null) {
            log.error("Не указан id обновляемого пользователя");
            throw new ValidationException("Не указан id обновляемого пользователя");
        }
        if (!usersList.containsKey(user.getId())) {
            log.error("Попытка обновить несуществующего пользователя id = {}", user.getId());
            throw new ValidationException("Попытка обновить несуществующего пользователя id = " + user.getId());
        }
        usersList.replace(user.getId(), user);
        return usersList.get(user.getId());
    }

}
