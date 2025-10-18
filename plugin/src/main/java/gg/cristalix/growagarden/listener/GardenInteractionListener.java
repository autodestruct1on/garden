package gg.cristalix.growagarden.listener;

import gg.cristalix.growagarden.GameState;
import gg.cristalix.growagarden.GrowAGardenPlugin;
import gg.cristalix.growagarden.common.mod.inventory.ItemEnum;
import gg.cristalix.growagarden.common.util.Pair;
import gg.cristalix.growagarden.model.garden.CellData;
import gg.cristalix.growagarden.model.garden.vegetation.SeedData;
import gg.cristalix.growagarden.model.garden.vegetation.SeedInstance;
import gg.cristalix.growagarden.model.item.CropCustomItem;
import gg.cristalix.growagarden.model.item.CustomItem;
import gg.cristalix.growagarden.model.item.ItemCustomItem;
import gg.cristalix.growagarden.model.item.SeedCustomItem;
import gg.cristalix.growagarden.model.player.GamePlayer;
import gg.cristalix.growagarden.model.player.inventory.InventoryData;
import gg.cristalix.growagarden.model.world.WorldState;
import gg.cristalix.growagarden.service.alert.AlertService;
import gg.cristalix.growagarden.service.garden.GardenService;
import gg.cristalix.growagarden.service.item.seed.SeedService;
import gg.cristalix.growagarden.utils.GlobalCache;
import gg.cristalix.growagarden.utils.map.MapUtil;
import gg.cristalix.wada.math.V3;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import ru.cristalix.core.build.models.Box;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GardenInteractionListener implements Listener {

  Box cellZone;
  GrowAGardenPlugin plugin;
  SeedService seedService;
  GameState gameState;

  public GardenInteractionListener() {
    this.cellZone = (Box) MapUtil.mapCubouid.get("cell");
    this.plugin = GrowAGardenPlugin.getInstance();
    this.seedService = plugin.getSeedService();
    this.gameState = plugin.getGameState();
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
  public void onInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    UUID playerId = player.getUniqueId();
    if (GlobalCache.checkAndUpdate(playerId, "INTERACT")) return;

    Action action = event.getAction();
    if (action != Action.RIGHT_CLICK_BLOCK) return;

    Block clickedBlock = event.getClickedBlock();
    if (clickedBlock == null) return;

    V3 targetPoint = calculateTargetPoint(player, clickedBlock);
    if (!cellZone.isInZone(new ru.cristalix.core.math.V3(
      targetPoint.getX(),
      targetPoint.getY(),
      targetPoint.getZ()))) return;

    GamePlayer gamePlayer = player.getBungeePlayer();

    InventoryData inventoryData = gamePlayer.getInventoryData();
    CustomItem equipCustomItem = inventoryData.getEquipCustomItem();

    event.setCancelled(true);

    if (equipCustomItem == null) {
      handleHarvest(player, gamePlayer, targetPoint);
      return;
    }

    ItemEnum itemEnum = equipCustomItem.getItemEnum();
    if (itemEnum == ItemEnum.SEED) {
      if (equipCustomItem instanceof SeedCustomItem seedCustomItem) {
        handlePlanting(player, gamePlayer, seedCustomItem, targetPoint);
      }
    } else if (itemEnum == ItemEnum.ITEM) {
      String itemId = equipCustomItem.getItemId();
      if (equipCustomItem instanceof ItemCustomItem itemCustomItem) {
        if (itemId.equals("watering_can")) {
          handleWatering(player, gamePlayer, itemCustomItem, targetPoint);
        } else if (itemId.equals("shovel")) {
          handleDigging(player, gamePlayer, itemCustomItem, targetPoint);
        }
      }
    }
  }

  private V3 calculateTargetPoint(Player player, Block clickedBlock) {
    Location eyeLocation = player.getEyeLocation();
    Vector direction = eyeLocation.getDirection();
    Location targetLocation = eyeLocation.clone().add(direction.normalize());
    targetLocation.setY(clickedBlock.getY() + 1);
    return new V3(targetLocation.getX(), targetLocation.getY(), targetLocation.getZ());
  }

  private void handlePlanting(Player player, GamePlayer gamePlayer, SeedCustomItem seed, V3 targetPoint) {
    if (!GardenService.canPlantAt(gamePlayer, targetPoint)) {
      AlertService.sendError(player, "Слишком близко к другому растению!");
      return;
    }

    String seedId = seed.getItemId();
    SeedData seedData = seedService.getSeedDataByName(seedId);

    if (seedData == null) {
      AlertService.sendError(player, "Неизвестное семя!");
      return;
    }

    boolean isPlantSeed = GardenService.plantSeed(gamePlayer, targetPoint, seedId);
    if (!isPlantSeed) {
      AlertService.sendError(player, "Не удалось посадить семя!");
      return;
    }

    gamePlayer.getInventoryData().decreaseItemAmount(ItemEnum.SEED, seedId, 1);
    AlertService.sendSuccess(player, "Посажено семя: §f" + seedData.getName());
  }

  private void handleHarvest(Player player, GamePlayer gamePlayer, V3 targetPoint) {
    if (!GardenService.hasPlantAt(gamePlayer, targetPoint)) {
      AlertService.sendError(player, "Здесь нечего собирать!");
      return;
    }

    CellData cell = GardenService.getCellAt(gamePlayer, targetPoint);
    if (cell == null) return;

    SeedInstance seedInstance = cell.getSeedInstance();

    WorldState worldState = gameState.getWorldState();
    SeedData seedData = seedService.getSeedDataByName(seedInstance.getSeedId());
    if (seedData == null) return;

    if (!seedInstance.isFullyGrown(seedData, worldState)) {
      double progress = GardenService.getGrowthProgress(cell, worldState);
      AlertService.sendWarning(player, "Растение ещё не созрело! Прогресс: §f" +
        String.format("%.1f", progress) + "%");
      return;
    }

    CropCustomItem crop = GardenService.harvestCrop(gamePlayer, targetPoint, worldState);

    if (crop == null) {
      AlertService.sendError(player, "Не удалось собрать урожай!");
      return;
    }

    if (!gamePlayer.getInventoryData().addItem(crop)) {
      AlertService.sendError(player, "Инвентарь переполнен! Освободите место.");
      return;
    }

    String cropName = seedData.getName().replace("Семена ", "");
    double totalWeight = crop.getInstances().stream()
      .mapToDouble(CropCustomItem.CropInstance::getWeight)
      .sum();

    AlertService.sendSuccess(player, "Собран урожай: §f" + cropName + " §7(§f" +
      String.format("%.2f", totalWeight) + " кг§7)");

    plugin.getGardenMod().getInventoryMod().openMenu(player, ItemEnum.ALL);
  }

  private void handleWatering(Player player, GamePlayer gamePlayer, ItemCustomItem item, V3 targetPoint) {
    if (!GardenService.hasPlantAt(gamePlayer, targetPoint)) {
      AlertService.sendError(player, "Здесь нет растения для полива!");
      return;
    }

    if (!GardenService.waterPlant(gamePlayer, targetPoint)) {
      AlertService.sendError(player, "Растение уже полито!");
      return;
    }

    AlertService.sendSuccess(player, "Растение полито! Рост ускорен на 10%.");

    gamePlayer.getInventoryData().decreaseItemAmount(ItemEnum.ITEM, item.getItemId(), 1);
  }

  private void handleDigging(Player player, GamePlayer gamePlayer, ItemCustomItem item, V3 targetPoint) {
    if (!GardenService.hasPlantAt(gamePlayer, targetPoint)) {
      AlertService.sendError(player, "Здесь нет растения для выкапывания!");
      return;
    }

    if (!GardenService.digPlant(gamePlayer, targetPoint)) {
      AlertService.sendError(player, "Не удалось выкопать растение!");
      return;
    }

    AlertService.sendSuccess(player, "Растение выкопано!");

    gamePlayer.getInventoryData().decreaseItemAmount(ItemEnum.ITEM, item.getItemId(), 1);
  }
}