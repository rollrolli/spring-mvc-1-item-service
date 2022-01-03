package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ItemRepository {

    private static final Map<Long, Item> store = new ConcurrentHashMap<>();
    private static AtomicLong sequence = new AtomicLong();

    public Item save(Item item) {
        item.setId(sequence.getAndIncrement());
        store.put(item.getId(), item);
        return item;
    }

    public Item findByID(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId, ItemParamDto itemParamDto) {
        Item findItem = findByID(itemId);
        findItem.setItemParamDto(itemParamDto);
    }

    public void clearStore() {
        store.clear();
    }

}
