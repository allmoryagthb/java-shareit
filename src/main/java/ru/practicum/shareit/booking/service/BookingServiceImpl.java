package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;

import static java.time.LocalDateTime.now;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto addBooking(BookingDtoInput bookingDtoInput, Long bookerId) {
        User user = getUser(bookerId);
        Item item = getItem(bookingDtoInput.getItemId());

        if (!item.getAvailable())
            throw new ValidationException("Предмет нельзя забронировать");

        if (item.getOwner().getId().equals(bookerId))
            throw new ValidationException("Владелец не может забронировать свой предмет");

        if (Objects.isNull(bookingDtoInput.getStart()) || Objects.isNull(bookingDtoInput.getEnd()))
            throw new ValidationException("Не указаны даты начала/конца бронирования предмета");

        if (bookingDtoInput.getStart().isBefore(now()))
            throw new ValidationException("Дата начала бронирования должна быть в будущем");

        if (bookingDtoInput.getEnd().isBefore(now()))
            throw new ValidationException("Дата окончания бронирования должна быть в будущем");

        if (bookingDtoInput.getStart().isAfter(bookingDtoInput.getEnd()))
            throw new ValidationException("Дата начала бронирования должна быть раньше даты окончания срока бронирования");

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setStart(bookingDtoInput.getStart());
        booking.setEnd(bookingDtoInput.getEnd());
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        bookingRepository.save(booking);
        log.info("Заявка на бронирование с id = {} создана", booking.getId());

        return BookingMapper.jpaToDto(booking);
    }

    @Override
    public BookingDtoOutput updateBookingStatus(Long bookingId, Long userId, Boolean isApproved) {
        Booking booking = getBooking(bookingId);
        User user = getUser(userId);

        if (booking.getBooker().getId().equals(userId)) {
            if (!booking.getStatus().equals(BookingStatus.CANCELED) && !isApproved)
                booking.setStatus(BookingStatus.CANCELED);
            else if (booking.getStatus().equals(BookingStatus.CANCELED) && !isApproved)
                throw new ConflictException("Заявка на бронирование уже отменена");
            else
                throw new ConflictException("Одобрить заявку может только владелец");
        } else if (itemRepository.findByOwner(user).getOwner().getId().equals(userId)) {
            if (booking.getStatus().equals(BookingStatus.WAITING) && isApproved)
                booking.setStatus(BookingStatus.APPROVED);
            else if (booking.getStatus().equals(BookingStatus.APPROVED) && isApproved)
                throw new ConflictException("Заявка на бронирование уже подтверждена");
            else if (booking.getStatus().equals(BookingStatus.REJECTED))
                throw new ConflictException("Заявка на бронирование уже отклонена");
            else
                booking.setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.jpaToDtoOutput(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        getUser(userId);
        Booking booking = getBooking(bookingId);

        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId))
            throw new ConflictException("Пользователь с id '%d' не является владельцем/арендатором предмета".formatted(userId));

        return BookingMapper.jpaToDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookingsByBooker(String state, Long bookerId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        getUser(bookerId);
        List<Booking> result;

        switch (state) {
            case "ALL" -> result = bookingRepository.findByBookerId(bookerId, sort);
            case "CURRENT" ->
                    result = bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(bookerId, now(), now(), sort);
            case "PAST" -> result = bookingRepository.findByBookerIdAndEndBefore(bookerId, now(), sort);
            case "FUTURE" -> result = bookingRepository.findByBookerIdAndStartAfter(bookerId, now(), sort);
            case "WAITING" ->
                    result = bookingRepository.findByBookerIdAndStatusEqualsIgnoreCase(bookerId, BookingStatus.WAITING, sort);
            case "REJECTED" ->
                    result = bookingRepository.findByBookerIdAndStatusEqualsIgnoreCase(bookerId, BookingStatus.REJECTED, sort);
            default -> throw new ValidationException("Неизвестный статус");
        }

        return result.stream()
                .map(BookingMapper::jpaToDto)
                .toList();
    }

    @Override
    public List<BookingDto> getAllBookingsByOwner(String state, Long ownerId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        getUser(ownerId);
        List<Booking> result;

        switch (state) {
            case "ALL" -> result = bookingRepository.findByItemOwnerId(ownerId, sort);
            case "CURRENT" ->
                    result = bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfter(ownerId, now(), now(), sort);
            case "PAST" -> result = bookingRepository.findByItemOwnerIdAndEndBefore(ownerId, now(), sort);
            case "FUTURE" -> result = bookingRepository.findByItemOwnerIdAndStartAfter(ownerId, now(), sort);
            case "WAITING" ->
                    result = bookingRepository.findByItemOwnerIdAndStatusEqualsIgnoreCase(ownerId, BookingStatus.WAITING, sort);
            case "REJECTED" ->
                    result = bookingRepository.findByItemOwnerIdAndStatusEqualsIgnoreCase(ownerId, BookingStatus.REJECTED, sort);
            default -> throw new ValidationException("Неизвестный статус");
        }
        return result.stream()
                .map(BookingMapper::jpaToDto)
                .toList();
    }

    private Booking getBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Заявка на бронирование с id = '%d' не найдена".formatted(id)));
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с id = '%d' не найден".formatted(id)));
    }

    private Item getItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Предмет с id = '%d' не найден".formatted(id)));
    }
}
