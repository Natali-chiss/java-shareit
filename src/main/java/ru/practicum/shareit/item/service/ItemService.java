package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto saveItem(ItemDto item, Long ownerId);

    ItemDto updateItem(Long itemId, Item item, Long userId);

    ItemDto getItemById(Long itemId);

    List<ItemDto> getAllItemsOfOwner(Long ownerId);

    void deleteItemById (Long id, Long userId);

    List<ItemDto> searchByText (String text);
}