package gg.cristalix.growagarden.mod;

import gg.cristalix.growagarden.GrowAGardenPlugin;
import gg.cristalix.growagarden.mod.crop.CropMod;
import gg.cristalix.growagarden.mod.event.PlayerModLoadedEvent;
import gg.cristalix.growagarden.mod.inventory.InventoryMod;
import gg.cristalix.wada.transfer.ModTransfer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GardenMod {
  GrowAGardenPlugin plugin;

  InventoryMod inventoryMod;
  CropMod cropMod;

  public GardenMod(GrowAGardenPlugin plugin) {
    this.plugin = plugin;

    this.inventoryMod = new InventoryMod(this);
    this.cropMod = new CropMod(this);

    plugin.registerEvents(cropMod);

    ModTransfer.registerChannel("gag:mod:loaded", (player, transfer) -> {
      new PlayerModLoadedEvent(player).callEvent();
    });
  }
}
