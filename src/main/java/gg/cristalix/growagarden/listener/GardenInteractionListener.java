package gg.cristalix.growagarden.listener;

import gg.cristalix.growagarden.GrowAGardenPlugin;
import gg.cristalix.growagarden.common.mod.inventory.ItemEnum;
import gg.cristalix.growagarden.model.garden.CellData;
import gg.cristalix.growagarden.model.garden.vegetation.SeedData;
import gg.cristalix.growagarden.model.item.CropCustomItem;
import gg.cristalix.growagarden.model.item.CustomItem;
import gg.cristalix.growagarden.model.item.ItemCustomItem;
import gg.cristalix.growagarden.model.item.SeedCustomItem;
import gg.cristalix.growagarden.model.player.GamePlayer;
import gg.cristalix.growagarden.model.player.inventory.InventoryData;
import gg.cristalix.growagarden.service.alert.AlertService;
import gg.cristalix.growagarden.service.garden.GardenService;
import gg.cristalix.growagarden.service.inventory.InventoryService;
import gg.cristalix.growagarden.service.network.ModSyncService;
import gg.cristalix.growagarden.utils.GlobalCache;
import gg.cristalix.growagarden.utils.map.MapUtil;
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
import ru.cristalix.core.math.V3;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GardenInteractionListener implements Listener {

  Box cellZone;

  public GardenInteractionListener() {
    this.cellZone = (Box) MapUtil.mapCubouid.get("cell");
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
    if (!cellZone.isInZone(targetPoint)) return;

    GamePlayer gamePlayer = player.getBungeePlayer();

    InventoryData inventoryData = gamePlayer.getInventoryData();
    CustomItem equipCustomItem = inventoryData.getEquipCustomItem();

    if (equipCustomItem == null) {
      event.setCancelled(true);
      handleHarvest(player, gamePlayer, targetPoint);
      return;
    }

    event.setCancelled(true);

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

    if (!GardenService.plantSeed(gamePlayer, targetPoint, seed.getItemId())) {
      AlertService.sendError(player, "Не удалось посадить семя!");
      return;
    }

    SeedData seedData = GrowAGardenPlugin.getInstance().getSeedService().getSeedData(seed.getItemId());
    if (seedData != null) {
      AlertService.sendSuccess(player, "Посажено семя: §f" + seedData.getName());
    }

    InventoryService.decreaseSeedAmount(gamePlayer, seed.getItemId(), 1);

    CellData cell = GardenService.getCellAt(gamePlayer, targetPoint);
    if (cell != null) {
      ModSyncService.syncPlantUpdate(player, targetPoint, cell);
    }
    ModSyncService.syncInventory(player);
  }

  private void handleHarvest(Player player, GamePlayer gamePlayer, V3 targetPoint) {
    if (!GardenService.hasPlantAt(gamePlayer, targetPoint)) {
      AlertService.sendError(player, "Здесь нечего собирать!");
      return;
    }

    CellData cell = GardenService.getCellAt(gamePlayer, targetPoint);
    if (cell == null || cell.getSeedInstance() == null) {
      return;
    }

    SeedData seedData = GrowAGardenPlugin.getInstance().getSeedService()
      .getSeedData(cell.getSeedInstance().getSeedId());

    if (seedData == null) {
      return;
    }

    if (!cell.getSeedInstance().isFullyGrown(seedData,
      GrowAGardenPlugin.getInstance().getGameState().getWorldState())) {
      double progress = GardenService.getGrowthProgress(cell,
        GrowAGardenPlugin.getInstance().getGameState().getWorldState());
      AlertService.sendWarning(player, "Растение ещё не созрело! Прогресс: §f" +
        String.format("%.1f", progress) + "%");
      return;
    }

    CropCustomItem crop = GardenService.harvestCrop(gamePlayer, targetPoint,
            GrowAGardenPlugin.getInstance().getGameState().getWorldState());

    if (crop == null) {
      AlertService.sendError(player, "Не удалось собрать урожай!");
      return;
    }

    if (!InventoryService.addCrop(gamePlayer, crop)) {
      AlertService.sendError(player, "Инвентарь переполнен! Освободите место.");
      return;
    }

    String cropName = seedData.getName().replace("Семена ", "");
    double totalWeight = crop.getInstances().stream()
      .mapToDouble(CropCustomItem.CropInstance::getWeight)
      .sum();

    AlertService.sendSuccess(player, "Собран урожай: §f" + cropName + " §7(§f" +
      String.format("%.2f", totalWeight) + " кг§7)");

    if (seedData.isMultiHarvest()) {
      CellData updatedCell = GardenService.getCellAt(gamePlayer, targetPoint);
      if (updatedCell != null) {
        ModSyncService.syncPlantUpdate(player, targetPoint, updatedCell);
      }
    } else {
      ModSyncService.syncPlantRemove(player, targetPoint);
    }
    ModSyncService.syncInventory(player);
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

    InventoryService.decreaseItemAmount(gamePlayer, item.getItemId(), 1);
    ModSyncService.syncInventory(player);

    CellData cell = GardenService.getCellAt(gamePlayer, targetPoint);
    if (cell != null) {
      ModSyncService.syncPlantUpdate(player, targetPoint, cell);
    }
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

    InventoryService.decreaseItemAmount(gamePlayer, item.getItemId(), 1);

    ModSyncService.syncPlantRemove(player, targetPoint);
    ModSyncService.syncInventory(player);
  }
}