package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
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

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto addBooking(BookingDtoInput bookingDtoInput, Long bookerId) {
        User user = userRepository.findById(bookerId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id = '%s' не найден".formatted(bookerId)));
        Item item = itemRepository.findById(bookingDtoInput.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Предмет с таким id не найден"));

        if (!item.getAvailable())
            throw new ValidationException("Предмет нельзя забронировать");

        if (item.getOwner().getId().equals(bookerId))
            throw new ValidationException("Владелец не может забронировать свой предмет");

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
    public BookingDto updateBookingStatus(Long bookingId, Long ownerId, Boolean isApproved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Заявка на бронирование с id = '%s' не найдена".formatted(bookingId)));
        userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id = '%s' не найден".formatted(ownerId)));

        if (booking.getStatus().equals(BookingStatus.APPROVED) && isApproved)
            throw new ConflictException("Заявка на бронирование уже подтверждена");

        if (booking.getStatus().equals(BookingStatus.REJECTED) && !isApproved)
            throw new ConflictException("Заявка на бронирование уже отменена");


        return BookingMapper.jpaToDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id = '%s' не найден".formatted(userId)));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Заявка на бронирование с id = '%s' не найдена".formatted(bookingId)));

        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId))
            throw new ConflictException("Пользователь с id '%d' не является владельцем/арендатором предмета".formatted(userId));

        return BookingMapper.jpaToDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookingsByBooker(String state, Long bookerId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        userRepository.findById(bookerId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id = '%s' не найден".formatted(bookerId)));
        List<Booking> result;

        switch (state) {
            case "ALL" -> result = bookingRepository.findByBookerId(bookerId, sort);
            case "CURRENT" ->
                    result = bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(bookerId, LocalDate.now(), LocalDate.now(), sort);
            case "PAST" -> result = bookingRepository.findByBookerIdAndEndBefore(bookerId, LocalDate.now(), sort);
            case "FUTURE" -> result = bookingRepository.findByBookerIdAndStartAfter(bookerId, LocalDate.now(), sort);
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
        userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id = '%s' не найден".formatted(ownerId)));
        List<Booking> result;

        switch (state) {
            case "ALL" -> result = bookingRepository.findByItemOwnerId(ownerId, sort);
            case "CURRENT" ->
                    result = bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfter(ownerId, LocalDate.now(), LocalDate.now(), sort);
            case "PAST" -> result = bookingRepository.findByItemOwnerIdAndEndBefore(ownerId, LocalDate.now(), sort);
            case "FUTURE" -> result = bookingRepository.findByItemOwnerIdAndStartAfter(ownerId, LocalDate.now(), sort);
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
}
