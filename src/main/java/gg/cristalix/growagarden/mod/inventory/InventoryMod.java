package gg.cristalix.growagarden.mod.inventory;

import gg.cristalix.growagarden.common.mod.inventory.ItemEnum;
import gg.cristalix.growagarden.mod.GardenMod;
import gg.cristalix.growagarden.mod.util.ItemsDataSerializers;
import gg.cristalix.growagarden.model.item.CustomItem;
import gg.cristalix.growagarden.model.player.GamePlayer;
import gg.cristalix.growagarden.model.player.inventory.CustomInventoryData;
import gg.cristalix.growagarden.model.player.inventory.InventoryData;
import gg.cristalix.wada.transfer.ModTransfer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

public class InventoryMod {

  GardenMod gardenMod;

  public InventoryMod(GardenMod gardenMod) {
    this.gardenMod = gardenMod;

    ModTransfer.registerChannel("gag:inv:open", (player, transfer) -> {
      int itemEnumIndex = transfer.readInt();
      ItemEnum itemEnum = ItemEnum.getItemEnum(itemEnumIndex);
      if (itemEnum == null) return;

      openMenu(player, itemEnum);
    });

    ModTransfer.registerChannel("gag:inv:equip", (player, transfer) -> {
      int itemEnumIndex = transfer.readInt();
      ItemEnum itemEnum = ItemEnum.getItemEnum(itemEnumIndex);
      if (itemEnum == null) return;

      UUID uuid = transfer.readUUID();
      equipItem(player, itemEnum, uuid);
    });
  }

  private void equipItem(Player player, ItemEnum itemEnum, UUID uuid) {
    GamePlayer gamePlayer = player.getBungeePlayer();
    InventoryData inventoryData = gamePlayer.getInventoryData();
    CustomItem customItem = inventoryData.getCustomItem(itemEnum, uuid);
    if (customItem == null) return;
    inventoryData.setEquipItem(customItem);
  }

  public void openMenu(Player player, ItemEnum itemEnum) {
    ModTransfer transfer = new ModTransfer()
      .writeInt(itemEnum.ordinal());

    loadInventory(transfer, player, itemEnum);

    transfer.send("gag:inv:open", player);
  }

  private void loadInventory(ModTransfer transfer, Player player, ItemEnum itemEnum) {
    GamePlayer gamePlayer = player.getBungeePlayer();
    InventoryData inventoryData = gamePlayer.getInventoryData();

    switch (itemEnum) {
      case ALL -> writeInventoryData(transfer, inventoryData.getAllInventory());
      case ITEM -> writeInventoryData(transfer, inventoryData.getItemInventoryData());
      case CROP -> writeInventoryData(transfer, inventoryData.getCropInventoryData());
      case SEED -> writeInventoryData(transfer, inventoryData.getSeedInventoryData());
      default -> transfer.writeLong(0);
    }
  }

  private void writeInventoryData(ModTransfer transfer, CustomInventoryData<?>... inventoryDataArray) {
    writeCountItems(transfer, inventoryDataArray);

    for (CustomInventoryData<?> inventoryData : inventoryDataArray) {
      writeItems(transfer, inventoryData);
    }
  }

  private void writeCountItems(ModTransfer transfer, CustomInventoryData<?>... inventoryDataArray) {
    long totalSize = 0;

    for (CustomInventoryData<?> customInventoryData : inventoryDataArray) {
      totalSize += customInventoryData.getItems().size();
    }

    transfer.writeLong(totalSize);
  }

  private <T extends CustomItem> void writeItems(ModTransfer transfer, CustomInventoryData<T> inventoryData) {
    Collection<T> values = inventoryData.getItems().values();
    values.forEach(value -> {
      ItemsDataSerializers.serialize(
        transfer,
        value,
        ItemsDataSerializers.UUID,
        ItemsDataSerializers.ITEM_ID,
        ItemsDataSerializers.ITEM_STACK,
        ItemsDataSerializers.COUNT,
        ItemsDataSerializers.ITEM_ENUM
      );
    });
  }
}
