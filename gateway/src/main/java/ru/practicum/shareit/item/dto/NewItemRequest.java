package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewItemRequest {
    @NotBlank(message = "Name cannot be null, empty or blank!")
    private String name;

    @NotBlank(message = "Description cannot be null, empty or blank!")
    private String description;

    @NotNull(message = "Available cannot be null!")
    private Boolean available;

    private Long requestId;
}
