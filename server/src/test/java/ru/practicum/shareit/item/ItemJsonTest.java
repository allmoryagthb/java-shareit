package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutput;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemJsonTest {
    private final JacksonTester<ItemDto> jsonDto;
    private final JacksonTester<ItemDtoFull> jsonDtoFull;

    @Test
    @SneakyThrows
    void dtoTest() {
        ItemDto itemDto = new ItemDto(
                123L,
                "name_test",
                "description_test",
                true,
                321L
        );

        JsonContent<ItemDto> result = jsonDto.write(itemDto);
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(123);
        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo("name_test");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("description_test");
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId")
                .isEqualTo(321);
    }

    @Test
    @SneakyThrows
    void dtoFullTest() {
        ItemDtoFull itemDtoFull = new ItemDtoFull(
                123L,
                "name_test",
                "description_test",
                false,
                new ItemRequestDtoOutput(),
                new BookingDto(),
                new BookingDto(),
                Collections.emptyList()
        );

        JsonContent<ItemDtoFull> result = jsonDtoFull.write(itemDtoFull);
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(123);
        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo("name_test");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("description_test");
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(false);
        assertThat(result).doesNotHaveEmptyJsonPathValue("$.itemRequestDtoOutput");
        assertThat(result).doesNotHaveEmptyJsonPathValue("$.lastBooking");
        assertThat(result).doesNotHaveEmptyJsonPathValue("$.nextBooking");
    }
}
