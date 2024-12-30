package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> findAllBookingsOfUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                        @RequestParam(name = "state", required = false, defaultValue = "all") String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("FindAllBookingsOfUser userId={}, state={}", userId, stateParam);
        return bookingClient.findAllBookingsOfUser(userId, state);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @PathVariable long bookingId) {
        log.info("FindBooking userId={}, bookingId={}", userId, bookingId);
        return bookingClient.findBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllBookingsForOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                          @RequestParam(name = "state", required = false, defaultValue = "ALL") String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("FindAllBookingsForOwner userId={}, state={}", userId, stateParam);
        return bookingClient.findAllBookingsForOwner(userId, state);
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @Valid @RequestBody NewBookingRequest bookingRequest) {
        log.info("CreateBooking {}, userId={}", bookingRequest, userId);
        if (bookingRequest.getStart().isEqual(bookingRequest.getEnd()) || bookingRequest.getStart().isAfter(bookingRequest.getEnd())) {
            throw new ValidationException("Start and end cannot be the same," +
                    " and start must be before end");
        }
        return bookingClient.createBooking(userId, bookingRequest);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @PathVariable long bookingId,
                                                @RequestParam boolean approved) {
        log.info("UpdateBooking bookingId={}, userId={}, approved={}", bookingId, userId, approved);
        return bookingClient.updateBooking(userId, bookingId, approved);
    }
}
