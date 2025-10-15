package gg.cristalix.growagarden.common.element;

import gg.cristalix.enginex.color.palette.ButtonColor;
import gg.cristalix.enginex.element.button.Button;
import gg.cristalix.growagarden.common.menu.AbstractMenu;
import gg.cristalix.growagarden.common.util.ModeEmoji;

public class ButtonInfo extends Button {

  public ButtonInfo() {
    super(ModeEmoji.INFO, ButtonColor.PURPLE);
    this.textElement.setPos(1, 1, 0);
    setSize(AbstractMenu.DEFAULT_BUTTON_SIZE);
  }

  public ButtonInfo(String text) {
    this();
    setTooltip(text);
  }

}
