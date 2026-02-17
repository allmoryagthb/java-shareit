package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutput;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"jdbc.url=jdbc:postgresql://localhost:5432/shareit"})
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ItemRequestServiceImplTest {
    private final EntityManager entityManager;
    private final ItemRequestService itemRequestService;
    private final UserService userService;

    @Test
    @Rollback
    void getUsersItemRequestsTest() {
        UserDto userDto = new UserDto("name", "email@mail.ru");
        ItemRequestDtoInput itemRequestDtoInput = new ItemRequestDtoInput("description");

        userDto.setId(userService.addNewUser(userDto).getId());
        ItemRequestDtoOutput result = itemRequestService.addUsersItemRequest(userDto.getId(), itemRequestDtoInput);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getDescription(), equalTo(itemRequestDtoInput.getDescription()));

        TypedQuery<ItemRequest> query = entityManager
                .createQuery("Select i from ItemRequest i where i.id = :req_id", ItemRequest.class);
        ItemRequest itemRequestResult = query.setParameter("req_id", result.getId())
                .getSingleResult();

        assertThat(itemRequestResult.getId(), notNullValue());
        assertThat(itemRequestResult.getId(), equalTo(result.getId()));
        assertThat(itemRequestResult.getDescription(), equalTo(result.getDescription()));
    }

    @Test
    @Rollback
    void getAllUsersItemRequestsTest() {
        UserDto userDto = new UserDto("name", "email@mail.ru");
        ItemRequestDtoInput itemRequestDtoInput1 = new ItemRequestDtoInput("description1");
        ItemRequestDtoInput itemRequestDtoInput2 = new ItemRequestDtoInput("description2");

        userDto.setId(userService.addNewUser(userDto).getId());
        ItemRequestDtoOutput result1 = itemRequestService.addUsersItemRequest(userDto.getId(), itemRequestDtoInput1);
        ItemRequestDtoOutput result2 = itemRequestService.addUsersItemRequest(userDto.getId(), itemRequestDtoInput2);

        assertThat(result1.getId(), notNullValue());
        assertThat(result1.getDescription(), equalTo(result1.getDescription()));

        assertThat(result2.getId(), notNullValue());
        assertThat(result2.getDescription(), equalTo(result2.getDescription()));

        TypedQuery<ItemRequest> query1 = entityManager
                .createQuery("Select i from ItemRequest i where i.id = :req_id", ItemRequest.class);
        ItemRequest itemRequestResult1 = query1.setParameter("req_id", result1.getId())
                .getSingleResult();

        assertThat(itemRequestResult1.getId(), notNullValue());
        assertThat(itemRequestResult1.getId(), equalTo(result1.getId()));
        assertThat(itemRequestResult1.getDescription(), equalTo(result1.getDescription()));

        TypedQuery<ItemRequest> query2 = entityManager
                .createQuery("Select i from ItemRequest i where i.id = :req_id", ItemRequest.class);
        ItemRequest itemRequestResult2 = query2.setParameter("req_id", result2.getId())
                .getSingleResult();

        assertThat(itemRequestResult2.getId(), notNullValue());
        assertThat(itemRequestResult2.getId(), equalTo(result2.getId()));
        assertThat(itemRequestResult2.getDescription(), equalTo(result2.getDescription()));
    }

    @Test
    @Rollback
    void addUsersItemRequestTest() {
        UserDto userDto = new UserDto("name", "email@mail.ru");
        ItemRequestDtoInput itemRequestDtoInput = new ItemRequestDtoInput("description");

        userDto.setId(userService.addNewUser(userDto).getId());
        ItemRequestDtoOutput result = itemRequestService.addUsersItemRequest(userDto.getId(), itemRequestDtoInput);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getId(), equalTo(1L));
        assertThat(result.getDescription(), equalTo(itemRequestDtoInput.getDescription()));
    }
}
