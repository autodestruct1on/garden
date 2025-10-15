package gg.cristalix.growagarden.service.item;

import gg.cristalix.growagarden.model.item.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemService {

  @Getter
  Map<String, CustomItemData> customItemDataMap;

  public ItemService() {
    this.customItemDataMap = new HashMap<>();
  }

  public void registerItemData(String itemId, CustomItemData itemData) {
    customItemDataMap.put(itemId, itemData);
  }

  public void registerAllItemData(Map<String, CustomItemData> itemDataMap) {
    customItemDataMap.putAll(itemDataMap);
  }

  @Nullable
  public CustomItem createItem(String itemId) {
    CustomItemData customItemData = getCustomItemData(itemId);
    if (customItemData == null) return null;

    return switch (customItemData.getItemEnum()) {
      case CROP -> createItem(customItemData, new CropCustomItem(itemId));
      case ITEM -> createItem(customItemData, new ItemCustomItem(itemId));
      case SEED -> createItem(customItemData, new SeedCustomItem(itemId));
      default -> null;
    };
  }

  @Nullable
  private CustomItemData getCustomItemData(String itemId) {
    return customItemDataMap.get(itemId);
  }

  public void loadItem(CustomItem customItem) {
    CustomItemData customItemData = getCustomItemData(customItem.getItemId());
    if (customItemData != null) {
      customItem.load(customItemData);
    }
  }

  private CustomItem createItem(CustomItemData itemData, CustomItem customItem) {
    return customItem.load(itemData);
  }

  public boolean hasItemData(String itemId) {
    return customItemDataMap.containsKey(itemId);
  }

  @Nullable
  public CustomItemData getItemData(String itemId) {
    return customItemDataMap.get(itemId);
  }
}