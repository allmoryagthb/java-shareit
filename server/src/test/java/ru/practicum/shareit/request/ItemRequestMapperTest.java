package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutput;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ItemRequestMapperTest {

    @Test
    void jpaToDtoOutputTest() {
        ItemRequest itemRequest = new ItemRequest(1L, "desc");
        ItemRequestDtoOutput result = ItemRequestMapper.jpaToDtoOutput(itemRequest);

        assertThat(result.getId(), equalTo(itemRequest.getId()));
        assertThat(result.getDescription(), equalTo(itemRequest.getDescription()));
    }

    @Test
    void dtoInputToJpaTest() {
        ItemRequestDtoInput itemRequestDtoInput = new ItemRequestDtoInput(1L, "desc");
        ItemRequest result = ItemRequestMapper.dtoInputToJpa(itemRequestDtoInput);

        assertThat(result.getId(), equalTo(itemRequestDtoInput.getId()));
        assertThat(result.getDescription(), equalTo(itemRequestDtoInput.getDescription()));
    }
}
