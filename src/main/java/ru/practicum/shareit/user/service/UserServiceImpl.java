package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String USER_NOT_FOUND = "Пользователь с id = %d не найден";
    private final UserRepository userRepository;

    @Override
    public UserDto saveUser(User user) {
        validateEmail(user.getEmail());
        User savedUser = userRepository.saveUser(user);
        return UserMapper.toUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(Long userId, User user) {
        User userFromRepository = userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND, userId)));
        if (user.getEmail() != null && !userFromRepository.getEmail().equals(user.getEmail())) {
            validateEmail(user.getEmail());
        }
        User updatedUser = userRepository.updateUser(userId, user);
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND, id)));
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND, id)));
        userRepository.deleteUser(id);
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ConditionsNotMetException("Email должен быть указан");
        }
        if (!email.contains("@")) {
            throw new ConditionsNotMetException("Email должен содержать символ @");
        }
        if (isEmailUsed(email)) {
            throw new DuplicatedDataException("Этот email уже используется");
        }
    }

    private boolean isEmailUsed(String email) {
        return userRepository.getAllUsers().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }
}