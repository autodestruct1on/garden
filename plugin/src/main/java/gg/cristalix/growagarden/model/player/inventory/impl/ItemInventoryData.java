package gg.cristalix.growagarden.model.player.inventory.impl;

import gg.cristalix.growagarden.model.item.ItemCustomItem;
import gg.cristalix.growagarden.model.player.inventory.CustomInventoryData;

public class ItemInventoryData extends CustomInventoryData<ItemCustomItem> {

  public ItemInventoryData() {
    super(5);
  }

  @Override
  protected boolean mergeItems(ItemCustomItem existing, ItemCustomItem newItem) {
    this.inventoryData.updateEquipItem(existing);
    existing.increaseAmount(newItem.getAmount());
    return true;
  }
}