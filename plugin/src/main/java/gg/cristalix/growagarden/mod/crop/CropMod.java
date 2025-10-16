package gg.cristalix.growagarden.mod.crop;

import gg.cristalix.growagarden.GameState;
import gg.cristalix.growagarden.mod.GardenMod;
import gg.cristalix.growagarden.mod.crop.data.CropModData;
import gg.cristalix.growagarden.mod.event.PlayerModLoadedEvent;
import gg.cristalix.growagarden.model.garden.CellData;
import gg.cristalix.growagarden.model.garden.vegetation.SeedData;
import gg.cristalix.growagarden.model.garden.vegetation.SeedInstance;
import gg.cristalix.growagarden.model.player.GamePlayer;
import gg.cristalix.growagarden.model.world.WorldState;
import gg.cristalix.growagarden.service.seed.SeedService;
import gg.cristalix.wada.transfer.ModTransfer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.cristalix.core.math.V3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CropMod implements Listener {
  GardenMod gardenMod;
  GameState gameState;

  private static final String PACKET_LOAD = "crop:load";
  private static final String PACKET_REMOVE = "crop:remove";
  private static final String PACKET_UPDATE = "crop:update";

  public CropMod(GardenMod gardenMod) {
    this.gardenMod = gardenMod;
    this.gameState = gardenMod.getPlugin().getGameState();
  }

  @EventHandler
  private void onLoadMod(PlayerModLoadedEvent event) {
    Player player = event.getPlayer();
    GamePlayer gamePlayer = player.getBungeePlayer();
    if (gamePlayer == null) return;

    WorldState worldState = gameState.getWorldState();
    Map<String, CellData> cells = gamePlayer.getGarden().getAllPlantedCells();

    gardenMod.getPlugin().getLogger().info("Loading crops for player " + player.getName() + ", cells count: " + cells.size());

    List<CropModData> cropModDataList = new ArrayList<>();

    for (CellData cell : cells.values()) {
      CropModData cropModData = createCropModData(cell, worldState);
      if (cropModData != null) {
        cropModDataList.add(cropModData);
        gardenMod.getPlugin().getLogger().info("Created crop at " + cropModData.getPosition() + " model: " + cropModData.getModel());
      }
    }

    if (!cropModDataList.isEmpty()) {
      gardenMod.getPlugin().getLogger().info("Sending " + cropModDataList.size() + " crops to client");
      loadCrops(player, cropModDataList);
    } else {
      gardenMod.getPlugin().getLogger().info("No crops to load for player - garden is empty or no plants");
    }
  }

  private CropModData createCropModData(CellData cell, WorldState worldState) {
    SeedInstance instance = cell.getSeedInstance();
    if (instance == null) return null;

    SeedService seedService = gardenMod.getPlugin().getSeedService();
    SeedData seedData = seedService.getSeedDataByUUID(instance.getSeedUuid());
    if (seedData == null) return null;

    CropModData cropModData = new CropModData();
    cropModData.setUuid(UUID.randomUUID());

    V3 position = cell.getPoint();
    cropModData.setPosition(new gg.cristalix.wada.math.V3(position.getX(), position.getY(), position.getZ()));

    cropModData.setCurrentStage(instance.calculateCurrentStage(seedData, worldState));
    cropModData.setGrowthProgress(instance.getGrowthProgress(seedData, worldState));
    cropModData.setWatered(instance.isWatered());

    List<String> lore = new ArrayList<>();
    lore.add(seedData.getDisplayName());
    lore.add("§7Стадия: §f" + cropModData.getCurrentStage() + "/" + seedData.getStages());
    lore.add("§7Прогресс: §f" + String.format("%.1f", cropModData.getGrowthProgress() * 100) + "%");
    if (cropModData.isWatered()) {
      lore.add("§a✓ Полито");
    }
    cropModData.setLore(lore);

    cropModData.setModel("banana_stage_1");

    cropModData.setModelSize(new gg.cristalix.wada.math.V3(1, 1, 1));

    return cropModData;
  }

  public void loadCrops(Player player, List<CropModData> cropModDataList) {
    ModTransfer transfer = new ModTransfer();
    writeCrop(transfer, cropModDataList);
    transfer.send(PACKET_LOAD, player);
  }

  public void removeCrops(Player player, List<UUID> uuids) {
    new ModTransfer()
            .writeCollection(uuids, ModTransfer::writeUUID)
            .send(PACKET_REMOVE, player);
  }

  public void writeCrop(ModTransfer modTransfer, List<CropModData> cellDataList) {
    modTransfer.writeCollection(cellDataList, (transfer, value) -> value.serialize(transfer));
  }

  public void sendCrop(Player player, V3 location, String modelId) {
    new ModTransfer()
            .writeDouble(location.getX())
            .writeDouble(location.getY())
            .writeDouble(location.getZ())
            .writeString(modelId)
            .send(PACKET_LOAD, player);
  }
}