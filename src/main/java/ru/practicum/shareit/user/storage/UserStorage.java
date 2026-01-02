package ru.practicum.shareit.user.storage;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
@Repository
public class UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public @NotBlank User getUserById(Long userId) {
        return users.get(userId);
    }

    private Long getNextId() {
        long currentId = users.keySet()
                .stream()
                .mapToLong(e -> e)
                .max()
                .orElse(0);
        return ++currentId;
    }
}
