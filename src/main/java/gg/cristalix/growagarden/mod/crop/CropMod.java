package gg.cristalix.growagarden.mod.crop;

import gg.cristalix.growagarden.GameState;
import gg.cristalix.growagarden.mod.GardenMod;
import gg.cristalix.growagarden.mod.crop.data.CropModData;
import gg.cristalix.growagarden.mod.event.PlayerModLoadedEvent;
import gg.cristalix.wada.transfer.ModTransfer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.cristalix.core.math.V3;

import java.util.List;
import java.util.UUID;

public class CropMod implements Listener {
  GardenMod gardenMod;
  GameState gameState;

  private static final String PACKET_LOAD = "gag:crop:load";
  private static final String PACKET_REMOVE = "gag:crop:remove";
  private static final String PACKET_UPDATE = "gag:crop:update";

  public CropMod(GardenMod gardenMod) {
    this.gardenMod = gardenMod;
    this.gameState = gardenMod.getPlugin().getGameState();
  }

  @EventHandler
  private void onLoadMod(PlayerModLoadedEvent event) {
    //    todo отправлять данные на клиент #writeCrop
//    Player player = event.getPlayer();
//    GamePlayer gamePlayer = player.getBungeePlayer();
//
//    ModTransfer transfer = new ModTransfer();
//    List<CropModData> cellDataList = ;
//    writeCrop(transfer, cellDataList);
//    transfer.send(PACKET_LOAD, player);
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


//  @Nullable
//  public PlantSyncData createPlantSyncData(CellData cell, WorldState worldState) {
//    SeedInstance instance = cell.getSeedInstance();
//    if (instance == null) return null;
//
//    SeedData seedData = GrowAGardenPlugin.getInstance().getSeedService().getSeedData(instance.getSeedId());
//    if (seedData == null) return null;
//
//    return new PlantSyncData(
//      instance.getSeedId(),
//      instance.calculateCurrentStage(seedData, worldState),
//      instance.getGrowthProgress(seedData, worldState),
//      instance.isWatered(),
//      instance.getRemainingGrowTime(seedData, worldState)
//    );
//  }

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
