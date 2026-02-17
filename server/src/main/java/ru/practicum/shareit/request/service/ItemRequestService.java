package ru.practicum.shareit.request.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutput;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public List<ItemRequestDtoOutput> getUsersItemRequests(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким id не найден"));
        return addInfo(itemRequestRepository.findAllByRequesterId(userId));
    }

    public Collection<ItemRequestDtoOutput> getAllUsersItemRequests() {
        return addInfo(itemRequestRepository.findAll());
    }

    public ItemRequestDtoOutput getUsersItemRequestById(@Positive Long requestId) {
        return addInfo(List.of(itemRequestRepository.findById(requestId).orElseThrow())).getFirst();
    }

    public ItemRequestDtoOutput addUsersItemRequest(Long ownerId,
                                                    @Valid ItemRequestDtoInput itemRequestDtoInput) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким id не найден"));
        ItemRequest itemRequest = ItemRequestMapper.dtoInputToJpa(itemRequestDtoInput);
        itemRequest.setRequesterId(ownerId);
        itemRequest.setCreated(LocalDateTime.now());
        return ItemRequestMapper.jpaToDtoOutput(itemRequestRepository.save(itemRequest));
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
                                .map(ItemMapper::jpaToDto).toList());
                    else
                        result.setItems(Collections.emptyList());
                    return result;
                }).toList();
    }
}
