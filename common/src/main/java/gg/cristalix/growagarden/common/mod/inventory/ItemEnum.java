package gg.cristalix.growagarden.common.mod.inventory;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ItemEnum {
  ALL("Все", false),
  ITEM("Предметы", true),
  SEED("Семена", true),
  CROP("Растения", true),
  PET("Питомцы", true),
  EAT("Еда", true);

  String modName;
  boolean equip;

  public static final ItemEnum[] VAL = values();

  @Nullable
  public static ItemEnum getItemEnum(int id) {
    if (0 > id || id >= VAL.length) return null;
    return VAL[id];
  }
}
