package gg.cristalix.growagarden.mod.crop;

import gg.cristalix.growagarden.GameState;
import gg.cristalix.growagarden.GrowAGardenPlugin;
import gg.cristalix.growagarden.mod.GardenMod;
import gg.cristalix.growagarden.mod.crop.data.CropModData;
import gg.cristalix.growagarden.mod.event.PlayerModLoadedEvent;
import gg.cristalix.growagarden.model.garden.CellData;
import gg.cristalix.growagarden.model.garden.vegetation.SeedData;
import gg.cristalix.growagarden.model.garden.vegetation.SeedInstance;
import gg.cristalix.growagarden.model.player.GamePlayer;
import gg.cristalix.growagarden.model.world.WorldState;
import gg.cristalix.growagarden.service.item.seed.SeedService;
import gg.cristalix.wada.transfer.ModTransfer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class CropMod implements Listener {
  GrowAGardenPlugin plugin;

  GardenMod gardenMod;
  GameState gameState;
  WorldState worldState;

  SeedService seedService;

  private static final String PACKET_LOAD = "gag:crop:load";
  private static final String PACKET_REMOVE = "gag:crop:remove";
  private static final String PACKET_UPDATE = "gag:crop:update";

  Set<UUID> loadMods = new HashSet<>();

  public CropMod(GardenMod gardenMod) {
    this.plugin = gardenMod.getPlugin();
    this.gardenMod = gardenMod;
    this.gameState = plugin.getGameState();
    this.worldState = gameState.getWorldState();
    this.seedService = plugin.getSeedService();
  }

  public void loadCrops(Player player, CellData... cellData) {
    this.loadCrops(player, Arrays.asList(cellData));
  }

  public void loadCrops(Player player, Collection<CellData> cellDataList) {
    List<CropModData> cropModDataList = getCropModData(cellDataList);

    if (!cropModDataList.isEmpty()) {
      gardenMod.getPlugin().getLogger().info("Sending load " + cropModDataList.size() + " crops to client");
      writeLoadCrops(player, cropModDataList);
    } else {
      gardenMod.getPlugin().getLogger().info("No crops to load for player - garden is empty or no plants");
    }
  }

  public void updateCrops(Player player, CellData... cellData) {
    this.updateCrops(player, Arrays.asList(cellData));
  }

  public void updateCrops(Player player, Collection<CellData> cellDataList) {
    List<CropModData> cropModDataList = getCropModData(cellDataList);

    if (!cropModDataList.isEmpty()) {
      gardenMod.getPlugin().getLogger().info("Sending update " + cropModDataList.size() + " crops to client");
      writeUpdateCrops(player, cropModDataList);
    } else {
      gardenMod.getPlugin().getLogger().info("No crops to update for player - garden is empty or no plants");
    }
  }

  public void removeCrops(Player player, UUID... uuids) {
    this.removeCrops(player, Arrays.asList(uuids));
  }

  private void writeLoadCrops(Player player, List<CropModData> cropModDataList) {
    ModTransfer transfer = new ModTransfer();
    writeCrop(transfer, cropModDataList);
    transfer.send(PACKET_LOAD, player);
  }

  private void writeUpdateCrops(Player player, List<CropModData> cropModDataList) {
    ModTransfer transfer = new ModTransfer();
    writeCrop(transfer, cropModDataList);
    transfer.send(PACKET_UPDATE, player);
  }

  private void removeCrops(Player player, List<UUID> uuids) {
    new ModTransfer()
      .writeCollection(uuids, ModTransfer::writeUUID)
      .send(PACKET_REMOVE, player);
  }

  private List<CropModData> getCropModData(Collection<CellData> cellData) {
    List<CropModData> cropModDataList = new ArrayList<>();

    for (CellData cell : cellData) {
      CropModData cropModData = cell.getSeedInstance().getCropModData();
      if (cropModData == null) cropModData = createCropModData(cell, worldState);
      if (cropModData == null) continue;

      cropModDataList.add(cropModData);
      plugin.getLogger().info("Created crop at " + cropModData.getPosition() + " model: " + cropModData.getModel());
    }

    return cropModDataList;
  }

  private CropModData createCropModData(CellData cell, WorldState worldState) {
    SeedInstance instance = cell.getSeedInstance();
    if (instance == null) return null;

    SeedData seedData = seedService.getSeedDataByName(instance.getSeedId());
    if (seedData == null) return null;

    CropModData cropModData = new CropModData(cell, seedData, worldState);
    instance.setCropModData(cropModData);

    return cropModData;
  }

  private void writeCrop(ModTransfer modTransfer, List<CropModData> cellDataList) {
    modTransfer.writeCollection(cellDataList, (transfer, value) -> value.serialize(transfer));
  }

  @EventHandler
  private void onLoadMod(PlayerModLoadedEvent event) {
    Player player = event.getPlayer();
    GamePlayer gamePlayer = player.getBungeePlayer();
    Map<String, CellData> cells = gamePlayer.getGarden().getAllPlantedCells();
    loadCrops(player, cells.values());
  }
}