package gg.cristalix.growagarden.common.element;

import gg.cristalix.enginex.element.carved.CarvedRectangle;
import gg.cristalix.enginex.element.layout.LayoutPriority;
import gg.cristalix.enginex.element.layout.type.GridLayout;
import gg.cristalix.enginex.element.scrollview.type.VerticalScrollView;
import gg.cristalix.enginex.math.V3;
import gg.cristalix.growagarden.common.element.item.ItemInventoryElement;
import gg.cristalix.growagarden.common.menu.AbstractMenu;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryElement extends CarvedRectangle {

  final VerticalScrollView<GridLayout> inventory = new VerticalScrollView<>(GridLayout.class)
    .setPos(AbstractMenu.DEFAULT_ELEMENT_PADDING, AbstractMenu.DEFAULT_ELEMENT_PADDING, 0)
    .setColor(AbstractMenu.MENU_COLOR_OUTLINE)
    .setDisableBarOnInactive(true);

  List<ItemInventoryElement> itemsDataList = new ArrayList<>();

  final int maxElementLine;
  final double spacing;
  final V3 elementSize;
  final double sizeX;
  final double maxScrollSizeY;
  final boolean autoUpdateSizeY;

  boolean loadSize = false;

  public InventoryElement(int maxElementLine, double spacing, V3 elementSize, double sizeX, double maxScrollSizeY) {
    this(maxElementLine, spacing, elementSize, sizeX, maxScrollSizeY, false);
  }

  public InventoryElement(int maxElementLine, double spacing, V3 elementSize, double sizeX, double maxScrollSizeY, boolean autoUpdateSizeY) {
    this.maxElementLine = maxElementLine;
    this.spacing = spacing;
    this.elementSize = elementSize;
    this.sizeX = sizeX;
    this.maxScrollSizeY = maxScrollSizeY;
    this.autoUpdateSizeY = autoUpdateSizeY;

    setColor(AbstractMenu.MENU_COLOR);

    this.inventory.getLayout()
      .setPriority(LayoutPriority.HORIZONTAL)
      .setSpacing(spacing)
      .setColumns(maxElementLine);

    addChild(inventory);

    updateSize();
  }

  public void updateSize() {
    if (loadSize && !autoUpdateSizeY) return;

    if (autoUpdateSizeY) {
      int itemsSize = itemsDataList.size();
      boolean minSize = itemsSize <= maxElementLine;

      double usageLine = Math.ceil((double) itemsSize / maxElementLine);
      double elementSizeY = elementSize.getY();
      double currentMaxSize = usageLine * elementSizeY + (usageLine - 1) * spacing;

      boolean maxSize = currentMaxSize > maxScrollSizeY;

      this.inventory.setSize(
        sizeX - AbstractMenu.DEFAULT_ELEMENT_PADDING * 2 - 10,
        maxSize ? maxScrollSizeY : minSize ? elementSizeY : currentMaxSize,
        0
      );
    } else {
      this.inventory.setSize(sizeX - AbstractMenu.DEFAULT_ELEMENT_PADDING * 2 - 10, maxScrollSizeY, 0);
    }

    setSize(sizeX, inventory.getSizeY() + AbstractMenu.DEFAULT_ELEMENT_PADDING * 2, 0);

    loadSize = true;
  }

  public void update() {
    inventory.clearChildren();

    for (ItemInventoryElement itemsData : itemsDataList) {
      if (itemsData.isEquip()) continue;
      inventory.addChild(itemsData);
    }
  }

  public void load(List<ItemInventoryElement> itemInventoryElements) {
    inventory.clearChildren();

    itemsDataList = itemInventoryElements;
    update();
    updateSize();
  }
}
