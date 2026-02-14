package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comments.dto.CommentDto;
import ru.practicum.shareit.item.comments.dto.CommentDtoShort;
import ru.practicum.shareit.item.comments.mapper.CommentMapper;
import ru.practicum.shareit.item.comments.model.Comment;
import ru.practicum.shareit.item.comments.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ru.practicum.shareit.user.repository.UserRepository userRepository;

    @Override
    public Collection<ItemDtoFull> getUserItems(Long ownerId) {
        getUser(ownerId);
        return addInfoToItems(itemRepository.getAllByOwnerId(ownerId));

    }

    @Override
    public ItemDtoFull getItemDtoById(Long itemId, Long userId) {
        return addInfoToItem(getItem(itemId), userId);
    }

    @Override
    public Collection<ItemDto> getAvailableItemsDtoByText(String searchText) {
        if (searchText.isBlank())
            return Collections.emptyList();
        return itemRepository.searchAvailableItemsByText(searchText)
                .stream()
                .map(ItemMapper::jpaToDto)
                .toList();
    }

    @Override
    public ItemDto addItem(Long ownerId, ItemDto itemDto) {
        Item newItem = ItemMapper.dtoToJpa(itemDto);
        ru.practicum.shareit.user.model.User user = getUser(ownerId);
        newItem.setOwner(user);
        return ItemMapper.jpaToDto(itemRepository.save(newItem));
    }

    @Override
    public ItemDto updateItem(Long ownerId, Long itemId, ItemDto itemDto) {
        Item itemToUpdate = getItem(itemId);
        getUser(ownerId);

        if (!itemToUpdate.getOwner().getId().equals(ownerId))
            throw new EntityNotFoundException("Пользователь не является владельцем предмета");

        if (Objects.nonNull(itemDto.getName())
            && !itemDto.getName().isBlank()
            && !itemToUpdate.getName().equals(itemDto.getName()))
            itemToUpdate.setName(itemDto.getName());

        if (Objects.nonNull(itemDto.getDescription())
            && !itemToUpdate.getDescription().isBlank()
            && !itemToUpdate.getDescription().equals(itemDto.getDescription()))
            itemToUpdate.setDescription(itemDto.getDescription());

        if (Objects.nonNull(itemDto.getAvailable())
            && !itemToUpdate.getAvailable().equals(itemDto.getAvailable()))
            itemToUpdate.setAvailable(itemDto.getAvailable());

        return ItemMapper.jpaToDto(itemRepository.save(itemToUpdate));
    }

    @Override
    public void deleteItemById(Long ownerId, Long itemId) {
        Item itemToDelete = getItem(itemId);
        getUser(ownerId);
        if (!itemToDelete.getOwner().getId().equals(ownerId))
            throw new EntityNotFoundException("Пользователь не является владельцем предмета");

        itemRepository.delete(itemToDelete);
    }

    @Override
    public CommentDtoShort addComment(Long userId, Long itemId, CommentDto commentDto) {
        ru.practicum.shareit.user.model.User user = getUser(userId);
        Item item = getItem(itemId);

        if (Objects.isNull(bookingRepository
                .findFirstByItemIdAndBookerIdAndEndIsBefore(itemId, userId, now())))
            throw new ValidationException("Пользователь не являлся арендатором предмета");

        if (Objects.isNull(commentDto.getText()) || commentDto.getText().isBlank())
            throw new ValidationException("Текст комментария не может быть пустым");

        Comment comment = CommentMapper.dtoToJpa(commentDto);
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(now());
        return CommentMapper.jpaToDtoShort(commentRepository.save(comment));
    }

    private ItemDtoFull addInfoToItem(Item item, Long userId) {
        ItemDtoFull itemDtoFull = ItemMapper.jpaToDtoFull(item);

        if (item.getOwner().getId().equals(userId)) {
            Booking bookingLast = bookingRepository
                    .findFirstByItemIdAndEndBeforeAndStatus(item.getId(), now(),
                            BookingStatus.APPROVED, Sort.by(DESC, "start")).orElse(null);

            itemDtoFull.setLastBooking(Objects.nonNull(bookingLast) ? BookingMapper.jpaToDto(bookingLast) : null);

            Booking bookingNext = bookingRepository
                    .findFirstByItemIdAndStartAfterAndStatus(item.getId(), now(),
                            BookingStatus.APPROVED, Sort.by(ASC, "start")).orElse(null);
            itemDtoFull.setNextBooking(Objects.nonNull(bookingNext) ? BookingMapper.jpaToDto(bookingNext) : null);
        }

        itemDtoFull.setComments(commentRepository.findAllByItemId(item.getId())
                .stream()
                .map(CommentMapper::jpaToDto)
                .collect(toList()));

        return itemDtoFull;
    }

    private List<ItemDtoFull> addInfoToItems(List<Item> items) {
        Map<Item, Booking> itemsWithLastBookings = bookingRepository
                .findByItemInAndStartLessThanEqualAndStatus(
                        items,
                        now(),
                        BookingStatus.APPROVED,
                        Sort.by(DESC, "start"))
                .stream()
                .collect(Collectors.toMap(Booking::getItem, Function.identity()));

        Map<Item, Booking> itemsWithNextBookings = bookingRepository
                .findByItemInAndStartAfterAndStatus(
                        items,
                        now(),
                        BookingStatus.APPROVED,
                        Sort.by(Sort.Direction.ASC, "start"))
                .stream()
                .collect(Collectors.toMap(Booking::getItem, Function.identity()));

        Map<Item, List<Comment>> itemsWithComments = commentRepository
                .findByItemIn(items, Sort.by(Sort.Direction.DESC, "created")).stream()
                .collect(groupingBy(Comment::getItem, toList()));

        List<ItemDtoFull> result = new ArrayList<>();
        for (Item item : items) {
            ItemDtoFull itemDtoFull = ItemMapper.jpaToDtoFull(item);
            Booking bookingLast = itemsWithLastBookings.get(item);

            if (!Objects.isNull(bookingLast))
                itemDtoFull.setLastBooking(BookingMapper.jpaToDto(bookingLast));

            Booking bookingNext = itemsWithNextBookings.get(item);

            if (!Objects.isNull(bookingNext))
                itemDtoFull.setNextBooking(BookingMapper.jpaToDto(bookingNext));

            List<Comment> itemComments = itemsWithComments.getOrDefault(item, Collections.emptyList());
            itemDtoFull.setComments(itemComments.stream()
                    .map(CommentMapper::jpaToDto)
                    .toList());

            result.add(itemDtoFull);
        }

        return result;
    }

    private ru.practicum.shareit.user.model.User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id = '%d' не найден".formatted(id)));
    }

    private Item getItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Предмет с id = '%d' не найден".formatted(id)));
    }
}
