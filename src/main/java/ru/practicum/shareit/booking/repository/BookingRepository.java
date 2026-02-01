package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long bookerId, Sort sort);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDate startBefore, LocalDate endAfter, Sort sort);

    List<Booking> findByBookerIdAndEndBefore(Long bookerId, LocalDate endBefore, Sort sort);

    List<Booking> findByBookerIdAndStartAfter(Long bookerId, LocalDate startAfter, Sort sort);

    List<Booking> findByBookerIdAndStatusEqualsIgnoreCase(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findByItemOwner(User itemOwner, Sort sort);

    List<Booking> findByItemOwnerId(Long itemOwnerId, Sort sort);

    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfter(Long itemOwnerId, LocalDate startBefore, LocalDate endAfter, Sort sort);

    List<Booking> findByItemOwnerIdAndEndBefore(Long itemOwnerId, LocalDate endBefore, Sort sort);

    List<Booking> findByItemOwnerIdAndStartAfter(Long itemOwnerId, LocalDate startAfter, Sort sort);

    List<Booking> findByItemOwnerIdAndStatusEqualsIgnoreCase(Long itemOwnerId, BookingStatus status, Sort sort);

    List<Booking> findByItemInAndStartLessThanEqualAndStatus(Collection<Item> items, LocalDate startIsLessThan, BookingStatus status, Sort sort);

    List<Booking> findByItemInAndStartAfterAndStatus(Collection<Item> items, LocalDate startAfter, BookingStatus status, Sort sort);

    boolean findByBookerIdAndItemIdAndEndBefore(Long bookerId, Long itemId, LocalDate endBefore);
}
