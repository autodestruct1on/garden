package gg.cristalix.growagarden.common.element;

import gg.cristalix.enginex.color.Color;
import gg.cristalix.enginex.element.Text;
import gg.cristalix.enginex.element.carved.CarvedRectangle;
import gg.cristalix.enginex.math.Relative;
import gg.cristalix.enginex.math.V3;
import gg.cristalix.growagarden.common.menu.AbstractMenu;
import gg.cristalix.growagarden.common.util.ModeEmoji;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotifyElement extends CarvedRectangle {

  Text emojiElement = new Text(ModeEmoji.INFO)
    .setOriginAndAlign(Relative.LEFT)
    .setPosX(AbstractMenu.DEFAULT_ELEMENT_PADDING)
    .setScale(1.5);

  Text textInfo = new Text()
    .setOriginAndAlign(Relative.LEFT)
    .setPosX(60);

  public NotifyElement(String... text) {
    addChild(emojiElement, textInfo);

    this.textInfo.setValue(text);
  }

  public NotifyElement updateSize(double sizeX) {
    textInfo.setAutoSkip(sizeX - 60 - AbstractMenu.DEFAULT_ELEMENT_PADDING);
    setSize(sizeX, AbstractMenu.DEFAULT_ELEMENT_PADDING * 2 + Math.max(emojiElement.getSizeY(), textInfo.getSizeY()), 0);
    return this;
  }

  @Override
  public NotifyElement setColor(Color color) {
    return (NotifyElement) super.setColor(color);
  }

  @Override
  public NotifyElement setOriginAndAlign(V3 relative) {
    return (NotifyElement) super.setOriginAndAlign(relative);
  }

  @Override
  public NotifyElement setPosX(double posX) {
    return (NotifyElement) super.setPosX(posX);
  }

  @Override
  public NotifyElement setPosY(double posY) {
    return (NotifyElement) super.setPosY(posY);
  }
}
