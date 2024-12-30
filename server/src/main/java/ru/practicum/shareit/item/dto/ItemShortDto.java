package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemShortDto {
    private long itemId;
    private String name;
    private long ownerId;
}
