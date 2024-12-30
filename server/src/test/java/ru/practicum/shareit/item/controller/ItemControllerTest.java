package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.interfaces.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private ItemDto itemDto;

    private ItemWithDatesDto itemWithDatesDto;

    private CommentDto commentDto;

    private ItemRequest itemRequest;

    private CommentRequest commentRequest;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemController)
                .build();

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Item");
        itemDto.setDescription("Description");
        itemDto.setAvailable(true);
        itemDto.setOwner(null);
        itemDto.setComments(List.of());

        itemWithDatesDto = new ItemWithDatesDto();
        itemWithDatesDto.setId(1L);
        itemWithDatesDto.setName("Name");
        itemWithDatesDto.setDescription("Description");
        itemWithDatesDto.setAvailable(true);
        itemWithDatesDto.setOwner(null);
        itemWithDatesDto.setComments(null);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Text");
        commentDto.setAuthorName("Author");
        commentDto.setCreated(null);

        itemRequest = new ItemRequest();
        itemRequest.setName("Name");
        itemRequest.setDescription("Description");
        itemRequest.setAvailable(true);
        itemRequest.setRequestId(1L);

        commentRequest = new CommentRequest();
        commentRequest.setText("Text");
    }

    @Test
    void findAllItemsOfUser_whenInvoked_thenHasCorrectResponse() throws Exception {
        when(itemService.findAllItemsOfUser(1L)).thenReturn(List.of(itemDto));
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemDto))));
    }

    @Test
    void findItem_whenInvoked_thenHasCorrectResponse() throws Exception {
        when(itemService.findItem(1L, 1L)).thenReturn(itemWithDatesDto);
        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemWithDatesDto)));
    }

    @Test
    void findItemByText_whenInvoked_thenHasCorrectResponse() throws Exception {
        when(itemService.findItemByText(eq(1L), any())).thenReturn(List.of(itemDto));
        mvc.perform(get("/items/search?text=text")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemDto))));
    }

    @Test
    void createItem_whenInvoked_thenHasCorrectResponse() throws Exception {
        when(itemService.createItem(1L, itemRequest)).thenReturn(itemDto);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto)));
    }

    @Test
    void createComment_whenInvoked_thenHasCorrectResponse() throws Exception {
        when(itemService.createComment(1L, 1L, commentRequest)).thenReturn(commentDto);
        mvc.perform(post("/items/1/comment").header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(commentDto)));
    }

    @Test
    void updateItem_whenInvoked_thenHasCorrectResponse() throws Exception {
        when(itemService.updateItem(1L, 1L, itemRequest)).thenReturn(itemDto);
        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto)));
    }
}