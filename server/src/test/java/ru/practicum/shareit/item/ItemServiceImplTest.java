package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"jdbc.url=jdbc:postgresql://localhost:5432/shareit"})
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ItemServiceImplTest {
    private final ItemService itemService;
    private final UserService userService;
    private final EntityManager entityManager;

    @Test
    @Rollback
    void getUserItemsTest() {
        UserDto userDto = userService.addNewUser(new UserDto("testUser", "email@mail.ru"));
        ItemDto itemDto1 = itemService.addItem(userDto.getId(), new ItemDto("item1", "itemDescription1", true));
        ItemDto itemDto2 = itemService.addItem(userDto.getId(), new ItemDto("item2", "itemDescription2", true));
        List<ItemDtoFull> result = itemService.getUserItems(userDto.getId()).stream().toList();

        assertThat(result.size(), equalTo(2));
        assertThat(result.getFirst().getId(), equalTo(itemDto1.getId()));
        assertThat(result.getFirst().getDescription(), equalTo(itemDto1.getDescription()));
        assertThat(result.getLast().getId(), equalTo(itemDto2.getId()));
        assertThat(result.getLast().getDescription(), equalTo(itemDto2.getDescription()));

        TypedQuery<Item> query = entityManager
                .createQuery("Select i from Item i where i.id = :id", Item.class);
        Item itemResult1 = query.setParameter("id", result.getFirst().getId())
                .getSingleResult();
        Item itemResult2 = query.setParameter("id", result.getLast().getId())
                .getSingleResult();

        assertThat(result.getFirst().getId(), equalTo(itemResult1.getId()));
        assertThat(result.getFirst().getDescription(), equalTo(itemResult1.getDescription()));
        assertThat(result.getLast().getId(), equalTo(itemResult2.getId()));
        assertThat(result.getLast().getDescription(), equalTo(itemResult2.getDescription()));
    }

    @Test
    @Rollback
    void getAvailableItemsDtoByTextTest() {
        UserDto userDto = userService.addNewUser(new UserDto("testUser", "email@mail.ru"));
        ItemDto itemDto1 = itemService.addItem(userDto.getId(), new ItemDto("item1", "itemDescription1", true));
        itemService.addItem(userDto.getId(), new ItemDto("item2", "itemDescription2", false));

        List<ItemDto> result = itemService.getAvailableItemsDtoByText("itemDescription").stream().toList();
        assertThat(result.size(), equalTo(1));
        assertThat(result.getFirst().getId(), equalTo(itemDto1.getId()));
        assertThat(result.getFirst().getDescription(), equalTo(itemDto1.getDescription()));

        TypedQuery<Item> query = entityManager
                .createQuery("Select i from Item i where i.id = :id", Item.class);
        Item itemResult1 = query.setParameter("id", result.getFirst().getId())
                .getSingleResult();
        Item itemResult2 = query.setParameter("id", result.getLast().getId())
                .getSingleResult();

        assertThat(result.getFirst().getId(), equalTo(itemResult1.getId()));
        assertThat(result.getFirst().getDescription(), equalTo(itemResult1.getDescription()));
        assertThat(result.getLast().getId(), equalTo(itemResult2.getId()));
        assertThat(result.getLast().getDescription(), equalTo(itemResult2.getDescription()));
    }

    @Test
    @Rollback
    void addItemTest() {
        UserDto userDto = userService.addNewUser(new UserDto("testUser", "email@mail.ru"));
        ItemDto itemDto = itemService
                .addItem(userDto.getId(), new ItemDto("item1", "itemDescription1", true));

        TypedQuery<Item> query = entityManager
                .createQuery("Select i from Item i where i.id = :id", Item.class);
        Item itemResult = query.setParameter("id", itemDto.getId())
                .getSingleResult();

        assertThat(itemDto.getId(), equalTo(itemResult.getId()));
        assertThat(itemDto.getDescription(), equalTo(itemResult.getDescription()));
    }

    @Test
    @Rollback
    void updateItemTest() {
        UserDto userDto = userService.addNewUser(new UserDto("testUser", "email@mail.ru"));
        ItemDto itemAdded = itemService
                .addItem(userDto.getId(), new ItemDto("item1", "itemDescription1", true));
        ItemDto itemDtoUpd = itemService
                .updateItem(userDto.getId(), itemAdded.getId(),
                        new ItemDto("item1upd", "itemDescription1upd", true));

        TypedQuery<Item> query = entityManager
                .createQuery("Select i from Item i where i.id = :id", Item.class);
        Item itemResult = query.setParameter("id", itemDtoUpd.getId())
                .getSingleResult();

        assertThat(itemDtoUpd.getId(), equalTo(itemResult.getId()));
        assertThat(itemDtoUpd.getName(), equalTo(itemResult.getName()));
        assertThat(itemDtoUpd.getDescription(), equalTo(itemResult.getDescription()));
    }
}
