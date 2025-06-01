package ru.practicum.shareit.user.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody User user) {
        log.info("Получен запрос на создание пользователя");
        UserDto savedUser = userService.saveUser(user);
        log.info("Создан новый пользователь: {}", savedUser);
        return savedUser;
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@PathVariable @NotNull Long userId, @RequestBody User user) {
        log.info("Получен запрос на обновление пользователя c id={}", userId);
        UserDto updatedUser = userService.updateUser(userId, user);
        log.info("Обновление пользователя завершено: {}", user);
        return updatedUser;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable @NotNull Long userId) {
        log.info("Получен запрос на получение пользователя с id={}", userId);
        return userService.getUserById(userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUsers() {
        log.info("Получен запрос на получение списка пользователей");
        return userService.getAllUsers();
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable @NotNull Long userId) {
        log.info("Получен запрос на удаление пользователя");
        userService.deleteUser(userId);
        log.info("Пользователь с id={} успешно удален", userId);
    }
}