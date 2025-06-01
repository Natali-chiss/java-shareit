package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.ItemRequest;

@Getter
@Setter
@Builder
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private ItemRequest request; //если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос.

    public Boolean isAvailable() {
        return available;
    }
}