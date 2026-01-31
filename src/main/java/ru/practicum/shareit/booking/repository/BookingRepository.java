package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long bookerId, Sort sort);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDate startBefore, LocalDate endAfter, Sort sort);

    List<Booking> findByBookerIdAndEndBefore(Long bookerId, LocalDate endBefore, Sort sort);

    List<Booking> findByBookerIdAndStartAfter(Long bookerId, LocalDate startAfter, Sort sort);

    List<Booking> findByBookerIdAndBookingStatusEqualsIgnoreCase(Long bookerId, BookingStatus bookingStatus, Sort sort);

    List<Booking> findByItemOwner(User itemOwner, Sort sort);

    List<Booking> findByItemOwnerId(Long itemOwnerId, Sort sort);

    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfter(Long itemOwnerId, LocalDate startBefore, LocalDate endAfter, Sort sort);

    List<Booking> findByItemOwnerIdAndEndBefore(Long itemOwnerId, LocalDate endBefore, Sort sort);

    List<Booking> findByItemOwnerIdAndStartAfter(Long itemOwnerId, LocalDate startAfter, Sort sort);

    List<Booking> findByItemOwnerIdAndBookingStatusEqualsIgnoreCase(Long itemOwnerId, BookingStatus bookingStatus, Sort sort);

    List<Booking> findByItemInAndStartLessThanEqualAndBookingStatus(Collection<Item> items, LocalDate startIsLessThan, BookingStatus bookingStatus, Sort sort);

    List<Booking> findByItemInAndStartAfterAndBookingStatus(Collection<Item> items, LocalDate startAfter, BookingStatus bookingStatus, Sort sort);
}
