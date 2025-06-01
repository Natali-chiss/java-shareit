package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private static final String ITEM_NOT_FOUND = "Вещь с id = %d не найдена";
    private final Map<Long, Item> items = new HashMap<>();

    private long newId = 0;

    @Override
    public Item saveItem(Item item, Long ownerId) {
        validateCreate(item);
        item.setId(++newId);
        item.setOwnerId(ownerId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Long itemId, Item item, Long userId) {
        Item itemFromRepository = getItemById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format(ITEM_NOT_FOUND, item.getId())));

        validateUserIsOwner(itemFromRepository, userId);

        if (item.getName() != null) {
            itemFromRepository.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemFromRepository.setDescription(item.getDescription());
        }
        if (item.isAvailable() != null) {
            itemFromRepository.setAvailable(item.isAvailable());
        }
        return itemFromRepository;
    }

    @Override
    public Optional<Item> getItemById(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> getAllItemsOfOwner(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId().equals(ownerId))
                .toList();
    }

    @Override
    public void deleteItemById(Long id, Long userId) {
        Item item = getItemById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ITEM_NOT_FOUND, id)));
        validateUserIsOwner(item, userId);
        items.remove(id);
    }

    @Override
    public List<Item> searchByText(String text) {
        String searchText = text.toLowerCase();

        return items.values().stream()
                .filter(Item::isAvailable)
                .filter(item ->
                        item.getName().toLowerCase().contains(searchText) ||
                        item.getDescription().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
    }

    private void validateCreate(Item item) {
        if (item.getName().isBlank()) {
            throw new ConditionsNotMetException("Название должно быть указано");
        }
        if (item.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание должно быть указано");
        }
        if (item.isAvailable() == null) {
            throw new ConditionsNotMetException("Доступность должна быть указана");
        }
    }

    private void validateUserIsOwner(Item item, Long userId) {
        if (!item.getOwnerId().equals(userId)) {
            throw new ConditionsNotMetException("Редактировать или удалять вещь может только её владелец");
        }
    }
}
