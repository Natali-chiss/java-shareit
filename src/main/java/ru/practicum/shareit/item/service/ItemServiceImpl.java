package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private static final String ITEM_NOT_FOUND = "Вещь с id = %d не найдена";
    private final ItemRepository itemRepository;

    @Override
    public ItemDto saveItem(ItemDto item, Long ownerId) {
        Item savedItem = itemRepository.saveItem(ItemMapper.toItem(item), ownerId);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public ItemDto updateItem(Long itemId, Item item, Long userId) {
        Item updatedItem = itemRepository.updateItem(itemId, item, userId);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = itemRepository.getItemById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format(ITEM_NOT_FOUND, itemId)));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getAllItemsOfOwner(Long ownerId) {
        return itemRepository.getAllItemsOfOwner(ownerId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItemById(Long itemId, Long userId) {
        itemRepository.deleteItemById(itemId, userId);
    }

    @Override
    public List<ItemDto> searchByText(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.searchByText(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
