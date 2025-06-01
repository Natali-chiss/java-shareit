package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
        User savedUser = userRepository.saveUser(user);
        return UserMapper.toUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(Long userId, User user) {
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
}