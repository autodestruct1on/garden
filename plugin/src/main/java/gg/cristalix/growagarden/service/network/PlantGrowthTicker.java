package gg.cristalix.growagarden.service.network;

import gg.cristalix.growagarden.GrowAGardenPlugin;
import gg.cristalix.growagarden.model.garden.CellData;
import gg.cristalix.growagarden.model.player.GamePlayer;
import gg.cristalix.growagarden.model.world.WorldState;
import gg.cristalix.growagarden.network.data.PlantSyncData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.cristalix.core.math.V3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlantGrowthTicker extends BukkitRunnable {

  static final int GROWTH_TICK_INTERVAL = 20;

  @Getter
  static PlantGrowthTicker instance;

  final Map<UUID, Map<String, PlantStageData>> playerPlantStageCache = new HashMap<>();

  @Override
  public void run() {
    WorldState worldState = GrowAGardenPlugin.getInstance().getGameState().getWorldState();

    for (Player player : Bukkit.getOnlinePlayers()) {
      GamePlayer gamePlayer = player.getBungeePlayer();
      if (gamePlayer == null) continue;

      UUID playerId = player.getUniqueId();
      Map<String, PlantStageData> stageCache = playerPlantStageCache.computeIfAbsent(playerId, k -> new HashMap<>());

      Map<String, CellData> cells = gamePlayer.getGarden().getAllPlantedCells();

      for (Map.Entry<String, CellData> entry : cells.entrySet()) {
        String positionKey = entry.getKey();
        CellData cell = entry.getValue();

        PlantSyncData syncData = ModSyncService.createPlantSyncData(cell, worldState);
        if (syncData == null) {
          stageCache.remove(positionKey);
          continue;
        }

        PlantStageData cachedData = stageCache.get(positionKey);
        int currentStage = syncData.getCurrentStage();
        boolean currentWatered = syncData.isWatered();

        if (cachedData == null ||
          cachedData.stage != currentStage ||
          cachedData.watered != currentWatered) {

          stageCache.put(positionKey, new PlantStageData(currentStage, currentWatered));
          V3 position = cell.getPoint();
          ModSyncService.syncPlantUpdate(player, position, cell);
        }
      }

      stageCache.keySet().removeIf(key -> !cells.containsKey(key));
    }
  }

  public void clearCache(UUID playerId) {
    playerPlantStageCache.remove(playerId);
  }

  public static void start(GrowAGardenPlugin plugin) {
    if (instance != null && !instance.isCancelled()) {
      return;
    }
    instance = new PlantGrowthTicker();
    instance.runTaskTimer(plugin, GROWTH_TICK_INTERVAL, GROWTH_TICK_INTERVAL);
  }

  public static void stop() {
    if (instance != null) {
      instance.cancel();
      instance = null;
    }
  }

  @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
  private static class PlantStageData {
    int stage;
    boolean watered;

    PlantStageData(int stage, boolean watered) {
      this.stage = stage;
      this.watered = watered;
    }
  }
}