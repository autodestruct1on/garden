package gg.cristalix.growagarden.common.element.item;

import gg.cristalix.growagarden.common.data.item.data.ItemsData;
import gg.cristalix.growagarden.common.element.item.element.ItemRarityRender;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemInventoryElement extends ItemRarityRender {


  public ItemInventoryElement(ItemsData itemsData) {
    this();
    setItemsData(itemsData);
  }

  public ItemInventoryElement() {
    super(true, false, true);

    setSizeAll(64, 64, 0);
    itemRender.setSize(56, 56, 0);
  }

  public boolean isEmpty() {
    return itemsData == null;
  }

  public void updateLore() {
    setLore(itemsData);
  }

//  @Override
//  public void setLore(ItemsData itemsData) {
//    if (itemsData == null) return;
//    List<String> lore = itemsData.getItemStack().getTooltip(Enginex.getPlayer(), TooltipFlag.NORMAL);
//
//    lore.add("");
//    lore.add(ModeEmoji.MOUSE_LEFT + " " + (itemsData.isEquip() ? "Убрать" : "Выбрать"));
//
//    setTooltip(TooltipUtil.getTooltip(lore, itemsData.getRarity()));
//  }
}
