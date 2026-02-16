package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    private UserDto userDtoInput;
    private UserDto userDtoOutput;

    @BeforeEach
    void setUp() {
        this.userDtoInput = new UserDto(
                "user_name",
                "useremail@mail.ru"
        );
        this.userDtoOutput = new UserDto(
                1L,
                "user_name",
                "useremail@mail.ru"
        );
    }

    @Test
    void addUserTest() {
        Mockito.when(userService.addNewUser(userDtoInput))
                .thenReturn(userDtoOutput);
    }
}
