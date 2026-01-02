package ru.practicum.shareit.item.storage;

import lombok.Data;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
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

    public Collection<ItemDto> getUsersItems(Long ownerId) {
        return getAllItems().stream()
                .filter(e -> e.getOwner().getId().equals(ownerId))
                .map(ItemMapper::jpaToDto)
                .toList();
    }

    public Optional<Item> getItemById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    public Collection<Item> getAvailableItemByName(String searchText) {
        return items.values()
                .stream()
                .filter(e -> (e.getName().contains(searchText) || e.getDescription().contains(searchText)) && e.getIsAvailableStatus().equals(true))
                .toList();
    }

    public Item addItem(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        return item;
    }

    public Item deleteItem(Long id) {
        return items.remove(id);
    }

    private Long getNextId() {
        long currentId = items.size();
        return ++currentId;
    }
}
