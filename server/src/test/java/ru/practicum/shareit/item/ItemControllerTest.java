package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @MockBean
    private ItemService itemService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    private ItemDto itemDtoInput;
    private ItemDto itemDtoOutput;

    @BeforeEach
    void setUp() {
        this.itemDtoInput = new ItemDto(
                "test_name",
                "test_desc",
                true);
        this.itemDtoOutput = new ItemDto(
                1L,
                "test_name",
                "test_desc",
                true);
    }

    @Test
    @SneakyThrows
    void addItemTest() {
        when(itemService.addItem(anyLong(), any())).thenReturn(itemDtoOutput);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDtoInput))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(itemDtoOutput)))
                .andExpect(jsonPath("$.id", is(itemDtoOutput.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoOutput.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDtoOutput.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemDtoOutput.getAvailable()), Boolean.class));
    }

    @Test
    @SneakyThrows
    void getAllItemsTest() {
        ItemDtoFull itemDtoFull = ItemMapper.dtoToDtoFull(itemDtoOutput);
        when(itemService.getUserItems(anyLong())).thenReturn(List.of(itemDtoFull));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemDtoFull))));
    }

    @Test
    @SneakyThrows
    void updateItemTest() {
        when(itemService.updateItem(anyLong(), anyLong(), any())).thenReturn(itemDtoOutput);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDtoInput))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDtoOutput)))
                .andExpect(jsonPath("$.id", is(itemDtoOutput.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoOutput.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDtoOutput.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemDtoOutput.getAvailable()), Boolean.class));
    }
}
