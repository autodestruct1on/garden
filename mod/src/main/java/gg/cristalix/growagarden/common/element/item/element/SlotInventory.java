package gg.cristalix.growagarden.common.element.item.element;


import gg.cristalix.enginex.element.button.Button;
import gg.cristalix.growagarden.common.data.item.data.ItemsData;

public interface SlotInventory {
  ItemsData getItemsData();

  boolean isDelete();

  void setDelete(boolean b);

  void setLock(boolean newLock);

  boolean isEquip();

  void setEquip(boolean b);

  Button setEnabled(boolean b);

}
