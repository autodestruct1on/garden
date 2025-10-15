package gg.cristalix.growagarden.common.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Rarity {

  COMMON("Обычный", "Обычное", "Обычного", -5723992, "707070", 20, 1, 1, 0),
  RARE("Редкий", "Редкое", "Редкого", -11891476, "2A66BD", 25, 2, 2, 1),
  EPIC("Эпический", "Эпическое", "Эпического", -8500500, "5B27C9", 30, 3, 3, 4),
  LEGENDARY("Легендарный", "Легендарное", "Легендарного", -25278, "EFAC00", 40, 5, 4, 6),
  MYTHIC("Мифический", "Мифическое", "Мифического", -1622709, "A91925", 50, 8, 6, 8),
  SECRET("Секретное", "Секретное", "Секретного", -15149868, "37EFEF", 50, 10, 8, 10),
  OTHER("Остальное", "Остальное", "Остального", -11935884, "22AE49", 0, 9999999, 9999999, 9999999);

  public static final Rarity[] VAL = values();

  String name;
  String nameDroppedWeapon;
  String nameDroppedPet;
  int decimal;
  String colorHex;
  int maxRelicLevel;
  int priceEnchantWeapon;
  int priceEnchantPet;
  int priceUpgradePet;

  public String getColorChat() {
    return "¨" + colorHex.toLowerCase();
  }
}