package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutput;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {
    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    private ItemRequestDtoInput itemRequestDtoInput;
    private ItemRequestDtoOutput itemRequestDtoOutput;

    @BeforeEach
    void setUp() {
        this.itemRequestDtoInput = new ItemRequestDtoInput(
                "description"
        );
        this.itemRequestDtoOutput = new ItemRequestDtoOutput(
                1L,
                "description",
                1L,
                LocalDateTime.now()
        );
    }

    @Test
    @SneakyThrows
    void getUsersItemRequestsTest() {
        when(itemRequestService.getUsersItemRequests(anyLong()))
                .thenReturn(List.of(itemRequestDtoOutput));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemRequestDtoOutput))));
    }

    @Test
    @SneakyThrows
    void getAllUsersItemRequestsTest() {
        when(itemRequestService.getAllUsersItemRequests())
                .thenReturn(List.of(itemRequestDtoOutput));

        mvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemRequestDtoOutput))));
    }

    @Test
    @SneakyThrows
    void addUsersItemRequestsTest() {
        when(itemRequestService.addUsersItemRequest(1L, itemRequestDtoInput))
                .thenReturn(itemRequestDtoOutput);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequestDtoInput))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDtoOutput)))
                .andExpect(jsonPath("$.id", is(itemRequestDtoOutput.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDtoOutput.getDescription()), String.class))
                .andExpect(jsonPath("$.requester", is(itemRequestDtoOutput.getRequester()), Long.class));
    }
}
