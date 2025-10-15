package gg.cristalix.growagarden.model.world.npc.impl;

import gg.cristalix.growagarden.GrowAGardenPlugin;
import gg.cristalix.growagarden.common.util.TextUtil;
import gg.cristalix.growagarden.model.garden.vegetation.SeedData;
import gg.cristalix.growagarden.model.item.SeedCustomItem;
import gg.cristalix.growagarden.model.player.GamePlayer;
import gg.cristalix.growagarden.model.player.economy.CurrencyType;
import gg.cristalix.growagarden.model.world.npc.IWorldNPC;
import gg.cristalix.growagarden.model.world.npc.NPCData;
import gg.cristalix.growagarden.service.alert.AlertService;
import gg.cristalix.growagarden.service.hud.HudService;
import gg.cristalix.growagarden.service.inventory.InventoryService;
import gg.cristalix.growagarden.service.network.ModSyncService;
import gg.cristalix.wada.component.menu.selection.common.Selection;
import gg.cristalix.wada.component.menu.selection.common.SelectionButton;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeedSellerNPC extends IWorldNPC {

  public SeedSellerNPC(Location location) {
    super(NPCData.of("§aПродавец семян", "", location));
  }

  @Override
  protected void open(Player player) {
    Selection mainMenu = createMainMenu(player);
    menuManager.open(mainMenu, player);
  }

  private Selection createMainMenu(Player player) {
    GamePlayer gamePlayer = player.getBungeePlayer();
    double balance = gamePlayer != null ? gamePlayer.getBalance(CurrencyType.MONEY) : 0.0;

    Map<String, List<SelectionButton>> categorizedButtons = new HashMap<>();
    categorizedButtons.put("BASE", new ArrayList<>());
    categorizedButtons.put("TREE", new ArrayList<>());
    categorizedButtons.put("EXOTIC", new ArrayList<>());

    Map<String, SeedData> seeds = GrowAGardenPlugin.getInstance().getSeedService().getSeedDataMap();
    for (SeedData seed : seeds.values()) {
      SelectionButton button = createSeedButton(seed, player);
      categorizedButtons.get(seed.getCategory().name()).add(button);
    }

    List<SelectionButton> allButtons = new ArrayList<>();
    allButtons.addAll(categorizedButtons.get("BASE"));
    allButtons.addAll(categorizedButtons.get("TREE"));
    allButtons.addAll(categorizedButtons.get("EXOTIC"));

    Selection.Builder builder = Selection.builder()
            .title("§aМагазин семян")
            .balance(TextUtil.parseNumber(balance, 2))
            .balanceSymbol("§6монет")
            .rows(4)
            .columns(3);

    for (SelectionButton button : allButtons) {
      builder.buttons(button);
    }

    return builder.build();
  }

  private SelectionButton createSeedButton(SeedData seedData, Player player) {
    double price = GrowAGardenPlugin.getInstance().getSeedService().calculateSeedPrice(seedData);
    ItemStack seedItem = createSeedItemStack(seedData);
    String categoryColor = seedData.getCategory().getColorCode();
    String growTimeText = formatGrowTime(seedData.getGrowTimeMillis());

    return SelectionButton.builder()
            .headerText(categoryColor + seedData.getName())
            .subText("§7" + growTimeText)
            .itemIcon(seedItem)
            .price((int) price)
            .formattedPrice("монет")
            .onPlayerLeftClick((clickedPlayer, button) -> {
              handleSeedPurchase(clickedPlayer, seedData, price);
            })
            .build();
  }

  private void handleSeedPurchase(Player player, SeedData seedData, double price) {
    GamePlayer gamePlayer = player.getBungeePlayer();

    if (gamePlayer == null) {
      AlertService.sendError(player, "§cОшибка: данные игрока не найдены");
      return;
    }

    if (!gamePlayer.hasBalance(CurrencyType.MONEY, price)) {
      AlertService.sendError(player, "Недостаточно средств! Нужно: §6" +
              TextUtil.parseNumber(price, 0) + " монет");
      return;
    }

    SeedCustomItem seed = (SeedCustomItem) GrowAGardenPlugin.getInstance()
            .getItemService().createItem(seedData.getId());

    if (seed == null) {
      AlertService.sendError(player, "§cОшибка: не удалось создать семя");
      return;
    }

    if (!InventoryService.addSeed(gamePlayer, seed)) {
      AlertService.sendError(player, "Инвентарь переполнен! Освободите место.");
      return;
    }

    gamePlayer.removeBalance(CurrencyType.MONEY, price);

    AlertService.sendSuccess(player, "Куплено: " + seedData.getName());
    AlertService.sendTransaction(player, "§7-§6" + TextUtil.parseNumber(price, 0) + " монет", 2.0);

    HudService.updateHud(player);
    ModSyncService.syncInventory(player);

    menuManager.close(player);
  }

  private String formatGrowTime(long millis) {
    long minutes = millis / 60000;
    if (minutes >= 60) {
      long hours = minutes / 60;
      long remainingMinutes = minutes % 60;
      return hours + "ч " + remainingMinutes + "м";
    }
    return minutes + "м";
  }

  private ItemStack createSeedItemStack(SeedData seedData) {
    ItemStack item = new ItemStack(Material.CLAY_BALL, 1);
    ItemMeta meta = item.getItemMeta();

    meta.setDisplayName(seedData.getName());

    List<String> lore = new ArrayList<>();
    lore.add("§7Категория: §f" + seedData.getCategory().getDisplayName());
    lore.add("§7Стадии роста: §f" + seedData.getStages());
    lore.add("§7Время роста: §f" + (seedData.getGrowTimeMillis() / 60000) + " мин");
    lore.add("§7Вес плода: §f" + String.format("%.1f-%.1f", seedData.getBaseWeightKgMin(), seedData.getBaseWeightKgMax()) + " кг");
    lore.add("§7Базовая цена плода: §6" + TextUtil.parseNumber(seedData.getBaseValue(), 0) + " монет/кг");

    if (seedData.isMultiHarvest()) {
      lore.add("§aМожно собирать многократно");
    } else {
      lore.add("§cОднократный сбор");
    }

    lore.add("");
    lore.add("§8ПКМ по грядке для посадки");

    meta.setLore(lore);
    item.setItemMeta(meta);

    return item;
  }
}