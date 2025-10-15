package gg.cristalix.growagarden.service.inventory;

import gg.cristalix.growagarden.model.item.CropCustomItem;
import gg.cristalix.growagarden.model.item.ItemCustomItem;
import gg.cristalix.growagarden.model.item.SeedCustomItem;
import gg.cristalix.growagarden.model.player.GamePlayer;
import lombok.experimental.UtilityClass;

import javax.annotation.Nullable;

@UtilityClass
public class InventoryService {

  public boolean addCrop(GamePlayer player, CropCustomItem crop) {
    return player.getInventoryData().getCropInventoryData().addItem(crop);
  }

  public boolean addSeed(GamePlayer player, SeedCustomItem seed) {
    return player.getInventoryData().getSeedInventoryData().addItem(seed);
  }

  public boolean addItem(GamePlayer player, ItemCustomItem item) {
    return player.getInventoryData().getItemInventoryData().addItem(item);
  }

  @Nullable
  public CropCustomItem removeCrop(GamePlayer player, String itemId) {
    return player.getInventoryData().getCropInventoryData().removeItem(itemId);
  }

  @Nullable
  public SeedCustomItem removeSeed(GamePlayer player, String itemId) {
    return player.getInventoryData().getSeedInventoryData().removeItem(itemId);
  }

  @Nullable
  public ItemCustomItem removeItem(GamePlayer player, String itemId) {
    return player.getInventoryData().getItemInventoryData().removeItem(itemId);
  }

  public boolean decreaseSeedAmount(GamePlayer player, String itemId, int amount) {
    return player.getInventoryData().getSeedInventoryData().removeItem(itemId, amount);
  }

  public boolean decreaseCropAmount(GamePlayer player, String itemId, int amount) {
    return player.getInventoryData().getCropInventoryData().removeItem(itemId, amount);
  }

  public boolean decreaseItemAmount(GamePlayer player, String itemId, int amount) {
    return player.getInventoryData().getItemInventoryData().removeItem(itemId, amount);
  }

  @Nullable
  public SeedCustomItem getSeedByUUID(GamePlayer player, java.util.UUID uuid) {
    return player.getInventoryData().getSeedInventoryData().getItemByUUID(uuid);
  }

  @Nullable
  public CropCustomItem getCropByUUID(GamePlayer player, java.util.UUID uuid) {
    return player.getInventoryData().getCropInventoryData().getItemByUUID(uuid);
  }

  @Nullable
  public ItemCustomItem getItemByUUID(GamePlayer player, java.util.UUID uuid) {
    return player.getInventoryData().getItemInventoryData().getItemByUUID(uuid);
  }

  public boolean hasSeed(GamePlayer player, String itemId) {
    return player.getInventoryData().getSeedInventoryData().hasItem(itemId);
  }

  public boolean hasCrop(GamePlayer player, String itemId) {
    return player.getInventoryData().getCropInventoryData().hasItem(itemId);
  }

  public boolean hasItem(GamePlayer player, String itemId) {
    return player.getInventoryData().getItemInventoryData().hasItem(itemId);
  }

  public int getSeedCount(GamePlayer player, String itemId) {
    return player.getInventoryData().getSeedInventoryData().getItemCount(itemId);
  }

  public int getCropCount(GamePlayer player, String itemId) {
    return player.getInventoryData().getCropInventoryData().getItemCount(itemId);
  }

  public int getItemCount(GamePlayer player, String itemId) {
    return player.getInventoryData().getItemInventoryData().getItemCount(itemId);
  }

  public boolean canAddSeed(GamePlayer player, SeedCustomItem seed) {
    return player.getInventoryData().getSeedInventoryData().canAddItem(seed);
  }

  public boolean canAddCrop(GamePlayer player, CropCustomItem crop) {
    return player.getInventoryData().getCropInventoryData().canAddItem(crop);
  }

  public boolean canAddItem(GamePlayer player, ItemCustomItem item) {
    return player.getInventoryData().getItemInventoryData().canAddItem(item);
  }

  public void clearSeeds(GamePlayer player) {
    player.getInventoryData().getSeedInventoryData().clearItems();
  }

  public void clearCrops(GamePlayer player) {
    player.getInventoryData().getCropInventoryData().clearItems();
  }

  public void clearItems(GamePlayer player) {
    player.getInventoryData().getItemInventoryData().clearItems();
  }
}