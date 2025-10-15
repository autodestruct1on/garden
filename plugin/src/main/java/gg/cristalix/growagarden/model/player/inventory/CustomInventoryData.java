package gg.cristalix.growagarden.model.player.inventory;

import gg.cristalix.growagarden.model.item.CustomItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class CustomInventoryData<T extends CustomItem> {
  final Map<String, T> items = new HashMap<>();
  int maxSize;

  @Setter
  transient InventoryData inventoryData;

  protected CustomInventoryData(int maxSize) {
    this.maxSize = maxSize;
  }

  public T getItem(String itemId) {
    return items.get(itemId);
  }

  @Nullable
  public T getItemByUUID(UUID uuid) {
    return items.values().stream()
            .filter(item -> item.matchesUUID(uuid))
            .findFirst()
            .orElse(null);
  }

  public T removeItem(String itemId) {
    T remove = items.remove(itemId);
    if (remove != null) {
      this.inventoryData.removeEquipItem(remove);
    }
    return remove;
  }

  public boolean hasItem(String itemId) {
    return items.containsKey(itemId);
  }

  public void addOrUpdateItem(T item) {
    items.put(item.getItemId(), item);
  }

  public int getTotalItemCount() {
    return items.values().stream()
            .mapToInt(CustomItem::getAmount)
            .sum();
  }

  public boolean addItem(T item) {
    T existing = getItem(item.getItemId());

    if (existing != null) {
      return mergeItems(existing, item);
    }

    if (getTotalItemCount() >= maxSize) {
      return false;
    }

    addOrUpdateItem(item);
    return true;
  }

  protected abstract boolean mergeItems(T existing, T newItem);

//  public boolean decreaseAmount(String itemId, int amount) {
//    T item = getItem(itemId);
//    if (item == null || item.getAmount() < amount) {
//      return false;
//    }
//
//    item.decreaseAmount(amount);
//
//    if (item.getAmount() <= 0) {
//      removeItem(itemId);
//    } else {
//      this.inventoryData.updateEquipItem(item);
//    }
//
//    return true;
//  }

  public boolean removeItem(String itemId, int amount) {
    T item = getItem(itemId);
    if (item == null) {
      return false;
    }

    if (item.getAmount() < amount) {
      return false;
    }

    item.decreaseAmount(amount);

    if (item.getAmount() <= 0) {
      removeItem(itemId);
    } else {
      this.inventoryData.updateEquipItem(item);
    }

    return true;
  }

  public void clearItems() {
    items.clear();
  }

  public int getItemCount(String itemId) {
    T item = getItem(itemId);
    return item != null ? item.getAmount() : 0;
  }

  public boolean canAddItem(T item) {
    T existing = getItem(item.getItemId());

    if (existing != null) {
      return true;
    }

    boolean accept = getTotalItemCount() < maxSize;

    if (accept) this.inventoryData.updateEquipItem(item);

    return accept;
  }

  public void increaseItemAmount(String itemId, int amount) {
    T item = getItem(itemId);
    if (item != null) {
      item.increaseAmount(amount);
      this.inventoryData.updateEquipItem(item);
    }
  }
}