package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"jdbc.url=jdbc:postgresql://localhost:5432/shareit"})
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BookingServiceImplTest {
    private final EntityManager entityManager;
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    @Test
    @Rollback
    void addBookingTest() {
        UserDto userDtoOwner = userService.addNewUser(new UserDto("userName", "mail521@mail.ru"));
        UserDto userDtoBooker = userService.addNewUser(new UserDto("userNameBooker", "mail21424@mail.ru"));
        ItemDto itemDto = itemService.addItem(userDtoOwner.getId(), new ItemDto("itemName1", "itemDesc1", true));
        BookingDto bookingDtoResult = bookingService.addBooking(
                new BookingDtoInput(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(10), itemDto.getId()),
                userDtoBooker.getId());

        TypedQuery<Booking> query = entityManager
                .createQuery("Select i from Booking i where i.id = :book_id", Booking.class);
        Booking queryResult = query.setParameter("book_id", bookingDtoResult.getId())
                .getSingleResult();

        assertThat(bookingDtoResult.getId(), notNullValue());
        assertThat(queryResult.getId(), equalTo(bookingDtoResult.getId()));
        assertThat(queryResult.getItem().getId(), equalTo(bookingDtoResult.getItem().getId()));
        assertThat(queryResult.getBooker().getId(), equalTo(bookingDtoResult.getBooker().getId()));
    }

    @Test
    @Rollback
    void updateBookingStatusByBookerTest() {
        UserDto userDtoOwner = userService.addNewUser(new UserDto("userName", "mail521@mail.ru"));
        UserDto userDtoBooker = userService.addNewUser(new UserDto("userNameBooker", "mail32142@mail.ru"));
        ItemDto itemDto = itemService.addItem(userDtoOwner.getId(), new ItemDto("itemName2", "itemDesc2", true));
        BookingDto bookingDto = bookingService.addBooking(
                new BookingDtoInput(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(10), itemDto.getId()),
                userDtoBooker.getId());

        BookingDtoOutput bookingDtoOutput = bookingService
                .updateBookingStatus(bookingDto.getId(), userDtoBooker.getId(), false);

        TypedQuery<Booking> query = entityManager
                .createQuery("Select i from Booking i where i.id = :book_id", Booking.class);
        Booking queryResult = query.setParameter("book_id", bookingDtoOutput.getId())
                .getSingleResult();

        assertThat(queryResult.getId(), notNullValue());
        assertThat(queryResult.getId(), equalTo(bookingDtoOutput.getId()));
        assertThat(queryResult.getItem().getId(), equalTo(bookingDtoOutput.getItem().getId()));
        assertThat(queryResult.getBooker().getId(), equalTo(bookingDtoOutput.getBooker().getId()));
        assertThat(bookingDtoOutput.getStatus(), equalTo(BookingStatus.CANCELED));
    }

    @Test
    @Rollback
    void rejectBookingStatusByItemOwnerTest() {
        UserDto userDtoOwner = userService.addNewUser(new UserDto("userName", "mail1123@mail.ru"));
        UserDto userDtoBooker = userService.addNewUser(new UserDto("userNameBooker", "mail3212@mail.ru"));
        ItemDto itemDto = itemService.addItem(userDtoOwner.getId(), new ItemDto("itemName3", "itemDesc3", true));
        BookingDto bookingDto = bookingService.addBooking(
                new BookingDtoInput(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(10), itemDto.getId()),
                userDtoBooker.getId());

        BookingDtoOutput bookingDtoOutput = bookingService
                .updateBookingStatus(bookingDto.getId(), userDtoOwner.getId(), false);

        TypedQuery<Booking> query = entityManager
                .createQuery("Select i from Booking i where i.id = :book_id", Booking.class);
        Booking queryResult = query.setParameter("book_id", bookingDtoOutput.getId())
                .getSingleResult();

        assertThat(queryResult.getId(), notNullValue());
        assertThat(queryResult.getId(), equalTo(bookingDtoOutput.getId()));
        assertThat(queryResult.getItem().getId(), equalTo(bookingDtoOutput.getItem().getId()));
        assertThat(queryResult.getBooker().getId(), equalTo(bookingDtoOutput.getBooker().getId()));
        assertThat(bookingDtoOutput.getStatus(), equalTo(BookingStatus.REJECTED));
    }

    @Test
    @Rollback
    void approveBookingStatusByItemOwnerTest() {
        UserDto userDtoOwner = userService.addNewUser(new UserDto("userName", "mail1232@mail.ru"));
        UserDto userDtoBooker = userService.addNewUser(new UserDto("userNameBooker", "mail2123@mail.ru"));
        ItemDto itemDto = itemService.addItem(userDtoOwner.getId(), new ItemDto("itemName4", "itemDesc4", true));
        BookingDto bookingDto = bookingService.addBooking(
                new BookingDtoInput(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(10), itemDto.getId()),
                userDtoBooker.getId());

        BookingDtoOutput bookingDtoOutput = bookingService
                .updateBookingStatus(bookingDto.getId(), userDtoOwner.getId(), true);

        TypedQuery<Booking> query = entityManager
                .createQuery("Select i from Booking i where i.id = :book_id", Booking.class);
        Booking queryResult = query.setParameter("book_id", bookingDtoOutput.getId())
                .getSingleResult();

        assertThat(queryResult.getId(), notNullValue());
        assertThat(queryResult.getId(), equalTo(bookingDtoOutput.getId()));
        assertThat(queryResult.getItem().getId(), equalTo(bookingDtoOutput.getItem().getId()));
        assertThat(queryResult.getBooker().getId(), equalTo(bookingDtoOutput.getBooker().getId()));
        assertThat(bookingDtoOutput.getStatus(), equalTo(BookingStatus.APPROVED));
    }

    @Test
    @Rollback
    void getAllBookingsByBookerTest() {
        UserDto userDtoOwner = userService.addNewUser(new UserDto("userName", "mail135@mail.ru"));
        UserDto userDtoBooker = userService.addNewUser(new UserDto("userNameBooker", "mail2512@mail.ru"));
        ItemDto itemDto1 = itemService.addItem(userDtoOwner.getId(), new ItemDto("itemName", "itemDesc", true));
        ItemDto itemDto2 = itemService.addItem(userDtoOwner.getId(), new ItemDto("itemName", "itemDesc", true));
        ItemDto itemDto3 = itemService.addItem(userDtoOwner.getId(), new ItemDto("itemName", "itemDesc", true));

        bookingService.addBooking(
                new BookingDtoInput(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(10), itemDto1.getId()),
                userDtoBooker.getId());
        bookingService.addBooking(
                new BookingDtoInput(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(10), itemDto2.getId()),
                userDtoBooker.getId());
        bookingService.addBooking(
                new BookingDtoInput(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(10), itemDto3.getId()),
                userDtoBooker.getId());

        List<BookingDto> resultList = bookingService.getAllBookingsByBooker("ALL", userDtoBooker.getId());

        assertThat(resultList.size(), equalTo(3));
    }

    @Test
    @Rollback
    void getAllBookingsByOwnerTest() {
        UserDto userDtoOwner = userService.addNewUser(new UserDto("userName", "mail16234@mail.ru"));
        UserDto userDtoBooker = userService.addNewUser(new UserDto("userNameBooker", "mail1242@mail.ru"));
        itemService.addItem(userDtoOwner.getId(), new ItemDto("itemName", "itemDesc", true));

        ItemDto itemDto1 = itemService.addItem(userDtoOwner.getId(), new ItemDto("itemName", "itemDesc", true));
        ItemDto itemDto2 = itemService.addItem(userDtoOwner.getId(), new ItemDto("itemName", "itemDesc", true));
        ItemDto itemDto3 = itemService.addItem(userDtoOwner.getId(), new ItemDto("itemName", "itemDesc", true));

        bookingService.addBooking(
                new BookingDtoInput(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(10), itemDto1.getId()),
                userDtoBooker.getId());
        bookingService.addBooking(
                new BookingDtoInput(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(10), itemDto2.getId()),
                userDtoBooker.getId());
        bookingService.addBooking(
                new BookingDtoInput(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(10), itemDto3.getId()),
                userDtoBooker.getId());

        List<BookingDto> resultList = bookingService.getAllBookingsByOwner("ALL", userDtoOwner.getId());

        assertThat(resultList.size(), equalTo(3));
    }
}
