package gg.cristalix.growagarden.utils.items;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ShovelUtil {

    public static final int MAX_CHARGES = 1;

    public int getCharges(ItemStack item) {
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
            return 0;
        }

        List<String> lore = item.getItemMeta().getLore();
        for (String line : lore) {
            String cleanLine = ChatColor.stripColor(line);
            if (cleanLine.contains("Заряды:") || cleanLine.contains("Charges:")) {
                try {
                    String[] parts = cleanLine.split(":")[1].trim().split("/");
                    return Integer.parseInt(parts[0].trim());
                } catch (Exception e) {
                    return 0;
                }
            }
        }
        return 0;
    }

    public void setCharges(ItemStack item, int charges) {
        if (!item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();

        for (int i = 0; i < lore.size(); i++) {
            String cleanLine = ChatColor.stripColor(lore.get(i));
            if (cleanLine.contains("Заряды:") || cleanLine.contains("Charges:")) {
                lore.set(i, "§7Заряды: §e" + charges + "/" + MAX_CHARGES);
                break;
            }
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public boolean isShovel(ItemStack item) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return false;
        }

        String displayName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        return displayName.contains("Лопата");
    }

    public boolean useCharge(ItemStack item) {
        int currentCharges = getCharges(item);
        if (currentCharges <= 0) {
            return false;
        }

        setCharges(item, currentCharges - 1);
        return true;
    }
}