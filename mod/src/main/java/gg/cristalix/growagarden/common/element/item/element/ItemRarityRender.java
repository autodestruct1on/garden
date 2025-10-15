package gg.cristalix.growagarden.common.element.item.element;

import dev.xdark.clientapi.item.ItemStack;
import dev.xdark.clientapi.opengl.GlStateManager;
import gg.cristalix.enginex.color.Color;
import gg.cristalix.enginex.color.palette.ButtonColor;
import gg.cristalix.enginex.element.Text;
import gg.cristalix.enginex.element.button.Button;
import gg.cristalix.enginex.element.item.ItemRender;
import gg.cristalix.enginex.event.tick.PostRenderEvent;
import gg.cristalix.enginex.event.tick.PreRenderEvent;
import gg.cristalix.enginex.math.Relative;
import gg.cristalix.enginex.render.tooltip.custom.data.TooltipData;
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
public abstract class ItemRarityRender extends Button implements SlotInventory {

  final ItemRender itemRender;

  Text lockElement;
  Text enchantElement;
  Text textLevel;

  ItemsData itemsData;

  boolean lock = false;
  boolean enchant = false;
  boolean delete = false;

  protected ItemRarityRender(boolean enchant, boolean lock, boolean levelOrCount) {
    this.enchant = enchant;

    setButtonColor(AbstractMenu.MENU_COLOR_BUTTON);
    setOriginAndAlign(Relative.CENTER);
    setOutlineGradientHeight(0.5);
    setInteractive(false);

    itemRender = new ItemRender()
      .setSize(32, 32, 0)
      .setOriginAndAlign(Relative.CENTER);

    addChild(itemRender);

    if (lock) {
      lockElement = new Text("\uE0F4")
        .setScale(0.52, 0.52, 0)
        .setPos(6, 6, 0)
        .setEnabled(false);

      lockElement.registerEvent(PreRenderEvent.class, event -> GlStateManager.disableDepth());
      lockElement.registerEvent(PostRenderEvent.class, event -> GlStateManager.enableDepth());

      addChild(lockElement);
    }

    if (levelOrCount) {
      textLevel = new Text()
        .setOriginAndAlign(Relative.BOTTOM_RIGHT)
        .setScale(0.82, 0.82, 0)
        .setPos(-6, -6, 0);

      textLevel.registerEvent(PreRenderEvent.class, event -> GlStateManager.disableDepth());
      textLevel.registerEvent(PostRenderEvent.class, event -> GlStateManager.enableDepth());

      addChild(textLevel);
    }

  }

  @Override
  public Button setCarveCorners(Boolean... carveCorners) {
    return super.setCarveCorners(carveCorners);
  }

  public ItemRarityRender setSizeAll(double x, double y, double z) {
    super.setSize(x, y, z);
    return this;
  }

  public ItemRarityRender setItemsData(@NotNull ItemsData itemsData) {
    this.itemsData = itemsData;

    ItemStack itemStack = itemsData.getItemStack();
    setItem(itemStack);

    Color color = new Color(itemsData.getRarity().getColorHex());

    boolean isEnchant = this.enchant && itemsData.isEnchant();
    if (textLevel != null) {
      textLevel.setEnabled(true).setValue((isEnchant ? "&k" : "") + TextUtil.parseNumber(itemsData.getCount(), 0));
    }

    setInteractive(true);

    setLore(itemsData);
    postItemsData(itemsData);
    setOutlineGradientColor(color);
    return this;
  }

  public void postItemsData(ItemsData itemsData) {

  }

  public void setLore(ItemsData itemsData) {
    Color color = new Color(itemsData.getRarity().getColorHex());
    setTooltip(TooltipUtil.getTooltip(itemsData.getItemStack(), color));
  }

  public void setItem(ItemStack item) {
    itemRender.setItem(item);
  }

  public void setLock(boolean lock) {
    this.lock = lock;
    if (lockElement != null) lockElement.setEnabled(lock);
  }

  public void setDelete(boolean delete) {
    setButtonColorInstant(delete ? ButtonColor.GREEN_62 : AbstractMenu.MENU_COLOR_BUTTON);
    this.delete = delete;
  }

  public void setEquip(boolean equip) {
    getItemsData().setEquip(equip);
    setButtonColorInstant(equip ? ButtonColor.BLUE_62 : AbstractMenu.MENU_COLOR_BUTTON);
  }

  public boolean isEquip() {
    return getItemsData().isEquip();
  }

  public void clear() {
    itemsData = null;
    setItem(null);
    if (textLevel != null) textLevel.setEnabled(false);
    setTooltip((TooltipData) null);
    setInteractive(false);
    setOutlineGradientColor(null);
  }
}
