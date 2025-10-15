package gg.cristalix.growagarden.model.world.npc.impl;

import gg.cristalix.growagarden.GrowAGardenPlugin;
import gg.cristalix.growagarden.common.util.TextUtil;
import gg.cristalix.growagarden.model.garden.vegetation.SeedData;
import gg.cristalix.growagarden.model.item.CropCustomItem;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CropBuyerNPC extends IWorldNPC {

  private static final String NPC_DISPLAY_NAME = "§6Скупщик урожая";

  public CropBuyerNPC(Location location) {
    super(NPCData.of(NPC_DISPLAY_NAME, "", location));
  }

  @Override
  protected void open(Player player) {
    Selection mainMenu = createMainMenu(player);
    menuManager.open(mainMenu, player);
  }

  private Selection createMainMenu(Player player) {
    GamePlayer gamePlayer = player.getBungeePlayer();
    double balance = gamePlayer != null ? gamePlayer.getBalance(CurrencyType.MONEY) : 0.0;

    SelectionButton sellAllButton = createSellAllButton();
    SelectionButton pricesButton = createPricesButton();

    return Selection.builder()
            .title("§6Скупщик урожая")
            .balance(TextUtil.parseNumber(balance, 2))
            .balanceSymbol("§6монет")
            .rows(2)
            .columns(2)
            .buttons(sellAllButton, pricesButton)
            .build();
  }

  private SelectionButton createSellAllButton() {
    ItemStack icon = new ItemStack(Material.EMERALD_BLOCK);
    ItemMeta meta = icon.getItemMeta();
    meta.setDisplayName("§2Продать весь урожай");
    icon.setItemMeta(meta);

    return SelectionButton.builder()
            .headerText("§2Продать всё")
            .subText("§7Продать весь урожай")
            .itemIcon(icon)
            .onPlayerLeftClick((player, button) -> sellAllCrops(player))
            .build();
  }

  private SelectionButton createPricesButton() {
    ItemStack icon = new ItemStack(Material.PAPER);
    ItemMeta meta = icon.getItemMeta();
    meta.setDisplayName("§eЦены на урожай");
    icon.setItemMeta(meta);

    return SelectionButton.builder()
            .headerText("§eЦены")
            .subText("§7Показать цены")
            .itemIcon(icon)
            .onPlayerLeftClick((player, button) -> showPrices(player))
            .build();
  }

  private void sellAllCrops(Player player) {
    GamePlayer gamePlayer = player.getBungeePlayer();

    double totalEarnings = 0.0;
    int itemsSold = 0;
    Map<String, Integer> soldCrops = new HashMap<>();

    Map<String, CropCustomItem> crops = new HashMap<>(gamePlayer.getInventoryData().getCropInventoryData().getItems());

    for (Map.Entry<String, CropCustomItem> entry : crops.entrySet()) {
      CropCustomItem crop = entry.getValue();
      SeedData seedData = GrowAGardenPlugin.getInstance().getSeedService().getSeedData(crop.getItemId());

      if (seedData == null) {
        continue;
      }

      for (CropCustomItem.CropInstance instance : crop.getInstances()) {
        double weight = instance.getWeight();
        double basePrice = weight * seedData.getBaseValue();
        double mutationMultiplier = instance.getTotalPriceMultiplier();
        double totalPrice = basePrice * mutationMultiplier;

        totalEarnings += totalPrice;
        itemsSold++;
      }

      String cropName = seedData.getName().replace("Семена ", "");
      soldCrops.put(cropName, crop.getInstances().size());

      InventoryService.removeCrop(gamePlayer, entry.getKey());
    }

    if (itemsSold == 0) {
      AlertService.sendError(player, "В вашем инвентаре нет урожая для продажи!");
      return;
    }

    gamePlayer.addBalance(CurrencyType.MONEY, totalEarnings);

    AlertService.sendSuccess(player, "Продано предметов: §f" + itemsSold);

    StringBuilder details = new StringBuilder();
    for (Map.Entry<String, Integer> entry : soldCrops.entrySet()) {
      details.append(entry.getKey()).append(" §fx").append(entry.getValue()).append(" ");
    }

    AlertService.sendInfo(player, details.toString().trim());
    AlertService.sendTransaction(player, "§7+§6" + TextUtil.parseNumber(totalEarnings, 1) + " монет", 3.0);
    HudService.updateHud(player);
    menuManager.close(player);
  }

  private void showPrices(Player player) {
    GamePlayer gamePlayer = player.getBungeePlayer();
    double balance = gamePlayer != null ? gamePlayer.getBalance(CurrencyType.MONEY) : 0.0;

    Selection.Builder priceMenu = Selection.builder()
            .title("§6Цены на урожай")
            .balance(TextUtil.parseNumber(balance, 2))
            .balanceSymbol("§6монет")
            .rows(4)
            .columns(3);

    Map<String, SeedData> seeds = GrowAGardenPlugin.getInstance().getSeedService().getSeedDataMap();
    for (SeedData seed : seeds.values()) {
      String cropName = seed.getName().replace("Семена ", "");
      double pricePerKg = seed.getBaseValue();
      String categoryColor = seed.getCategory().getColorCode();

      ItemStack icon = new ItemStack(Material.CLAY_BALL);
      ItemMeta meta = icon.getItemMeta();
      meta.setDisplayName(categoryColor + cropName);
      List<String> lore = new ArrayList<>();
      lore.add("§7Цена: §6" + TextUtil.parseNumber(pricePerKg, 0) + " монет/кг");
      lore.add("§7Категория: §f" + seed.getCategory().getDisplayName());
      lore.add("");
      lore.add("§8Мутации увеличивают цену!");
      meta.setLore(lore);
      icon.setItemMeta(meta);

      SelectionButton priceButton = SelectionButton.builder()
              .headerText(categoryColor + cropName)
              .subText("§6" + TextUtil.parseNumber(pricePerKg, 0) + " монет/кг")
              .itemIcon(icon)
              .build();

      priceMenu.buttons(priceButton);
    }

    SelectionButton backButton = SelectionButton.builder()
            .headerText("§8← Назад")
            .subText("§7Вернуться в меню")
            .itemIcon(new ItemStack(Material.BARRIER))
            .onPlayerLeftClick((p, b) -> open(p))
            .build();

    priceMenu.buttons(backButton);

    menuManager.open(priceMenu.build(), player);
  }
}