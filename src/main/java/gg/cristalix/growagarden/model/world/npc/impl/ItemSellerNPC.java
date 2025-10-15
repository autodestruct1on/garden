package gg.cristalix.growagarden.model.world.npc.impl;

import gg.cristalix.growagarden.GrowAGardenPlugin;
import gg.cristalix.growagarden.common.util.TextUtil;
import gg.cristalix.growagarden.model.item.ItemCustomItem;
import gg.cristalix.growagarden.model.player.GamePlayer;
import gg.cristalix.growagarden.model.player.economy.CurrencyType;
import gg.cristalix.growagarden.model.world.npc.IWorldNPC;
import gg.cristalix.growagarden.model.world.npc.NPCData;
import gg.cristalix.growagarden.service.alert.AlertService;
import gg.cristalix.growagarden.service.hud.HudService;
import gg.cristalix.growagarden.service.inventory.InventoryService;
import gg.cristalix.wada.component.menu.selection.common.Selection;
import gg.cristalix.wada.component.menu.selection.common.SelectionButton;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemSellerNPC extends IWorldNPC {

    static double WATERING_CAN_PRICE = 50.0;
    static double SHOVEL_PRICE = 30.0;

    GrowAGardenPlugin plugin;

    public ItemSellerNPC(Location location) {
        super(NPCData.of("§eПродавец инструментов", "", location));
        this.plugin = GrowAGardenPlugin.getInstance();
    }

    @Override
    protected void open(Player player) {
        Selection mainMenu = createMainMenu(player);
        menuManager.open(mainMenu, player);
    }

    private Selection createMainMenu(Player player) {
        GamePlayer gamePlayer = player.getBungeePlayer();
        double balance = gamePlayer != null ? gamePlayer.getBalance(CurrencyType.MONEY) : 0.0;

        SelectionButton wateringCanButton = createWateringCanButton(player);
        SelectionButton shovelButton = createShovelButton(player);

        return Selection.builder()
                .title("§eМагазин инструментов")
                .balance(TextUtil.parseNumber(balance, 2))
                .balanceSymbol("§6монет")
                .rows(2)
                .columns(2)
                .buttons(wateringCanButton, shovelButton)
                .build();
    }

    private SelectionButton createWateringCanButton(Player player) {
        ItemStack wateringCanItem = createWateringCanItemStack();

        return SelectionButton.builder()
                .headerText("§bЛейка")
                .subText("§7Поливает растения")
                .itemIcon(wateringCanItem)
                .price((int) WATERING_CAN_PRICE)
                .formattedPrice("монет")
                .onPlayerLeftClick((clickedPlayer, button) -> handleWateringCanPurchase(clickedPlayer))
                .build();
    }

    private SelectionButton createShovelButton(Player player) {
        ItemStack shovelItem = createShovelItemStack();

        return SelectionButton.builder()
                .headerText("§eЛопата")
                .subText("§7Выкапывает растения")
                .itemIcon(shovelItem)
                .price((int) SHOVEL_PRICE)
                .formattedPrice("монет")
                .onPlayerLeftClick((clickedPlayer, button) -> handleShovelPurchase(clickedPlayer))
                .build();
    }

    private void handleWateringCanPurchase(Player player) {
        GamePlayer gamePlayer = player.getBungeePlayer();

        if (gamePlayer == null) {
            AlertService.sendError(player, "§cОшибка: данные игрока не найдены");
            return;
        }

        if (!gamePlayer.hasBalance(CurrencyType.MONEY, WATERING_CAN_PRICE)) {
            AlertService.sendError(player, "Недостаточно средств! Нужно: §6" +
                    TextUtil.parseNumber(WATERING_CAN_PRICE, 0) + " монет");
            return;
        }

        ItemCustomItem wateringCan = (ItemCustomItem) plugin.getItemService().createItem("watering_can");

        if (wateringCan == null) {
            AlertService.sendError(player, "§cОшибка: не удалось создать лейку");
            return;
        }

        if (!InventoryService.addItem(gamePlayer, wateringCan)) {
            AlertService.sendError(player, "Инвентарь переполнен! Освободите место.");
            return;
        }

        gamePlayer.removeBalance(CurrencyType.MONEY, WATERING_CAN_PRICE);

        AlertService.sendSuccess(player, "Куплено: §bЛейка");
        AlertService.sendTransaction(player, "§7-§6" +
                TextUtil.parseNumber(WATERING_CAN_PRICE, 0) + " монет", 2.0);

        HudService.updateHud(player);
        menuManager.close(player);
    }

    private void handleShovelPurchase(Player player) {
        GamePlayer gamePlayer = player.getBungeePlayer();

        if (gamePlayer == null) {
            AlertService.sendError(player, "§cОшибка: данные игрока не найдены");
            return;
        }

        if (!gamePlayer.hasBalance(CurrencyType.MONEY, SHOVEL_PRICE)) {
            AlertService.sendError(player, "Недостаточно средств! Нужно: §6" +
                    TextUtil.parseNumber(SHOVEL_PRICE, 0) + " монет");
            return;
        }

        ItemCustomItem shovel = (ItemCustomItem) plugin.getItemService().createItem("shovel");

        if (shovel == null) {
            AlertService.sendError(player, "§cОшибка: не удалось создать лопату");
            return;
        }

        if (!InventoryService.addItem(gamePlayer, shovel)) {
            AlertService.sendError(player, "Инвентарь переполнен! Освободите место.");
            return;
        }

        gamePlayer.removeBalance(CurrencyType.MONEY, SHOVEL_PRICE);

        AlertService.sendSuccess(player, "Куплено: §eЛопата");
        AlertService.sendTransaction(player, "§7-§6" +
                TextUtil.parseNumber(SHOVEL_PRICE, 0) + " монет", 2.0);

        HudService.updateHud(player);
        menuManager.close(player);
    }

    private ItemStack createWateringCanItemStack() {
        ItemStack item = new ItemStack(Material.CLAY_BALL, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§bЛейка");

        List<String> lore = new ArrayList<>();
        lore.add("§7Заряды: §b3/3");
        lore.add("§7Используется для полива растений");
        lore.add("§7Ускоряет рост на 10% до сбора урожая");
        lore.add("");
        lore.add("§8ПКМ по грядке для полива");

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack createShovelItemStack() {
        ItemStack item = new ItemStack(Material.CLAY_BALL, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§eЛопата");

        List<String> lore = new ArrayList<>();
        lore.add("§7Заряды: §e1/1");
        lore.add("§7Используется для выкапывания растений");
        lore.add("");
        lore.add("§8ПКМ по грядке для выкапывания");
        lore.add("§cСемена не возвращаются");

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }
}