package gg.cristalix.growagarden.utils.items;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class WateringCanUtil {
    public static final int MAX_CHARGES = 3;

    /**
     * Получает количество зарядов лейки
     */
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

    /**
     * Устанавливает количество зарядов лейки
     */
    public void setCharges(ItemStack item, int charges) {
        if (!item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();

        // Обновляем строку с зарядами
        for (int i = 0; i < lore.size(); i++) {
            String cleanLine = ChatColor.stripColor(lore.get(i));
            if (cleanLine.contains("Заряды:") || cleanLine.contains("Charges:")) {
                lore.set(i, "§7Заряды: §b" + charges + "/" + MAX_CHARGES);
                break;
            }
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    /**
     * Проверяет, является ли предмет лейкой
     */
    public boolean isWateringCan(ItemStack item) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return false;
        }

        String displayName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        return displayName.contains("Лейка");
    }

    /**
     * Использует один заряд лейки
     * @return true если заряд был использован, false если зарядов не было
     */
    public boolean useCharge(ItemStack item) {
        int currentCharges = getCharges(item);
        if (currentCharges <= 0) {
            return false;
        }

        setCharges(item, currentCharges - 1);
        return true;
    }
}