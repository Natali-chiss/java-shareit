package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();

    private long newId = 0;

    @Override
    public Item saveItem(Item item, Long ownerId) {
        item.setId(++newId);
        item.setOwnerId(ownerId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Long itemId, Item item) {
        Item itemFromRepository = getItemById(itemId).get(); // проверка в сервисе
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
    public void deleteItemById(Long id) {
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
}
