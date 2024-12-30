package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewCommentRequest {
    @NotBlank(message = "Text cannot be null, empty or blank!")
    private String text;
}
