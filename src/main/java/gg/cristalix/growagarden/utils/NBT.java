package gg.cristalix.growagarden.utils;

import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;

@UtilityClass
public class NBT {

  public Map<String, NBTBase> getNBT0(ItemStack item, boolean create) {
    try {
      if (item instanceof CraftItemStack) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = ((CraftItemStack) item).handle;
        if (nmsItem.hasTag()) {
          return nmsItem.tag.map;
        } else {
          return create ? (nmsItem.tag = new NBTTagCompound()).map : Collections.emptyMap();
        }
      } else {
        if (create && item.meta == null) {
          item.meta = Bukkit.getItemFactory().getItemMeta(item.getType());
        }

        return item.meta != null ? ((CraftMetaItem) item.meta).unhandledTags : Collections.emptyMap();
      }
    } catch (Exception var3) {
      return Collections.emptyMap();
    }
  }

  public Map<String, NBTBase> getNBT0(ItemStack item) {
    try {
      if (item instanceof CraftItemStack) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = ((CraftItemStack) item).handle;
        return nmsItem.hasTag() ? nmsItem.tag.map : (nmsItem.tag = new NBTTagCompound()).map;
      } else {
        if (item.meta == null) {
          item.meta = Bukkit.getItemFactory().getItemMeta(item.getType());
        }

        return ((CraftMetaItem) item.meta).unhandledTags;
      }
    } catch (Exception var2) {
      return Collections.emptyMap();
    }
  }

  public Map<String, NBTBase> getNBT(ItemMeta meta) {
    if (meta == null) {
      return Collections.emptyMap();
    } else {
      try {
        return ((CraftMetaItem) meta).unhandledTags;
      } catch (Exception var2) {
        var2.printStackTrace();
        return Collections.emptyMap();
      }
    }
  }

  public ItemStack setByte(ItemStack item, String key, byte value) {
    getNBT0(item).put(key, new NBTTagByte(value));
    return item;
  }

  public ItemStack setBoolean(ItemStack item, String tag, boolean value) {
    getNBT0(item).put(tag, new NBTTagInt(value ? 1 : 0));
    return item;
  }

  public boolean getBoolean(ItemStack item, String tag) {
    NBTBase nbt = getNBT0(item).get(tag);
    return nbt != null && ((NBTTagInt) nbt).e() == 1;
  }

  public ItemStack setString(ItemStack item, String tag, String value) {
    getNBT0(item).put(tag, new NBTTagString(value));
    return item;
  }

  public void setDouble(ItemStack item, String tag, double value) {
    setNBTBase(item, tag, new NBTTagDouble(value));
  }

  public void setInt(ItemStack item, String tag, int value) {
    setNBTBase(item, tag, new NBTTagInt(value));
  }

  public void setLong(ItemStack item, String tag, long value) {
    setNBTBase(item, tag, new NBTTagLong(value));
  }

  public void setNBTBase(ItemStack item, String tag, NBTBase value) {
    getNBT0(item, true).put(tag, value);
  }

  public NBTBase getNBTBase(ItemStack item, String tag) {
    return getNBT0(item, false).get(tag);
  }

  public NBTBase getNBTBase(ItemStack item, String tag, boolean create) {
    return getNBT0(item, create).get(tag);
  }

  public void setInt(ItemMeta meta, String tag, int value) {
    getNBT(meta).put(tag, new NBTTagInt(value));
  }

  public void remove(ItemStack item, String key) {
    getNBT0(item).remove(key);
  }

  public void remove(ItemMeta meta, String key) {
    getNBT(meta).remove(key);
  }

  public String getString(ItemStack item, String tag) {
    if (item.hasItemMeta() && tag != null) {
      NBTTagString nbtTagString = (NBTTagString) getNBT(item.getItemMeta()).get(tag);
      return nbtTagString == null ? null : nbtTagString.c_();
    } else {
      return null;
    }
  }

  public boolean hasTag0(ItemStack item, String tag) {
    return getNBTTag(item).hasKey(tag);
  }

  public boolean hasTag(ItemStack item, String tag) {
    return item != null && item.hasItemMeta() && getNBT0(item).containsKey(tag);
  }

  public boolean hasTag(ItemMeta meta, String tag) {
    return meta != null && getNBT(meta).containsKey(tag);
  }

  public double getDouble(ItemMeta meta, String tag) {
    NBTNumber base = getNbtNumber(meta, tag);
    return base == null ? 0.0 : base.asDouble();
  }

  public double getDouble(ItemStack item, String tag) {
    return getDouble(item.getItemMeta(), tag);
  }

  public int getInt(ItemStack item, String tag) {
    return getInt(item.getItemMeta(), tag);
  }

  public int getInt(ItemMeta meta, String tag) {
    NBTNumber base = getNbtNumber(meta, tag);
    return base == null ? 0 : base.e();
  }

  public short getShort(ItemStack item, String tag) {
    return getShort(item.getItemMeta(), tag);
  }

  public short getShort(ItemMeta meta, String tag) {
    NBTNumber base = getNbtNumber(meta, tag);
    return base == null ? 0 : base.f();
  }

  @Nullable
  public NBTNumber getNbtNumber(ItemMeta meta, String tag) {
    NBTBase base = getNBT(meta).get(tag);
    return base instanceof NBTNumber ? (NBTNumber) base : null;
  }

  public byte getByte(ItemStack item, String tag) {
    NBTBase base = getNBT0(item).get(tag);
    return base == null ? 0 : ((NBTTagByte) base).g();
  }

  public NBTTagCompound getNBTTag(ItemStack item) {
    return getNBTTag(CraftItemStack.asNMSCopy(item));
  }

  public NBTTagCompound getNBTTag(net.minecraft.server.v1_12_R1.ItemStack item) {
    return item.getTag() == null ? new NBTTagCompound() : item.getTag();
  }

}