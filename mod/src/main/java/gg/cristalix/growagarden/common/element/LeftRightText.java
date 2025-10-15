package gg.cristalix.growagarden.common.element;

import gg.cristalix.enginex.color.Color;
import gg.cristalix.enginex.element.AbstractElement;
import gg.cristalix.enginex.element.Text;
import gg.cristalix.enginex.math.Relative;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeftRightText extends AbstractElement<LeftRightText> {

  Text textLeft = new Text().setOriginAndAlign(Relative.LEFT);
  Text textRight = new Text().setOriginAndAlign(Relative.RIGHT);

  public LeftRightText() {
    addChild(textLeft, textRight);
  }

  public LeftRightText setColorLeft(Color color) {
    this.textLeft.setColor(color);
    return this;
  }

  public LeftRightText setColorRight(Color color) {
    this.textRight.setColor(color);
    return this;
  }

  public LeftRightText setLeftValue(String leftValue) {
    this.textLeft.setValue(leftValue);
    return this;
  }

  public LeftRightText setRightValue(String rightValue) {
    this.textRight.setValue(rightValue);
    return this;
  }

  @Override
  public void render(double partialTicks, double mouseX, double mouseY) {

  }
}
