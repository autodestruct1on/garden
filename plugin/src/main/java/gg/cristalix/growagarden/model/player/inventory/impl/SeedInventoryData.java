package gg.cristalix.growagarden.model.player.inventory.impl;

import gg.cristalix.growagarden.model.item.SeedCustomItem;
import gg.cristalix.growagarden.model.player.inventory.CustomInventoryData;

public class SeedInventoryData extends CustomInventoryData<SeedCustomItem> {

  public SeedInventoryData() {
    super(20);
  }

  @Override
  protected boolean mergeItems(SeedCustomItem existing, SeedCustomItem newItem) {
    this.inventoryData.updateEquipItem(existing);
    existing.increaseAmount(newItem.getAmount());
    return true;
  }
}