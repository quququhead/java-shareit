package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> findAllBookingsOfUser(long userId, BookingState state) {
        Map<String, Object> parameters = Map.of("state", state.name());
        return get("?state={state}", userId, parameters);
    }

    public ResponseEntity<Object> findBooking(long userId, long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> findAllBookingsForOwner(long userId, BookingState state) {
        Map<String, Object> parameters = Map.of("state", state.name());
        return get("/owner?state={state}", userId, parameters);
    }


    public ResponseEntity<Object> createBooking(long userId, NewBookingRequest bookingRequest) {
        return post("", userId, bookingRequest);
    }

    public ResponseEntity<Object> updateBooking(long userId, long bookingId, boolean confirmed) {
        Map<String, Object> parameters = Map.of("approved", confirmed);
        return patch("/" + bookingId + "?approved={approved}", userId, parameters, null);
    }
}
