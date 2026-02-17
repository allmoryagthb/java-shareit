package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserJsonTest {
    private final JacksonTester<UserDto> jsonDto;

    @Test
    @SneakyThrows
    void dtoInputTest() {
        UserDto userDto = new UserDto(
                1L,
                "user_name",
                "mail@mail.ru"
        );

        JsonContent<UserDto> result = jsonDto.write(userDto);
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo("user_name");
        assertThat(result).extractingJsonPathStringValue("$.email")
                .isEqualTo("mail@mail.ru");
    }
}
