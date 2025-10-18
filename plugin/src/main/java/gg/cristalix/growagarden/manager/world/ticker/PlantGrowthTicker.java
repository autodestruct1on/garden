package gg.cristalix.growagarden.manager.world.ticker;

import gg.cristalix.growagarden.GameState;
import gg.cristalix.growagarden.GrowAGardenPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlantGrowthTicker extends BukkitRunnable {

  static final int GROWTH_TICK_INTERVAL = 20;

  @Getter
  static PlantGrowthTicker instance;

  final GrowAGardenPlugin plugin;
  final GameState gameState;
  final Map<UUID, Map<String, PlantStageData>> playerPlantStageCache = new HashMap<>();

  private PlantGrowthTicker(GrowAGardenPlugin plugin) {
    this.plugin = plugin;
    this.gameState = plugin.getGameState();
  }

  @Override
  public void run() {
  }

  public void clearCache(UUID playerId) {
    playerPlantStageCache.remove(playerId);
  }

  public static void start(GrowAGardenPlugin plugin) {
    if (instance != null && !instance.isCancelled()) {
      return;
    }
    instance = new PlantGrowthTicker(plugin);
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