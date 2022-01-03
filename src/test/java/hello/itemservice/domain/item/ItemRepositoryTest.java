package hello.itemservice.domain.item;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ItemRepositoryTest {

    private ItemRepository itemRepository = new ItemRepository();

    @AfterEach
    void clearStore() {
        itemRepository.clearStore();
    }

    @Test
    void save() {
        // given
        ItemParamDto itemParamDto = new ItemParamDto("item1", 100, 10);
        Item item = new Item(itemParamDto);

        // when
        Item savedItem = itemRepository.save(item);

        // then
        Item findItem = itemRepository.findByID(savedItem.getId());
        assertThat(findItem).isEqualTo(savedItem);
    }

    @Test
    void findAll() {
        // given
        ItemParamDto itemParamDto1 = new ItemParamDto("item1", 100, 10);
        Item item1 = new Item(itemParamDto1);
        itemRepository.save(item1);

        ItemParamDto itemParamDto2 = new ItemParamDto("item2", 1000, 5);
        Item item2 = new Item(itemParamDto1);
        itemRepository.save(item2);

        // when
        List<Item> result = itemRepository.findAll();

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(item1);
    }

    @Test
    void update() {
        // given
        ItemParamDto itemParamDto1 = new ItemParamDto("item1", 100, 10);
        Item item1 = new Item(itemParamDto1);
        itemRepository.save(item1);

        // when
        ItemParamDto itemParamDto = new ItemParamDto("item1", 10000, 10);
        itemRepository.update(item1.getId(), itemParamDto);

        // then
        Item findItem = itemRepository.findByID(item1.getId());
        assertThat(findItem.getItemParamDto().getPrice()).isEqualTo(10000);
    }
}