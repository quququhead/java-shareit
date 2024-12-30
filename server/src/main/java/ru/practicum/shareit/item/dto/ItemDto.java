package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Data
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private UserDto owner;
    private List<CommentDto> comments;
}
