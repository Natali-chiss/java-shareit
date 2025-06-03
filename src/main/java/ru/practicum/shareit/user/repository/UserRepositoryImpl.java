package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    private long newId = 0;

    public User saveUser(User user) {
        user.setId(++newId);
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(Long userId, User user) {
        User userFromRepository = getUserById(userId).get(); // проверка в сервисе
        if (user.getName() != null) {
            userFromRepository.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userFromRepository.setEmail(user.getEmail());
        }
        return userFromRepository;
    }

    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }

    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }
}
