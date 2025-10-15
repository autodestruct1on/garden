package gg.cristalix.growagarden.common.data.item.data;

import dev.xdark.clientapi.item.ItemStack;
import gg.cristalix.enginex.transfer.ModTransfer;
import gg.cristalix.growagarden.common.mod.inventory.ItemEnum;
import gg.cristalix.growagarden.common.util.Rarity;

import java.util.UUID;

public class ItemsDataDeserializers {

  private static final boolean DEBUG = true;

  @FunctionalInterface
  public interface ItemsDataDeserializer {
    void deserialize(ItemsData.ItemsDataBuilder builder, ModTransfer transfer);
  }

  public static final ItemsDataDeserializer ITEM_ID = (builder, transfer) -> {
    String itemId = transfer.readString();
    if (DEBUG) System.out.println("read ITEM_ID: " + itemId);
    builder.itemId(itemId);
  };
  public static final ItemsDataDeserializer UUID = (builder, transfer) -> {
    UUID uuid = transfer.readUUID();
    if (DEBUG) System.out.println("read UUID: " + uuid);
    builder.uuid(uuid);
  };
  public static final ItemsDataDeserializer RARITY = (builder, transfer) -> {
    Rarity rarity = Rarity.VAL[transfer.readInt()];
    if (DEBUG) System.out.println("read RARITY: " + rarity);
    builder.rarity(rarity);
  };
  public static final ItemsDataDeserializer SORT = (builder, transfer) -> {
    double sort = transfer.readDouble();
    if (DEBUG) System.out.println("read SORT: " + sort);
    builder.sort(sort);
  };
  public static final ItemsDataDeserializer IS_EQUIP = (builder, transfer) -> {
    boolean equip = transfer.readBoolean();
    if (DEBUG) System.out.println("read IS_EQUIP: " + equip);
    builder.equip(equip);
  };
  public static final ItemsDataDeserializer COUNT = (builder, transfer) -> {
    long count = transfer.readLong();
    if (DEBUG) System.out.println("read COUNT: " + count);
    builder.count(count);
  };
  public static final ItemsDataDeserializer ITEM_ENUM = (builder, transfer) -> {
    ItemEnum itemEnum = ItemEnum.VAL[transfer.readInt()];
    if (DEBUG) System.out.println("read ITEM_ENUM: " + itemEnum);
    builder.itemEnum(itemEnum);
  };
  public static final ItemsDataDeserializer FEED_LEVEL = (builder, transfer) -> {
    int feedLevel = transfer.readInt();
    if (DEBUG) System.out.println("read FEED_LEVEL: " + feedLevel);
    builder.feedLevel(feedLevel);
  };
  public static final ItemsDataDeserializer PRICE = (builder, transfer) -> {
    double price = transfer.readDouble();
    if (DEBUG) System.out.println("read PRICE: " + price);
    builder.price(price);
  };
  public static final ItemsDataDeserializer BANS = (builder, transfer) -> {
    boolean bans = transfer.readBoolean();
    if (DEBUG) System.out.println("read BANS: " + bans);
    builder.bans(bans);
  };
  public static final ItemsDataDeserializer IS_ENCHANT = (builder, transfer) -> {
    boolean isEnchant = transfer.readBoolean();
    if (DEBUG) System.out.println("read IS_ENCHANT: " + isEnchant);
    builder.isEnchant(isEnchant);
  };
  public static final ItemsDataDeserializer IS_LOCK = (builder, transfer) -> {
    boolean lock = transfer.readBoolean();
    if (DEBUG) System.out.println("read IS_LOCK: " + lock);
    builder.lock(lock);
  };

  public static final ItemsDataDeserializer EXP = (builder, transfer) -> {
    long exp = transfer.readLong();
    if (DEBUG) System.out.println("read EXP: " + exp);
    builder.exp(exp);
  };
  public static final ItemsDataDeserializer GIVE_EXP = (builder, transfer) -> {
    int giveExp = transfer.readInt();
    if (DEBUG) System.out.println("read GIVE_EXP: " + giveExp);
    builder.giveExp(giveExp);
  };
  public static final ItemsDataDeserializer CHANCE = (builder, transfer) -> {
    double chance = transfer.readDouble();
    if (DEBUG) System.out.println("read CHANCE: " + chance);
    builder.chance(chance);
  };
  public static final ItemsDataDeserializer ITEM_STACK = (builder, transfer) -> {
    ItemStack itemStack = transfer.readItem();
    if (DEBUG) System.out.println("read ITEM_STACK: " + itemStack);
    builder.itemStack(itemStack);
  };

  public static ItemsData deserialize(ModTransfer transfer, ItemsDataDeserializer... deserializers) {
    ItemsData.ItemsDataBuilder builder = ItemsData.builder();
    for (ItemsDataDeserializer deserializer : deserializers) {
      deserializer.deserialize(builder, transfer);
    }
    return builder.build();
  }
}