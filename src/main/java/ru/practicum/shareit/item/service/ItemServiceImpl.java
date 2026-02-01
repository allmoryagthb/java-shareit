package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.comments.dto.CommentDto;
import ru.practicum.shareit.item.comments.mapper.CommentMapper;
import ru.practicum.shareit.item.comments.model.Comment;
import ru.practicum.shareit.item.comments.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public Collection<ItemDtoFull> getUsersItemsDto(Long ownerId) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким id не найден"));
        return addBookingsToItems(itemRepository.getAllByOwnerId(ownerId));

    }

    @Override
    public ItemDtoFull getItemDtoById(Long itemId) {
        return ItemMapper.jpaToDtoFull(itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Предмет с таким id не найден")));
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
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким id не найден"));
        newItem.setOwner(user);
        return ItemMapper.jpaToDto(itemRepository.save(newItem));
    }

    @Override
    public ItemDto updateItem(Long ownerId, Long itemId, ItemDto itemDto) {
        Item itemToUpdate = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Нет предмета с таким id"));
        userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким id не найден"));

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
        Item itemToDelete = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Нет предмета с таким id"));
        userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким id не найден"));
        if (!itemToDelete.getOwner().getId().equals(ownerId))
            throw new EntityNotFoundException("Пользователь не является владельцем предмета");

        itemRepository.delete(itemToDelete);
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, Comment comment) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким id не найден"));
        itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Предмет с таким id не найден"));

        if (!bookingRepository.findByBookerIdAndItemIdAndEndBefore(userId, itemId, LocalDate.now()))
            throw new ConflictException("Пользователь не являлся арендатором предмета");

        return CommentMapper.jpaToDto(commentRepository.save(comment));
    }

    private List<ItemDtoFull> addBookingsToItems(List<Item> items) {
        Map<Item, Booking> itemsWithLastBookings = bookingRepository
                .findByItemInAndStartLessThanEqualAndStatus(
                        items,
                        LocalDate.now(),
                        BookingStatus.APPROVED,
                        Sort.by(Sort.Direction.DESC, "start"))
                .stream()
                .collect(Collectors.toMap(Booking::getItem, Function.identity(), (o1, o2) -> o1));

        Map<Item, Booking> itemsWithNextBookings = bookingRepository
                .findByItemInAndStartAfterAndStatus(
                        items,
                        LocalDate.now(),
                        BookingStatus.APPROVED,
                        Sort.by(Sort.Direction.ASC, "end"))
                .stream()
                .collect(Collectors.toMap(Booking::getItem, Function.identity(), (o1, o2) -> o1));

        Map<Item, List<Comment>> itemsWithComments = commentRepository.findByItemIn(items, Sort.by(Sort.Direction.ASC, "created")).stream()
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
}
