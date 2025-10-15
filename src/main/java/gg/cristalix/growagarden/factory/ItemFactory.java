package gg.cristalix.growagarden.factory;

import gg.cristalix.growagarden.common.mod.inventory.ItemEnum;
import gg.cristalix.growagarden.model.garden.vegetation.MutationType;
import gg.cristalix.growagarden.model.item.CropCustomItem;
import gg.cristalix.growagarden.model.item.CustomItemData;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Set;

@UtilityClass
public class ItemFactory {

  public CropCustomItem createCrop(String seedId, double weight, Set<MutationType> mutations) {
    CropCustomItem crop = new CropCustomItem(seedId);
    CustomItemData data = new CustomItemData(
      seedId,
      "Crop",
      Material.CLAY_BALL.name(),
      ItemEnum.CROP,
      new HashMap<>(),
      null,
      0
    );
    crop.load(data);
    crop.addInstance(weight, mutations);
    return crop;
  }
}