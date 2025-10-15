package gg.cristalix.growagarden.common.data.item.data;

import gg.cristalix.enginex.transfer.ModTransfer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImproveEnchant extends EnchantData {
  double giveBoostLevel;

  public static ImproveEnchant deserialize(ModTransfer transfer) {
    ImproveEnchant improveEnchant = new ImproveEnchant();

    improveEnchant.icon = transfer.readString();
    improveEnchant.name = transfer.readString();
    improveEnchant.currentBoost = transfer.readDouble();
    improveEnchant.giveBoostLevel = transfer.readDouble();

    return improveEnchant;
  }
}
