package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewUserRequest {
    @NotBlank(message = "Name cannot be null, empty or blank!")
    private String name;

    @NotBlank(message = "Email cannot be null, empty or blank!")
    @Email(message = "Email should be valid!")
    private String email;
}
