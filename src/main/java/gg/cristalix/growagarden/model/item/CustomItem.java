package gg.cristalix.growagarden.model.item;

import gg.cristalix.growagarden.common.mod.inventory.ItemEnum;
import gg.cristalix.growagarden.utils.NBT;
import gg.cristalix.wada.color.Color;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class CustomItem {

  final UUID uuid = UUID.randomUUID();
  final String itemId;

  int amount = 1;

  transient String displayName;
  transient Material material;
  transient Map<String, String> nbt;
  transient String lore;
  transient ItemEnum itemEnum;
  transient ItemStack itemStack;

  protected CustomItem(String itemId) {
    this.itemId = itemId;
  }

  public CustomItem load(CustomItemData customItemData) {
    this.displayName = customItemData.getDisplayName();
    this.material = Material.valueOf(customItemData.getMaterial());
    this.nbt = customItemData.getNbt();
    this.lore = customItemData.getLore();
    this.itemEnum = customItemData.getItemEnum();

    return this;
  }

  public ItemStack getItemStack() {
    if (this.itemStack != null) return this.itemStack;

    this.itemStack = createItemStack();
    return itemStack;
  }

  public ItemStack getCloneItemStack() {
    return createItemStack();
  }

  protected ItemStack createItemStack() {
    ItemStack itemStack = new ItemStack(material);

    NBT.setString(itemStack, "itemId", itemId);
    NBT.setString(itemStack, "itemEnum", itemEnum.toString());

    ItemMeta itemMeta = itemStack.getItemMeta();

    itemMeta.setUnbreakable(true);
    itemMeta.addItemFlags(
      ItemFlag.HIDE_ATTRIBUTES,
      ItemFlag.HIDE_UNBREAKABLE,
      ItemFlag.HIDE_ENCHANTS
    );

    itemStack.setItemMeta(itemMeta);

    itemStack = setTexture(itemStack);
    itemStack = setName(itemStack);
    itemStack = setLore(itemStack);

    return itemStack;
  }

  protected ItemStack setTexture(ItemStack itemStack) {
    if (nbt == null) return itemStack;

    for (Map.Entry<String, String> entry : nbt.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      if (key == null || value == null) continue;

      if (key.equals("color")) NBT.setInt(itemStack, "color", new Color(value).getDecimal());
      else NBT.setString(itemStack, key, value);
    }

    return itemStack;
  }

  protected ItemStack setName(ItemStack itemStack) {
    ItemMeta itemMeta = itemStack.getItemMeta();

    itemMeta.setDisplayName(displayName);
    itemStack.setItemMeta(itemMeta);

    return itemStack;
  }

  protected ItemStack setLore(ItemStack itemStack) {
    return itemStack;
  }

  public void increaseAmount(int amount) {
    this.amount += amount;
  }

  public void decreaseAmount(int amount) {
    this.amount = Math.max(0, this.amount - amount);
  }

  public boolean matchesUUID(UUID uuid) {
    return this.uuid.equals(uuid);
  }
}
