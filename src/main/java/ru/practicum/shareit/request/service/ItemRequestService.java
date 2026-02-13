package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutput;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;

    public List<ItemRequestDtoOutput> getUsersItemRequests(Long userId) {
        return addInfo(itemRequestRepository.findAllByRequesterId(userId));
    }

    private List<ItemRequestDtoOutput> addInfo(List<ItemRequest> itemRequests) {
        if (itemRequests == null || itemRequests.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, List<Item>> itemsMap = itemRepository.findByRequestIn(itemRequests).stream()
                .collect(groupingBy(e -> e.getRequest().getId(), toList()));

        return itemRequests.stream()
                .map(element -> {
                    ItemRequestDtoOutput result = ItemRequestMapper.jpaToDtoOutput(element);

                    List<Item> itemsByElementId = itemsMap.get(element.getId());

                    if (Objects.nonNull(itemsByElementId))
                        result.setItems(itemsByElementId.stream()
                                .map(ItemMapper::jpaToDtoFull).toList());

                    return result;
                }).toList();
    }
}
