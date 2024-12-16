package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewBookingRequest {

    @NotNull(message = "ItemId cannot be null!")
    private Long itemId;

    @NotNull(message = "Start cannot be null!")
    @FutureOrPresent(message = "Start cannot be in the past!")
    private LocalDateTime start;

    @NotNull(message = "End cannot be null!")
    @FutureOrPresent(message = "End cannot be in the past!")
    private LocalDateTime end;
}
