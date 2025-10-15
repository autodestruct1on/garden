package gg.cristalix.growagarden.common.element;

import gg.cristalix.enginex.element.AbstractElement;
import gg.cristalix.enginex.math.Relative;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InfoElement<T extends AbstractElement<T>, B extends AbstractElement<B>> extends AbstractElement<InfoElement<T, B>> {

  T leftElement;
  B rightElement;

  public InfoElement(double sizeX, T leftElement, B rightElement) {
    this.leftElement = leftElement;
    this.rightElement = rightElement;

    leftElement.setOriginAndAlign(Relative.LEFT);
    rightElement.setOriginAndAlign(Relative.RIGHT);

    addChild(leftElement, rightElement);

    updateSize(sizeX);
  }

  public void updateSize(double sizeX) {
    setSize(sizeX, Math.max(Math.max(leftElement.getSizeY(), rightElement.getSizeY()), 16), 0);
  }

  @Override
  public void render(double partialTicks, double mouseX, double mouseY) {

  }
}
