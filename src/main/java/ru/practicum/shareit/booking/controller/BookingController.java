package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                 @Valid @RequestBody BookingDtoInput bookingDtoInput) {
        log.info("Добавить новую заявку");
        return bookingService.addBooking(bookingDtoInput, bookerId);
    }

    @PatchMapping("/{bookingId}?approved={approved}")
    public BookingDto updateBooking(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                    @PathVariable(name = "bookingId") Long bookingId,
                                    @PathVariable(name = "approved") Boolean approved) {
        log.info("Обновить заявку");
        return bookingService.updateBookingStatus(bookingId, ownerId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable(name = "bookingId") Long bookingId) {
        log.info("Получить заявку по id = {}", bookingId);
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllByBooker(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                           @RequestHeader("X-Sharer-User-Id") Long bookerId) {
        log.info("Получить список всех заявок пользователя с id {}", bookerId);
        return bookingService.getAllBookingsByBooker(state, bookerId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                          @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получить список всех заявок владельцем с id {}", ownerId);
        return bookingService.getAllBookingsByOwner(state, ownerId);
    }
}
