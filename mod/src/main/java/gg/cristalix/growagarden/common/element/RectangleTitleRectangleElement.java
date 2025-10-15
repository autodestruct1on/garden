package gg.cristalix.growagarden.common.element;

import gg.cristalix.enginex.color.Color;
import gg.cristalix.enginex.color.palette.Palette;
import gg.cristalix.enginex.element.Container;
import gg.cristalix.enginex.element.Rectangle;
import gg.cristalix.enginex.element.Text;
import gg.cristalix.enginex.math.Relative;
import gg.cristalix.growagarden.common.menu.AbstractMenu;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RectangleTitleRectangleElement extends Container {

  Rectangle rectangleLeft;
  Text text;
  Rectangle rectangleRight;

  public RectangleTitleRectangleElement(String value, double sizeX, Color colorRectangle) {
    this(value, sizeX, Palette.WHITE, colorRectangle);
  }

  public RectangleTitleRectangleElement(String value, double sizeX, Color colorText, Color colorRectangle) {
    this.text = createText(colorText);

    this.rectangleLeft = createRectangle(colorRectangle)
      .setOriginAndAlign(Relative.LEFT);
    this.rectangleRight = createRectangle(colorRectangle)
      .setOriginAndAlign(Relative.RIGHT);

    addChild(rectangleLeft, text, rectangleRight);

    setValue(value);
    updateSizeX(sizeX);
  }

  @Override
  public RectangleTitleRectangleElement setPosY(double posY) {
    super.setPosY(posY);
    return this;
  }

  public void setValue(String value) {
    this.text.setValue(value);
  }

  public void updateSizeX(double sizeX) {
    setSize(sizeX - AbstractMenu.DEFAULT_ELEMENT_PADDING * 2, 14, 0);
    double x = text.getSizeY();
    double newSizeX = (sizeX - AbstractMenu.DEFAULT_ELEMENT_PADDING * 2 - x - AbstractMenu.DEFAULT_SPACING_ELEMENT * 4) / 2;
    rectangleLeft.setSizeX(newSizeX);
    rectangleRight.setSizeX(newSizeX);
  }

  private Text createText(Color colorText) {
    return new Text()
      .setColor(colorText)
      .setOriginAndAlign(Relative.BOTTOM);
  }

  private Rectangle createRectangle(Color colorRectangle) {
    return new Rectangle()
      .setColor(colorRectangle)
      .setSizeY(2);
  }
}
