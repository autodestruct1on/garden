package gg.cristalix.growagarden.common.data.item.data;

import gg.cristalix.enginex.transfer.ModTransfer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class EnchantData {
  String icon;
  String name;
  double currentBoost;

  public static EnchantData deserialize(ModTransfer transfer) {
    EnchantData improveEnchant = new EnchantData();

    improveEnchant.icon = transfer.readString();
    improveEnchant.name = transfer.readString();
    improveEnchant.currentBoost = transfer.readDouble();

    return improveEnchant;
  }
}
