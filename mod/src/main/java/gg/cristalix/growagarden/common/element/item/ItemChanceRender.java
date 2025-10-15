package gg.cristalix.growagarden.common.element.item;

import dev.xdark.clientapi.item.ItemStack;
import gg.cristalix.enginex.color.Color;
import gg.cristalix.enginex.color.palette.ButtonColor;
import gg.cristalix.enginex.element.Text;
import gg.cristalix.enginex.element.button.Button;
import gg.cristalix.enginex.element.carved.CarvedRectangle;
import gg.cristalix.enginex.element.item.ItemRender;
import gg.cristalix.enginex.event.tick.PostRenderEvent;
import gg.cristalix.enginex.math.Relative;
import gg.cristalix.growagarden.common.data.item.data.ItemsData;
import gg.cristalix.growagarden.common.menu.AbstractMenu;
import gg.cristalix.growagarden.common.util.TextUtil;
import gg.cristalix.growagarden.util.TooltipUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class ItemChanceRender extends Button {
  final ItemRender itemRender;
  final Text chanceText;

  final CarvedRectangle chanceCarved;
  ItemsData itemsData;

  public ItemChanceRender() {
    setButtonColor(AbstractMenu.MENU_COLOR_BUTTON);
    setOriginAndAlign(Relative.CENTER);

    setOutlineGradientHeight(0.5);

    this.chanceCarved = new CarvedRectangle()
      .setOriginAndAlign(Relative.BOTTOM)
      .setColor(AbstractMenu.MENU_COLOR)
      .setCarveCorners(false, false, true, true)
      .setCarveOutlines(true, false, false, false)
      .setOutlineColor(AbstractMenu.MENU_COLOR_OUTLINE)
      .setPosY(-2);

    this.chanceText = new Text()
      .setOriginAndAlign(Relative.CENTER);
    chanceCarved.addChild(chanceText);

    itemRender = new ItemRender()
      .setSize(64, 64, 0)
      .setPosY(14)
      .setOriginAndAlign(Relative.TOP);

    itemRender.registerEvent(PostRenderEvent.class, event -> {
      if (getAbsoluteAlpha() < 1) itemRender.setEnabled(false);
    });

    addChild(itemRender, chanceCarved);
  }

  public ItemChanceRender setSizeAll(double x, double y, double z) {
    super.setSize(x, y, z);
    chanceCarved.setSize(x - 4, 28, z);
    return this;
  }

  public ItemChanceRender setItemsData(@NotNull ItemsData itemsData) {
    this.itemsData = itemsData;

    ItemStack itemStack = itemsData.getItemStack();
    setItem(itemStack);

    Color color = new Color(itemsData.getRarity().getColorHex());
    setChanceText(TextUtil.parseNumberChance(itemsData.getChance()));

    setTooltip(TooltipUtil.getTooltip(itemStack, color));

    setOutlineGradientColor(color);

    return this;
  }

  public void setChanceText(String chance) {
    this.chanceText.setValue(chance.length() >= 7 ? chance : (chance + "%"));
  }

  public void setItem(ItemStack item) {
    itemRender.setItem(item);
  }

  public void setEquip(boolean equip) {
    getItemsData().setEquip(equip);
    setButtonColorInstant(isEquip() ? ButtonColor.BLUE_62 : ButtonColor.GREY_62);
  }

  public boolean isEquip() {
    return getItemsData().isEquip();
  }

}
