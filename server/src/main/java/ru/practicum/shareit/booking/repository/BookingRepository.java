package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long bookerId, Sort sort);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime startBefore, LocalDateTime endAfter, Sort sort);

    List<Booking> findByBookerIdAndEndBefore(Long bookerId, LocalDateTime endBefore, Sort sort);

    List<Booking> findByBookerIdAndStartAfter(Long bookerId, LocalDateTime startAfter, Sort sort);

    List<Booking> findByBookerIdAndStatusEqualsIgnoreCase(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findByItemOwnerId(Long itemOwnerId, Sort sort);

    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfter(Long itemOwnerId, LocalDateTime startBefore, LocalDateTime endAfter, Sort sort);

    List<Booking> findByItemOwnerIdAndEndBefore(Long itemOwnerId, LocalDateTime endBefore, Sort sort);

    List<Booking> findByItemOwnerIdAndStartAfter(Long itemOwnerId, LocalDateTime startAfter, Sort sort);

    List<Booking> findByItemOwnerIdAndStatusEqualsIgnoreCase(Long itemOwnerId, BookingStatus status, Sort sort);

    List<Booking> findByItemInAndStartLessThanEqualAndStatus(Collection<Item> items, LocalDateTime startIsLessThan, BookingStatus status, Sort sort);

    List<Booking> findByItemInAndStartAfterAndStatus(Collection<Item> items, LocalDateTime startAfter, BookingStatus status, Sort sort);

    Object findFirstByItemIdAndBookerIdAndEndBefore(Long itemId, Long bookerId, LocalDateTime endBefore);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatus(Long itemId, LocalDateTime startAfter, BookingStatus status, Sort sort);

    Optional<Booking> findFirstByItemIdAndEndBeforeAndStatus(Long itemId, LocalDateTime endBefore, BookingStatus status, Sort sort);
}
