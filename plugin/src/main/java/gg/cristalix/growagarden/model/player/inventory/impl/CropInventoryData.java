package gg.cristalix.growagarden.model.player.inventory.impl;

import gg.cristalix.growagarden.model.item.CropCustomItem;
import gg.cristalix.growagarden.model.player.inventory.CustomInventoryData;

public class CropInventoryData extends CustomInventoryData<CropCustomItem> {

  public CropInventoryData() {
    super(40);
  }

  @Override
  protected boolean mergeItems(CropCustomItem existing, CropCustomItem newItem) {
    this.inventoryData.updateEquipItem(existing);
    for (CropCustomItem.CropInstance instance : newItem.getInstances()) {
      existing.addInstance(instance.getWeight(), instance.getMutations());
    }
    return true;
  }
}