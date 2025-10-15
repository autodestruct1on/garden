package gg.cristalix.growagarden.mod.util;

import gg.cristalix.growagarden.model.item.CustomItem;
import gg.cristalix.wada.transfer.ModTransfer;

public class ItemsDataSerializers {

  private static final boolean DEBUG = true;

  @FunctionalInterface
  public interface ItemDataSerializers {
    void deserialize(CustomItem customItem, ModTransfer transfer);
  }

  public static final ItemDataSerializers ITEM_ID = (customItem, transfer) -> {
    if (DEBUG) System.out.println("write ITEM_ID: " + customItem.getItemId());
    transfer.writeString(customItem.getItemId());
  };

  public static final ItemDataSerializers UUID = (customItem, transfer) -> {
    if (DEBUG) System.out.println("write UUID: " + customItem.getUuid());
    transfer.writeUUID(customItem.getUuid());
  };

  public static final ItemDataSerializers COUNT = (customItem, transfer) -> {
    if (DEBUG) System.out.println("write COUNT: " + customItem.getAmount());
    transfer.writeLong(customItem.getAmount());
  };

  public static final ItemDataSerializers ITEM_STACK = (customItem, transfer) -> {
    if (DEBUG) System.out.println("write ITEM_STACK: " + customItem.getItemStack());
    transfer.writeItem(customItem.getItemStack());
  };

  public static final ItemDataSerializers ITEM_ENUM = (customItem, transfer) -> {
    if (DEBUG) System.out.println("write ITEM_ENUM: " + customItem.getItemEnum());
    transfer.writeInt(customItem.getItemEnum().ordinal());
  };

  public static void serialize(ModTransfer transfer, CustomItem customItem, ItemDataSerializers... serializers) {
    for (ItemDataSerializers serializer : serializers) {
      serializer.deserialize(customItem, transfer);
    }
  }
}