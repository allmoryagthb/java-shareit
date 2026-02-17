package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ItemMapperTest {

    @Test
    void dtoToJpaTest() {
        ItemDto itemDto = new ItemDto(
                1L,
                "nameTest",
                "descTest",
                true,
                123L);

        Item result = ItemMapper.dtoToJpa(itemDto);

        assertThat(result.getId(), equalTo(itemDto.getId()));
        assertThat(result.getName(), equalTo(itemDto.getName()));
        assertThat(result.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(result.getAvailable(), equalTo(itemDto.getAvailable()));
    }

    @Test
    void jpaToDtoTest() {
        Item item = new Item(1L, "name", "desc", true, new User(), new ItemRequest());
        ItemDto result = ItemMapper.jpaToDto(item);

        assertThat(result.getId(), equalTo(item.getId()));
        assertThat(result.getName(), equalTo(item.getName()));
        assertThat(result.getDescription(), equalTo(item.getDescription()));
        assertThat(result.getAvailable(), equalTo(item.getAvailable()));
        assertThat(result.getRequestId(), equalTo(item.getRequest().getId()));
    }

    @Test
    void jpaToDtoFullTest() {
        Item item = new Item(1L, "name", "desc", true, new User(), new ItemRequest());
        ItemDtoFull result = ItemMapper.jpaToDtoFull(item);

        assertThat(result.getId(), equalTo(item.getId()));
        assertThat(result.getName(), equalTo(item.getName()));
        assertThat(result.getDescription(), equalTo(item.getDescription()));
        assertThat(result.getAvailable(), equalTo(item.getAvailable()));
    }

    @Test
    void dtoToDtoFullTest() {
        ItemDto itemDto = new ItemDto(
                1L,
                "nameTest",
                "descTest",
                true,
                123L);

        ItemDtoFull result = ItemMapper.dtoToDtoFull(itemDto);

        assertThat(result.getId(), equalTo(itemDto.getId()));
        assertThat(result.getName(), equalTo(itemDto.getName()));
        assertThat(result.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(result.getAvailable(), equalTo(itemDto.getAvailable()));
    }
}
