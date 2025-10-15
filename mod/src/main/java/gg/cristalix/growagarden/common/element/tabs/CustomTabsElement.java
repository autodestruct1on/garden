package gg.cristalix.growagarden.common.element.tabs;

import gg.cristalix.enginex.element.Container;
import gg.cristalix.enginex.element.Text;
import gg.cristalix.enginex.math.Relative;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomTabsElement extends Container {
  final String name;
  final Text textElement;
  boolean isActive = true;

  public CustomTabsElement(String name) {
    this.name = name;

    textElement = new Text(name).setOriginAndAlign(Relative.CENTER);
    setSize(textElement.getSize().getX(), textElement.getSize().getY() + 2, 0);
    addChild(textElement);
  }

  /**
   * @param isActive Новый статус активности, если не активно, то категорию нельзя будет выбрать
   */
  public void setActive(boolean isActive) {
    this.isActive = isActive;

    setInteractive(isActive);
    textElement.setColor(textElement.getColor().setAlpha(isActive ? 1 : 0.62));
  }
}