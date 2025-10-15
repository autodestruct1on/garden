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
    return switch (itemEnum) {
      case ITEM -> itemInventoryData.getItemByUUID(uuid);
      case CROP -> cropInventoryData.getItemByUUID(uuid);
      case SEED -> seedInventoryData.getItemByUUID(uuid);
      default -> null;
    };
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