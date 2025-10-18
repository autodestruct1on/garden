package gg.cristalix.growagarden.model.player.inventory;

import gg.cristalix.growagarden.common.mod.inventory.ItemEnum;
import gg.cristalix.growagarden.model.item.CustomItem;
import gg.cristalix.growagarden.model.player.inventory.impl.CropInventoryData;
import gg.cristalix.growagarden.model.player.inventory.impl.ItemInventoryData;
import gg.cristalix.growagarden.model.player.inventory.impl.SeedInventoryData;
import gg.cristalix.growagarden.service.item.ItemService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryData {

  final ItemInventoryData itemInventoryData;
  final CropInventoryData cropInventoryData;
  final SeedInventoryData seedInventoryData;

  transient CustomItem equipCustomItem;
  transient Player player;

  public InventoryData() {
    this.itemInventoryData = new ItemInventoryData();
    this.cropInventoryData = new CropInventoryData();
    this.seedInventoryData = new SeedInventoryData();
  }

  public void load(Player player) {
    this.player = player;
    this.itemInventoryData.setInventoryData(this);
    this.cropInventoryData.setInventoryData(this);
    this.seedInventoryData.setInventoryData(this);
  }

  public CustomItem getCustomItem(ItemEnum itemEnum, UUID uuid) {
    return getInventory(itemEnum).getItemByUUID(uuid);
  }

  public <T extends CustomItem> CustomInventoryData<T> getInventory(ItemEnum itemEnum) {
    return (CustomInventoryData<T>) switch (itemEnum) {
      case ITEM -> itemInventoryData;
      case CROP -> cropInventoryData;
      case SEED -> seedInventoryData;
      default -> null;
    };
  }

  public boolean addItem(CustomItem customItem) {
    return getInventory(customItem.getItemEnum()).addItem(customItem);
  }

  public <T extends CustomItem> T removeItem(ItemEnum itemEnum, String itemId) {
    return (T) getInventory(itemEnum).removeItem(itemId);
  }

  public boolean decreaseItemAmount(ItemEnum itemEnum, String itemId, int count) {
    return getInventory(itemEnum).removeItem(itemId, count);
  }

  public <T extends CustomItem> T getItemByUUID(ItemEnum itemEnum, UUID uuid) {
    return (T) getInventory(itemEnum).getItemByUUID(uuid);
  }

  public boolean hasItem(ItemEnum itemEnum, String itemId) {
    return getInventory(itemEnum).hasItem(itemId);
  }

  public int getItemCount(ItemEnum itemEnum, String itemId) {
    return getInventory(itemEnum).getItemCount(itemId);
  }

  public boolean canAddItem(ItemEnum itemEnum, CustomItem customItem) {
    return getInventory(itemEnum).canAddItem(customItem);
  }

  public CustomInventoryData<?>[] getAllInventory() {
    return new CustomInventoryData<?>[]{itemInventoryData, cropInventoryData, seedInventoryData};
  }

  public void load(ItemService service) {
    for (CustomInventoryData<? extends CustomItem> customInventoryData : getAllInventory()) {
      for (CustomItem value : customInventoryData.getItems().values()) {
        service.loadItem(value);
      }
    }
  }

  public <T extends CustomItem> void setEquipItem(T equip) {
    if (this.equipCustomItem == equip) return;
    this.equipCustomItem = equip;
    setItemInHand(equip);
  }

  public <T extends CustomItem> void updateEquipItem(T update) {
    if (this.equipCustomItem != update) return;
    setItemInHand(update);
  }

  public <T extends CustomItem> void removeEquipItem(T remove) {
    if (this.equipCustomItem != remove) return;
    this.equipCustomItem = null;
    setItemInHand(null);
  }

  private void setItemInHand(CustomItem customItem) {
    this.equipCustomItem = customItem;
    if (customItem == null) {
      player.setItemInHand(null);
    } else {
      ItemStack itemStack = customItem.getItemStack();
      itemStack.setAmount(customItem.getAmount());
      player.setItemInHand(itemStack);
    }
  }
}