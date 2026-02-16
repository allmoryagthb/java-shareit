package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UserMapperTest {

    @Test
    void dtoToJpaTest() {
        UserDto userDto = new UserDto(
                1L, "userDtoName", "userDtoDesc");
        User result = UserMapper.dtoToJpa(userDto);

        assertThat(result.getId(), equalTo(userDto.getId()));
        assertThat(result.getName(), equalTo(userDto.getName()));
        assertThat(result.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void jpaToDtoTest() {
        User user = new User(
                1L, "userName", "userDescription");
        UserDto result = UserMapper.jpaToDto(user);

        assertThat(result.getId(), equalTo(user.getId()));
        assertThat(result.getName(), equalTo(user.getName()));
        assertThat(result.getEmail(), equalTo(user.getEmail()));
    }
}
