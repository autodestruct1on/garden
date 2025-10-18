package gg.cristalix.growagarden.model.world.npc.impl;

import gg.cristalix.growagarden.GrowAGardenPlugin;
import gg.cristalix.growagarden.common.mod.inventory.ItemEnum;
import gg.cristalix.growagarden.common.util.TextUtil;
import gg.cristalix.growagarden.model.garden.vegetation.SeedData;
import gg.cristalix.growagarden.model.item.SeedCustomItem;
import gg.cristalix.growagarden.model.player.GamePlayer;
import gg.cristalix.growagarden.model.player.economy.CurrencyType;
import gg.cristalix.growagarden.model.world.npc.IWorldNPC;
import gg.cristalix.growagarden.model.world.npc.NPCData;
import gg.cristalix.growagarden.service.alert.AlertService;
import gg.cristalix.growagarden.service.hud.HudService;
import gg.cristalix.growagarden.service.item.seed.SeedService;
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
public class SeedSellerNPC extends IWorldNPC {

  GrowAGardenPlugin plugin;
  SeedService seedService;

  public SeedSellerNPC(Location location) {
    super(NPCData.of("§aПродавец семян", "", location));
    this.plugin = GrowAGardenPlugin.getInstance();
    this.seedService = plugin.getSeedService();
  }

  @Override
  protected void open(Player player) {
    Selection mainMenu = createMainMenu(player);
    menuManager.open(mainMenu, player);
  }

  private Selection createMainMenu(Player player) {
    GamePlayer gamePlayer = player.getBungeePlayer();
    double balance = gamePlayer != null ? gamePlayer.getBalance(CurrencyType.MONEY) : 0.0;

    Selection.Builder menuBuilder = Selection.builder()
      .title("§aПродавец семян")
      .balance(TextUtil.parseNumber(balance, 2))
      .balanceSymbol("§6монет")
      .rows(4)
      .columns(3);

    for (SeedData seedData : seedService.getSeedDataMap().values()) {
      double price = seedService.calculateSeedPrice(seedData);
      String categoryColor = seedData.getCategory().getColorCode();

      ItemStack icon = createSeedItemStack(seedData);
      ItemMeta meta = icon.getItemMeta();
      if (meta != null) {
        meta.setDisplayName(categoryColor + seedData.getName());
        List<String> lore = new ArrayList<>();
        lore.add("§7Цена: §6" + TextUtil.parseNumber(price, 0) + " монет");
        lore.add("§7Время роста: §f" + formatGrowTime(seedData.getGrowTimeMillis()));
        lore.add("§7Стадий: §f" + seedData.getStages());
        lore.add("§7Категория: §f" + seedData.getCategory().getDisplayName());
        if (seedData.isMultiHarvest()) {
          lore.add("§a✓ Многократный сбор");
        }
        lore.add("");
        lore.add("§eНажмите, чтобы купить");
        meta.setLore(lore);
        icon.setItemMeta(meta);
      }

      SelectionButton seedButton = SelectionButton.builder()
        .headerText(categoryColor + seedData.getName())
        .subText("§6" + TextUtil.parseNumber(price, 0) + " монет")
        .itemIcon(icon)
        .onPlayerLeftClick((p, b) -> buySeed(p, seedData))
        .build();

      menuBuilder.buttons(seedButton);
    }

    return menuBuilder.build();
  }

  private void buySeed(Player player, SeedData seedData) {
    GamePlayer gamePlayer = player.getBungeePlayer();

    double price = seedService.calculateSeedPrice(seedData);

    if (gamePlayer.getBalance(CurrencyType.MONEY) < price) {
      AlertService.sendError(player, "§cНедостаточно средств! Нужно: §6" +
        TextUtil.parseNumber(price, 0) + " монет");
      return;
    }

    SeedCustomItem seed = (SeedCustomItem) plugin.getItemService().createItem(seedData.getId());

    if (seed == null) {
      AlertService.sendError(player, "§cОшибка: не удалось создать семя");
      return;
    }

    if (!gamePlayer.getInventoryData().addItem(seed)) {
      AlertService.sendError(player, "Инвентарь переполнен! Освободите место.");
      return;
    }

    gamePlayer.removeBalance(CurrencyType.MONEY, price);

    AlertService.sendSuccess(player, "Куплено: " + seedData.getName());
    AlertService.sendTransaction(player, "§7-§6" + TextUtil.parseNumber(price, 0) + " монет", 2.0);

    HudService.updateHud(player);
    plugin.getGardenMod().getInventoryMod().openMenu(player, ItemEnum.ALL);
    menuManager.close(player);
  }

  private ItemStack createSeedItemStack(SeedData seedData) {
    ItemStack item = new ItemStack(Material.CLAY_BALL);
    ItemMeta meta = item.getItemMeta();
    if (meta != null) {
      meta.setDisplayName(seedData.getDisplayName());
      item.setItemMeta(meta);
    }
    return item;
  }

  private String formatGrowTime(long millis) {
    long seconds = millis / 1000;
    long minutes = seconds / 60;
    long hours = minutes / 60;

    if (hours > 0) {
      return hours + "ч " + (minutes % 60) + "м";
    } else if (minutes > 0) {
      return minutes + "м";
    } else {
      return seconds + "с";
    }
  }
}