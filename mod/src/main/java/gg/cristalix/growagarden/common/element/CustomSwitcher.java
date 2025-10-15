package gg.cristalix.growagarden.common.element;

import gg.cristalix.enginex.color.palette.Palette;
import gg.cristalix.enginex.element.AbstractElement;
import gg.cristalix.enginex.element.selector.Switcher;
import gg.cristalix.growagarden.common.menu.AbstractMenu;

import java.util.List;

public class CustomSwitcher extends Switcher {

  public CustomSwitcher(double sizeX, List<String> text) {
    this(sizeX, true, text.toArray(new String[0]));
  }

  public CustomSwitcher(double sizeX, String... text) {
    this(sizeX, true, text);
  }

  public CustomSwitcher(double sizeX, boolean instantSizeAllElement, List<String> text) {
    this(sizeX, instantSizeAllElement, text.toArray(new String[0]));
  }

  public CustomSwitcher(double sizeX, boolean instantSizeAllElement, String... text) {
    super(text);

    setSizeX(sizeX);
    setSelected(0);

    setColor(AbstractMenu.MENU_COLOR_OUTLINE);
    setSelectedColor(Palette.BLUE);

    int length = text.length;

    if (instantSizeAllElement) {
      double sizeXOneElement = size.getX() / length;
      for (int i = 0; i < length; i++)
        getElement(i).setSizeX(sizeXOneElement);
    } else {
      double maxSizeXSwitcherElement = getMaxSizeXSwitcherElement();
      double addSizeX = (size.getX() - maxSizeXSwitcherElement) / length;

      for (int i = 0; i < length; i++) {
        getElement(i).addSize(addSizeX, 0, 0);
      }
    }
  }

  private double getMaxSizeXSwitcherElement() {
    double sizeX = 0;

    for (AbstractElement<?> child : getChildren()) {
      sizeX += child.getSizeX();
    }

    return sizeX;
  }
}
