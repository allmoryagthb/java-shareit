package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    @SneakyThrows
    void addUserTest() {
        when(userService.addNewUser(userDtoInput))
                .thenReturn(userDtoOutput);

        mvc.perform(post("/users")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(userDtoInput))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(userDtoOutput)))
                .andExpect(jsonPath("$.id", is(userDtoOutput.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDtoOutput.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDtoOutput.getEmail()), String.class));
    }

    @Test
    @SneakyThrows
    void getAllUsersDtoTest() {
        when(userService.getAllUsersDto())
                .thenReturn(List.of(userDtoOutput));

        mvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(userDtoOutput))));
    }

    @Test
    @SneakyThrows
    void getUserByIdTest() {
        when(userService.getUserDtoById(any()))
                .thenReturn(userDtoOutput);

        mvc.perform(get("/users/%d".formatted(anyLong()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userDtoOutput)))
                .andExpect(jsonPath("$.id", is(userDtoOutput.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDtoOutput.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDtoOutput.getEmail()), String.class));
    }

    @Test
    @SneakyThrows
    void updateUserTest() {
        when(userService.updateUser(anyLong(), any()))
                .thenReturn(userDtoOutput);

        mvc.perform(patch("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(userDtoInput))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userDtoOutput)))
                .andExpect(jsonPath("$.id", is(userDtoOutput.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDtoOutput.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDtoOutput.getEmail()), String.class));

    }
}
