package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.interfaces.BookingService;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public List<BookingDto> findAllBookingsOfUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam(required = false, defaultValue = "ALL") BookingState state) {
        return bookingService.findAllBookingsOfUser(userId, state);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable long bookingId) {
        return bookingService.findBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public List<BookingDto> findAllBookingsForOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @RequestParam(required = false, defaultValue = "ALL") BookingState state) {
        return bookingService.findAllBookingsForOwner(userId, state);
    }

    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestBody BookingRequest bookingRequest) {
        return bookingService.createBooking(userId, bookingRequest);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable long bookingId,
                                    @RequestParam boolean approved) {
        return bookingService.updateBooking(userId, bookingId, approved);
    }
}
