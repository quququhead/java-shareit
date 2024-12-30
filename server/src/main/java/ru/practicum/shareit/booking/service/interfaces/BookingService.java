package ru.practicum.shareit.booking.service.interfaces;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    List<BookingDto> findAllBookingsOfUser(long userId, BookingState state);

    BookingDto findBooking(long userId, long bookingId);

    List<BookingDto> findAllBookingsForOwner(long userId, BookingState state);

    BookingDto createBooking(long userId, BookingRequest bookingDto);

    BookingDto updateBooking(long userId, long bookingId, boolean approved);
}
