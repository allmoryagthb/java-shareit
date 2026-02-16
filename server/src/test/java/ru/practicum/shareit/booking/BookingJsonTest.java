package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingJsonTest {
    private final JacksonTester<BookingDto> jsonDto;
    private final JacksonTester<BookingDtoInput> jsonDtoInput;
    private final JacksonTester<BookingDtoOutput> jsonDtoOutput;

    @Test
    @SneakyThrows
    void dtoTest() {
        BookingDto bookingDto = new BookingDto(
                123L,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1),
                new Item(),
                new User(),
                BookingStatus.APPROVED
        );

        JsonContent<BookingDto> result = jsonDto.write(bookingDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(123);
        assertThat(result).doesNotHaveEmptyJsonPathValue("$.start");
        assertThat(result).doesNotHaveEmptyJsonPathValue("$.end");
        assertThat(result).hasJsonPath("$.item");
        assertThat(result).hasJsonPath("$.booker");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
    }

    @Test
    @SneakyThrows
    void dtoInputTest() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput(
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1),
                123L
        );

        JsonContent<BookingDtoInput> result = jsonDtoInput.write(bookingDtoInput);
        assertThat(result).doesNotHaveEmptyJsonPathValue("$.start");
        assertThat(result).doesNotHaveEmptyJsonPathValue("$.end");
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(123);
    }

    @Test
    @SneakyThrows
    void dtoOutputTest() {
        BookingDtoOutput bookingDtoOutput = new BookingDtoOutput(
                123L,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1),
                new ItemDto(),
                new UserDto(),
                BookingStatus.APPROVED
        );

        JsonContent<BookingDtoOutput> result = jsonDtoOutput.write(bookingDtoOutput);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(123);
        assertThat(result).doesNotHaveEmptyJsonPathValue("$.start");
        assertThat(result).doesNotHaveEmptyJsonPathValue("$.end");
        assertThat(result).hasJsonPath("$.item");
        assertThat(result).hasJsonPath("$.booker");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
    }
}
