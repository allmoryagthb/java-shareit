package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class BookingMapperTest {

    @Test
    void jpaToDtoTest() {
        Booking booking = new Booking(1L, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusHours(1), new Item(), new User(), BookingStatus.APPROVED);

        BookingDto result = BookingMapper.jpaToDto(booking);

        assertThat(result.getId(), equalTo(booking.getId()));
        assertThat(result.getStart(), equalTo(booking.getStart()));
        assertThat(result.getEnd(), equalTo(booking.getEnd()));
        assertThat(result.getItem(), equalTo(booking.getItem()));
        assertThat(result.getBooker(), equalTo(booking.getBooker()));
        assertThat(result.getStatus(), equalTo(booking.getStatus()));
    }

    @Test
    void jpaToDtoOutputTest() {
        Booking booking = new Booking(1L, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusHours(1), new Item(), new User(), BookingStatus.APPROVED);

        BookingDtoOutput result = BookingMapper.jpaToDtoOutput(booking);

        assertThat(result.getId(), equalTo(booking.getId()));
        assertThat(result.getStart(), equalTo(booking.getStart()));
        assertThat(result.getEnd(), equalTo(booking.getEnd()));
        assertThat(result.getStatus(), equalTo(booking.getStatus()));
    }

    @Test
    void dtoInputToJpaTest() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusHours(1), 1L);
        Booking result = BookingMapper.dtoInputToJpa(bookingDtoInput);

        assertThat(result.getStart(), equalTo(bookingDtoInput.getStart()));
        assertThat(result.getEnd(), equalTo(bookingDtoInput.getEnd()));
    }
}
