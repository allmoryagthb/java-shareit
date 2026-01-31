package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(BookingDto bookingDto, Long bookerId);

    BookingDto updateBookingStatus(Long bookingId, Long ownerId, Boolean isApproved);

    BookingDto getBookingById(Long bookingId, Long userId);

    List<BookingDto> getAllBookingsByBooker(String state, Long bookerId);

    List<BookingDto> getAllBookingsByOwner(String state, Long ownerId);
}
