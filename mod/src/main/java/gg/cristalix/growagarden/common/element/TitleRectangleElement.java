package gg.cristalix.growagarden.common.element;

import gg.cristalix.enginex.color.Color;
import gg.cristalix.enginex.color.palette.Palette;
import gg.cristalix.enginex.element.AbstractElement;
import gg.cristalix.enginex.element.Rectangle;
import gg.cristalix.enginex.element.Text;
import gg.cristalix.enginex.math.Relative;
import gg.cristalix.growagarden.common.menu.AbstractMenu;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TitleRectangleElement extends AbstractElement<TitleRectangleElement> {
  Text text;
  Rectangle rectangle;

  public TitleRectangleElement(double sizeX, Color colorRectangle) {
    this("", sizeX, colorRectangle);
  }

  public TitleRectangleElement(String value, double sizeX, Color colorRectangle) {
    this(value, sizeX, Palette.WHITE, colorRectangle);
  }

  public TitleRectangleElement(String value, double sizeX, Color colorText, Color colorRectangle) {
    this.text = createText(colorText);
    this.rectangle = createRectangle(colorRectangle);

    addChild(text, rectangle);

    setValue(value);
    updateSizeX(sizeX);
  }

  public void setValue(String value) {
    this.text.setValue(value);
  }

  public void updateSizeX(double sizeX) {
    setSize(sizeX - AbstractMenu.DEFAULT_ELEMENT_PADDING * 2, 26, 0);
    rectangle.setSizeX(sizeX - AbstractMenu.DEFAULT_ELEMENT_PADDING * 2);
  }

  private Text createText(Color colorText) {
    return new Text()
      .setColor(colorText);
  }

  private Rectangle createRectangle(Color colorRectangle) {
    return new Rectangle()
      .setOriginAndAlign(Relative.BOTTOM)
      .setColor(colorRectangle)
      .setSizeY(2);
  }

  @Override
  public void render(double partialTicks, double mouseX, double mouseY) {

  }
}
