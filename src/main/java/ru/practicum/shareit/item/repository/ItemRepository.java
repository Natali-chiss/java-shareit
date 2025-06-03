package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item saveItem(Item item, Long ownerId);

    Item updateItem(Long itemId, Item item);

    Optional<Item> getItemById(Long itemId);

    List<Item> getAllItemsOfOwner(Long ownerId);

    void deleteItemById(Long id);

    List<Item> searchByText(String text);
}
