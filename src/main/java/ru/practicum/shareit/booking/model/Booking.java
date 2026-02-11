package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "start_date")
    private LocalDateTime start;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "end_date")
    private LocalDateTime end;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "booker_id", referencedColumnName = "id")
    private User booker;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;
}
