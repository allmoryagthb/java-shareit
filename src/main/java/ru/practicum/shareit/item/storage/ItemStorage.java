package ru.practicum.shareit.item.storage;

import lombok.Data;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@Repository
public class ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();

    public Collection<Item> getAllItems() {
        return items.values();
    }

    public Collection<Item> getUsersItems(Long ownerId) {
        return items.values().stream()
                .filter(e -> e.getOwner().getId().equals(ownerId))
                .toList();
    }


    public Optional<Item> getItemById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    public Optional<Item> getItemByName(String name) {
        return items.values()
                .stream()
                .filter(e -> e.getName().equalsIgnoreCase(name))
                .findAny();
    }

    public Item addItem(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        return item;
    }

    public Item updateItem(Item item) {
        return items.put(item.getId(), item);
    }

    public Item deleteItem(Long id) {
        return items.remove(id);
    }

    private Long getNextId() {
        long currentId = items.keySet()
                .stream()
                .mapToLong(e -> e)
                .max()
                .orElse(0);
        return ++currentId;
    }
}
