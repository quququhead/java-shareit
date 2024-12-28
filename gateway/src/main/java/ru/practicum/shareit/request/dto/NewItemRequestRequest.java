package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewItemRequestRequest {
    @NotBlank(message = "Description cannot be null, empty or blank!")
    private String description;
}
