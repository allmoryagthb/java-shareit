package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;

public class BookingMapper {

    public static BookingDto jpaToDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus());
    }

    public static Booking dtoToJpa(BookingDto bookingDto) {
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getItem(),
                bookingDto.getBooker(),
                bookingDto.getStatus());
    }

    public static BookingDtoOutput jpaToDtoOutput(Booking booking) {
        return new BookingDtoOutput(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                ItemMapper.jpaToDto(booking.getItem()),
                ru.practicum.shareit.user.mapper.UserMapper.jpaToDto(booking.getBooker()),
                booking.getStatus());
    }

    public static Booking dtoInputToJpa(BookingDtoInput bookingDtoInput) {
        return new Booking(
                null,
                bookingDtoInput.getStart(),
                bookingDtoInput.getEnd(),
                null,
                null,
                null);
    }
}
