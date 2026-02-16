package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutput;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestJsonTest {
    private final JacksonTester<ItemRequestDtoInput> jsonDtoInput;
    private final JacksonTester<ItemRequestDtoOutput> jsonDtoOutput;

    @Test
    @SneakyThrows
    void dtoInputTest() {
        ItemRequestDtoInput itemRequestDtoInput = new ItemRequestDtoInput(
                1L,
                "description_test"
        );

        JsonContent<ItemRequestDtoInput> result = jsonDtoInput.write(itemRequestDtoInput);
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("description_test");
    }

    @Test
    @SneakyThrows
    void dtoOutputTest() {
        LocalDateTime startTime = LocalDateTime.now();
        ItemRequestDtoOutput itemRequestDtoOutput = new ItemRequestDtoOutput(
                1L,
                "description_test",
                2L,
                startTime
        );

        JsonContent<ItemRequestDtoOutput> result = jsonDtoOutput.write(itemRequestDtoOutput);
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("description_test");
        assertThat(result).extractingJsonPathNumberValue("$.requesterId")
                .isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(startTime.toString());
    }
}
