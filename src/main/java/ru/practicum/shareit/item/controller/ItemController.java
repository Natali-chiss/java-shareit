package ru.practicum.shareit.item.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestBody ItemDto item,
                              @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получен запрос на создание нового объекта шеринга");
        userService.getUserById(ownerId);
        ItemDto savedItem = itemService.saveItem(item, ownerId);
        log.info("Создан новый объект шеринга: {}", savedItem);
        return savedItem;
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@PathVariable @NotNull Long itemId,
                              @RequestBody Item item,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на обновление объекта шеринга c id={} от пользователя с id={}", itemId, userId);
        userService.getUserById(userId);
        ItemDto updatedItem = itemService.updateItem(itemId, item, userId);
        log.info("Обновление объекта шеринга завершено: {}", updatedItem);
        return updatedItem;
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItemById(@PathVariable @NotNull Long itemId) {
        log.info("Получен запрос на получение объекта шеринга с id={}", itemId);
        return itemService.getItemById(itemId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getAllItemsOfOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получен запрос на получение объектов шеринга владельца с id={}", ownerId);
        userService.getUserById(ownerId);
        return itemService.getAllItemsOfOwner(ownerId);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteItemById(@PathVariable @NotNull Long itemId,
                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на удаление объекта шеринга");
        userService.getUserById(userId);
        itemService.deleteItemById(itemId, userId);
        log.info("Объект шеринга с id={} успешно удален", itemId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> searchItemByText(@RequestParam @NotNull String text) {
        log.info("Получен запрос на поиск объектов шеринга по тексту: '{}'", text);
        return itemService.searchByText(text);
    }
}
