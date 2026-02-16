package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"jdbc.url=jdbc:postgresql://localhost:5432/shareit"})
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserServiceImplTest {
    private final EntityManager entityManager;
    private final UserService userService;

    @Test
    void saveUserTest() {
        UserDto userDto = new UserDto(
                "name", "email@mail.ru"
        );

        userService.addNewUser(userDto);

        TypedQuery<User> query = entityManager
                .createQuery("Select u from User u where u.email = :email", User.class);
        User result = query.setParameter("email", userDto.getEmail())
                .getSingleResult();

        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(userDto.getName()));
        assertThat(result.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void getAllUsersTest() {
        UserDto userDto1 = new UserDto(
                "name1", "email1@mail.ru"
        );
        UserDto userDto2 = new UserDto(
                "name2", "email2@mail.ru"
        );
        userDto1.setId(userService.addNewUser(userDto1).getId());
        userDto2.setId(userService.addNewUser(userDto2).getId());

        Collection<UserDto> result = userService.getAllUsersDto();

        assertThat(result.size(), equalTo(2));
        assertThat(List.copyOf(result).getFirst(), equalTo(userDto1));
        assertThat(List.copyOf(result).getLast(), equalTo(userDto2));
    }

    @Test
    void getUserDtoByIdTest() {
        UserDto userDto = new UserDto(
                "name1", "email1@mail.ru"
        );
        userDto.setId(userService.addNewUser(userDto).getId());
        UserDto result = userService.getUserDtoById(userDto.getId());

        assertThat(result.getId(), notNullValue());
        assertThat(result.getId(), equalTo(userDto.getId()));
        assertThat(result.getName(), equalTo(userDto.getName()));
        assertThat(result.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void updateUserTest() {
        UserDto userDto = new UserDto(
                "name1", "email1@mail.ru"
        );
        UserDto userDtoUpd = new UserDto(
                "name1Upd", "email1Upd@mail.ru"
        );
        userDto.setId(userService.addNewUser(userDto).getId());
        userService.updateUser(userDto.getId(), userDtoUpd);

        UserDto result = userService.getUserDtoById(userDto.getId());
        assertThat(result.getId(), notNullValue());
        assertThat(result.getId(), equalTo(userDto.getId()));
        assertThat(result.getName(), equalTo(userDtoUpd.getName()));
        assertThat(result.getEmail(), equalTo(userDtoUpd.getEmail()));
    }
}
