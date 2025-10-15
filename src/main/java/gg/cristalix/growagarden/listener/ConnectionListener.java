package gg.cristalix.growagarden.listener;

import gg.cristalix.growagarden.GrowAGardenPlugin;
import gg.cristalix.growagarden.model.player.GamePlayer;
import gg.cristalix.growagarden.service.alert.AlertService;
import gg.cristalix.growagarden.service.hud.HudService;
import gg.cristalix.growagarden.service.network.PlantGrowthTicker;
import gg.cristalix.growagarden.utils.GlobalCache;
import gg.cristalix.growagarden.utils.map.MapUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.cristalix.core.data.event.PlayerDataLoadEvent;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class ConnectionListener implements Listener {
  GrowAGardenPlugin plugin;

  @EventHandler
  public void onPlayerLoad(PlayerDataLoadEvent event) {
    Player player = event.getPlayer();

    GamePlayer gamePlayer = event.getDocumentAs(GamePlayer.class);
    gamePlayer.init(player);

    if (event.isFirstJoin()) {
      gamePlayer.firstInit();
    }

    gamePlayer.getInventoryData().load(plugin.getItemService());
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    player.setGameMode(GameMode.ADVENTURE);
    player.teleport(MapUtil.spawnLocation);
    player.setItemInHand(null);
    HudService.updateHud(player);
    AlertService.sendInfo(player, "§aДобро пожаловать в Grow a Garden!");

    PlantGrowthTicker.start(plugin);
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    HudService.removeHud(player);

    UUID uniqueId = player.getUniqueId();
    if (PlantGrowthTicker.getInstance() != null) {
      PlantGrowthTicker.getInstance().clearCache(uniqueId);
    }

    GlobalCache.clear(uniqueId);
  }
}