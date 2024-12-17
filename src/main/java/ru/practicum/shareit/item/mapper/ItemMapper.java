package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithDatesDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    public static Item mapToItem(User user, NewItemRequest request) {
        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setAvailable(request.getAvailable());
        item.setOwner(user);
        return item;
    }

    public static ItemDto mapToItemDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.isAvailable());
        dto.setOwner(UserMapper.mapToUserDto(item.getOwner()));
        dto.setComments(item.getComments().stream().map(CommentMapper::mapToCommentDto).collect(Collectors.toList()));
        return dto;
    }

    public static ItemWithDatesDto mapToItemWithDatesDto(Item item, Booking lastBooking, Booking nextBooking) {
        ItemWithDatesDto dto = new ItemWithDatesDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.isAvailable());
        dto.setOwner(UserMapper.mapToUserDto(item.getOwner()));
        if (lastBooking != null) {
            dto.setLastBooking(lastBooking.getEnd());
        }
        if (nextBooking != null) {
            dto.setNextBooking(nextBooking.getStart());
        }
        dto.setComments(item.getComments().stream().map(CommentMapper::mapToCommentDto).collect(Collectors.toList()));
        return dto;
    }

    public static Item updateItemFields(Item item, UpdateItemRequest request) {
        if (request.hasName()) {
            item.setName(request.getName());
        }
        if (request.hasDescription()) {
            item.setDescription(request.getDescription());
        }
        if (request.hasAvailable()) {
            item.setAvailable(request.getAvailable());
        }
        return item;
    }
}
