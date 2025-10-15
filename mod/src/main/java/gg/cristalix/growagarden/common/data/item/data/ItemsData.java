package gg.cristalix.growagarden.common.data.item.data;

import dev.xdark.clientapi.item.ItemStack;
import gg.cristalix.growagarden.common.mod.inventory.ItemEnum;
import gg.cristalix.growagarden.common.util.Rarity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemsData {
  UUID uuid;
  ItemStack itemStack;
  @Builder.Default
  String itemId = "";
  @Builder.Default
  Rarity rarity = Rarity.COMMON;
  @Builder.Default
  double sort = 0;
  @Builder.Default
  boolean equip = false;
  @Builder.Default
  ItemEnum itemEnum;
  @Builder.Default
  long count = 1;
  @Builder.Default
  int feedLevel = 1;
  @Builder.Default
  double price = 0;
  @Builder.Default
  boolean bans = false;
  @Builder.Default
  boolean isEnchant = false;
  @Builder.Default
  boolean lock = false;
  @Builder.Default
  Set<ImproveEnchant> improveEnchants = new HashSet<>();
  @Builder.Default
  Set<EnchantData> enchant = new HashSet<>();
  @Builder.Default
  Set<EnchantLevel> enchantLevel = new HashSet<>();
  @Builder.Default
  long exp = 0;
  @Builder.Default
  int giveExp = 0;
  @Builder.Default
  double chance = 0;

}