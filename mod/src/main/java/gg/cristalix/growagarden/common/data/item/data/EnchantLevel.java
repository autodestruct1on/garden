package gg.cristalix.growagarden.common.data.item.data;

import gg.cristalix.enginex.transfer.ModTransfer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class EnchantLevel {
  String icon;
  String name;
  int level;

  public static EnchantLevel deserialize(ModTransfer transfer) {
    EnchantLevel improveEnchant = new EnchantLevel();

    improveEnchant.icon = transfer.readString();
    improveEnchant.name = transfer.readString();
    improveEnchant.level = transfer.readInt();

    return improveEnchant;
  }
}
