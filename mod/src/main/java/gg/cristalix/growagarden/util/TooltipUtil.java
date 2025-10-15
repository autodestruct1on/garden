package gg.cristalix.growagarden.util;

import dev.xdark.clientapi.item.ItemStack;
import dev.xdark.clientapi.item.TooltipFlag;
import gg.cristalix.enginex.Enginex;
import gg.cristalix.enginex.color.Color;
import gg.cristalix.enginex.color.palette.Palette;
import gg.cristalix.enginex.render.tooltip.custom.data.TooltipData;
import gg.cristalix.growagarden.common.util.Rarity;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class TooltipUtil {

  public TooltipData getTooltip(String name, String lore) {
    return getTooltipData(name, lore, Palette.GREEN);
  }

  public TooltipData getTooltip(ItemStack itemStack, Rarity rarity) {
    return getTooltip(itemStack, new Color(rarity.getColorHex()));
  }

  public TooltipData getTooltip(ItemStack itemStack, String addName, Color color) {
    List<String> lore = itemStack.getTooltip(Enginex.getPlayer(), TooltipFlag.NORMAL);
    String name = lore.remove(0);
    return getTooltipData(name + addName, lore, color);
  }

  public TooltipData getTooltip(ItemStack itemStack, String addName, Rarity rarity) {
    return getTooltip(itemStack, addName, new Color(rarity.getColorHex()));
  }

  public TooltipData getTooltip(ItemStack itemStack, Color color) {
    List<String> lore = itemStack.getTooltip(Enginex.getPlayer(), TooltipFlag.NORMAL);
    String name = lore.remove(0);
    return getTooltipData(name, lore, color);
  }

  public TooltipData getTooltip(List<String> lore, Rarity rarity) {
    return getTooltip(lore, new Color(rarity.getColorHex()));
  }

  public TooltipData getTooltip(String name, Collection<String> lore, Color color) {
    return getTooltipData(name, lore, color);
  }

  public TooltipData getTooltip(List<String> lore, Color color) {
    String name = lore.remove(0);
    return getTooltipData(name, lore, color);
  }

  private TooltipData getTooltipData(String name, String lore, Color color) {
    return getTooltipData(name, Collections.singletonList(lore), color);
  }

  private TooltipData getTooltipData(String name, Collection<String> lore, Color color) {
    TooltipData.TooltipDataBuilder builder = TooltipData.builder()
      .cornerColor(color)
      .title(name);
    if (!lore.isEmpty()) builder.lines(lore);
    return builder.build();
  }
}
