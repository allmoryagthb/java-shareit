package ru.practicum.shareit.user.storage;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@Repository
public class UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public @NotBlank Optional<User> getUserById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public User addUser(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    public User deleteUserById(Long id) {
        return users.remove(id);
    }

    private Long getNextId() {
        long currentId = users.size();
        return ++currentId;
    }
}
