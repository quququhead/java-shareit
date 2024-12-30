package ru.practicum.shareit.booking.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdAndStatusOrderByStart(long userId, BookingStatus status);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStart(long userId, LocalDateTime time1, LocalDateTime time2);

    List<Booking> findByBookerIdAndStatusOrBookerIdAndStatusOrderByStart(long userId1, BookingStatus status1, long userId2, BookingStatus status2);

    List<Booking> findByBookerIdAndEndBeforeOrderByStart(long userId, LocalDateTime time);

    List<Booking> findByBookerIdAndStartAfterOrderByStart(long userId, LocalDateTime time);

    List<Booking> findByBookerIdOrderByStart(long userId);

    List<Booking> findByItem_OwnerIdAndStatusOrderByStart(long userId,  BookingStatus status);

    List<Booking> findByItem_OwnerIdAndStartBeforeAndEndAfterOrderByStart(long userId, LocalDateTime time1, LocalDateTime time2);

    List<Booking> findByItem_OwnerIdAndStatusOrItem_OwnerIdAndStatusOrderByStart(long userId1, BookingStatus status1, long userId2, BookingStatus status2);

    List<Booking> findByItem_OwnerIdAndEndBeforeOrderByStart(long userId, LocalDateTime time);

    List<Booking> findByItem_OwnerIdAndStartAfterOrderByStart(long userId, LocalDateTime time);

    List<Booking> findByItem_OwnerIdOrderByStart(long userId);

    Optional<Booking> findByBookerIdAndItemIdAndStatusAndEndBefore(long userId, long itemId, BookingStatus status, LocalDateTime time);

    Booking findFirstByItemIdAndStatusAndStartAfterOrderByStartDesc(long itemId, BookingStatus status, LocalDateTime time);

    Booking findFirstByItemIdAndStatusAndStartAfterOrderByStart(long itemId, BookingStatus status, LocalDateTime time);
}
