package gg.cristalix.growagarden.utils.map;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.util.Vector;
import ru.cristalix.core.build.models.IZone;
import ru.cristalix.core.build.models.Point;
import ru.cristalix.core.map.BukkitWorldLoader;
import ru.cristalix.core.map.IMapService;
import ru.cristalix.core.map.MapVersion;
import ru.cristalix.core.math.V3;

import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@UtilityClass
public class MapUtil {
  public World lobbyWorld;

  public Map<String, List<Point>> mapPoints;
  public Map<String, IZone> mapCubouid;

  public Location spawnLocation;

  public void loadWorld(MapVersion mapVersion) {
    mapPoints = mapVersion.getData().getPoints();
    mapCubouid = mapVersion.getData().getZones();

    mapPoints.forEach((key, points) -> log.info("Loaded points: {} | {}", key, points));

    IMapService mapService = IMapService.get();
    lobbyWorld = mapService.loadMap(mapVersion, BukkitWorldLoader.INSTANCE).join().getWorld();
    setupWorld(lobbyWorld);

    spawnLocation = getMapLocation("spawn");
    lobbyWorld.setSpawnLocation(spawnLocation);
  }

  public Location getMapLocation(String name) {
    return getMapLocation(name, new Vector());
  }

  public Location getMapLocation(String name, Vector offset) {
    List<Point> points = mapPoints.get(name);
    if (points == null || points.isEmpty())
      return null;

    Point point = points.getFirst();
    V3 vector = point.getV3();

    String tag = point.getTag();
    float yaw = !tag.isEmpty() ? Float.parseFloat(tag) : 0;

    return new Location(lobbyWorld,
      vector.getX() + offset.getX() + 0.5,
      vector.getY() + offset.getY(),
      vector.getZ() + offset.getZ() + 0.5,
      yaw, 0);
  }

  private void setupWorld(World world) {
    world.setTime(13000);
    world.setAutoSave(false);
    world.setGameRuleValue("announceAdvancements", "false");
    world.setGameRuleValue("doDaylightCycle", "false");
    world.setGameRuleValue("doEntityDrops", "false");
    world.setGameRuleValue("doFireTick", "false");
    world.setGameRuleValue("doWeatherCycle", "false");
    world.setGameRuleValue("keepInventory", "true");
    world.setGameRuleValue("mobGriefing", "false");
    world.setGameRuleValue("showDeathMessages", "false");
    world.setGameRuleValue("randomTickSpeed", "0");
    world.setGameRuleValue("spectatorsGenerateChunks", "false");
    world.setDifficulty(Difficulty.PEACEFUL);
    world.setSpawnFlags(false, false);

    WorldServer worldServer = ((CraftWorld) world).getHandle();
    worldServer.captureBlockStates = true;
    worldServer.savingDisabled = true;
    worldServer.getPlayerChunkMap().setViewDistanceForAll(10);
  }
}
