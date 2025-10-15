package gg.cristalix.growagarden.model.item;

import gg.cristalix.growagarden.common.mod.inventory.ItemEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomItemData {
  String itemId;
  String displayName;
  String material;
  ItemEnum itemEnum;

  Map<String, String> nbt;
  String lore;
  int amountUsage;
}
