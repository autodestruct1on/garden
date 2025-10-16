/*
package gg.cristalix.growagarden.service.network;

import gg.cristalix.growagarden.GameState;
import gg.cristalix.growagarden.GrowAGardenPlugin;
import gg.cristalix.growagarden.common.mod.inventory.ItemEnum;
import gg.cristalix.growagarden.model.garden.CellData;
import gg.cristalix.growagarden.model.garden.vegetation.MutationType;
import gg.cristalix.growagarden.model.garden.vegetation.SeedData;
import gg.cristalix.growagarden.model.garden.vegetation.SeedInstance;
import gg.cristalix.growagarden.model.item.CropCustomItem;
import gg.cristalix.growagarden.model.item.ItemCustomItem;
import gg.cristalix.growagarden.model.item.SeedCustomItem;
import gg.cristalix.growagarden.model.player.GamePlayer;
import gg.cristalix.growagarden.model.world.WorldState;
import gg.cristalix.growagarden.model.world.weather.WeatherData;
import gg.cristalix.growagarden.network.ModChannels;
import gg.cristalix.growagarden.network.data.PlantSyncData;
import gg.cristalix.growagarden.service.seed.SeedService;
import gg.cristalix.wada.transfer.ModTransfer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import ru.cristalix.core.math.V3;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModSyncService {

  GrowAGardenPlugin plugin;
  SeedService seedService;
  GameState gameState;

  public static void syncInventory(Player player) {
    GamePlayer gamePlayer = player.getBungeePlayer();
    if (gamePlayer == null) return;

    ModTransfer transfer = new ModTransfer();

    transfer.writeInt(ItemEnum.VAL.length);
    for (ItemEnum itemEnum : ItemEnum.VAL) {
      transfer.writeString(itemEnum.name());
    }

    Map<String, CropCustomItem> crops = gamePlayer.getInventoryData().getCropInventoryData().getItems();
    transfer.writeInt(crops.size());
    for (Map.Entry<String, CropCustomItem> entry : crops.entrySet()) {
      transfer.writeString(entry.getValue().getUuid().toString());
      transfer.writeString(entry.getKey());
      transfer.writeInt(entry.getValue().getInstances().size());
      for (CropCustomItem.CropInstance instance : entry.getValue().getInstances()) {
        transfer.writeDouble(instance.getWeight());

        Set<MutationType> mutations = instance.getMutations();
        transfer.writeInt(mutations.size());
        for (MutationType mutation : mutations) {
          transfer.writeString(mutation.name());
        }
      }
    }

    Map<String, SeedCustomItem> seeds = gamePlayer.getInventoryData().getSeedInventoryData().getItems();
    transfer.writeInt(seeds.size());
    for (Map.Entry<String, SeedCustomItem> entry : seeds.entrySet()) {
      transfer.writeString(entry.getValue().getUuid().toString());
      transfer.writeString(entry.getKey());
      transfer.writeInt(entry.getValue().getAmount());
    }

    Map<String, ItemCustomItem> items = gamePlayer.getInventoryData().getItemInventoryData().getItems();
    transfer.writeInt(items.size());
    for (Map.Entry<String, ItemCustomItem> entry : items.entrySet()) {
      transfer.writeString(entry.getValue().getUuid().toString());
      transfer.writeString(entry.getKey());
      transfer.writeInt(entry.getValue().getAmount());
      transfer.writeInt(entry.getValue().getAmountUsage());
    }

    transfer.send(ModChannels.INVENTORY_SYNC, player);
  }

  public void syncGarden(Player player) {
    GamePlayer gamePlayer = player.getBungeePlayer();
    if (gamePlayer == null) return;

    WorldState worldState = gameState.getWorldState();
    Map<String, CellData> cells = gamePlayer.getGarden().getAllPlantedCells();

    ModTransfer transfer = new ModTransfer();
    transfer.writeInt(cells.size());

    for (Map.Entry<String, CellData> entry : cells.entrySet()) {
      CellData cell = entry.getValue();
      V3 position = cell.getPoint();

      transfer.writeDouble(position.getX());
      transfer.writeDouble(position.getY());
      transfer.writeDouble(position.getZ());

      writePlantData(transfer, cell, worldState);
    }

    transfer.send(ModChannels.GARDEN_SYNC, player);
  }

  public void syncPlantUpdate(Player player, V3 position, CellData cell) {
    WorldState worldState = gameState.getWorldState();
    PlantSyncData syncData = createPlantSyncData(cell, worldState);

    if (syncData == null) return;

    ModTransfer transfer = new ModTransfer();

    transfer.writeDouble(position.getX());
    transfer.writeDouble(position.getY());
    transfer.writeDouble(position.getZ());

    transfer.writeString(syncData.getSeedId());
    transfer.writeInt(syncData.getCurrentStage());
    transfer.writeDouble(syncData.getGrowthProgress());
    transfer.writeBoolean(syncData.isWatered());
    transfer.writeLong(syncData.getRemainingTimeMillis());

    transfer.send(ModChannels.PLANT_UPDATE, player);

    plugin.getGardenMod().getCropMod().sendCrop(player, position, "banana_stage_1");
  }

  public void syncPlantRemove(Player player, V3 position) {
    ModTransfer transfer = new ModTransfer();

    transfer.writeDouble(position.getX());
    transfer.writeDouble(position.getY());
    transfer.writeDouble(position.getZ());

    transfer.send(ModChannels.PLANT_REMOVE, player);
  }

  private void writePlantData(ModTransfer transfer, CellData cell, WorldState worldState) {
    SeedInstance instance = cell.getSeedInstance();
    if (instance == null) {
      transfer.writeString("");
      transfer.writeInt(0);
      transfer.writeDouble(0.0);
      transfer.writeBoolean(false);
      transfer.writeLong(0L);
      return;
    }

    SeedData seedData = seedService.getSeedDataByUUID(instance.getSeedUuid());
    if (seedData == null) {
      transfer.writeString("");
      transfer.writeInt(0);
      transfer.writeDouble(0.0);
      transfer.writeBoolean(false);
      transfer.writeLong(0L);
      return;
    }

    transfer.writeString(seedData.getId());
    transfer.writeInt(instance.calculateCurrentStage(seedData, worldState));
    transfer.writeDouble(instance.getGrowthProgress(seedData, worldState));
    transfer.writeBoolean(instance.isWatered());
    transfer.writeLong(instance.getRemainingGrowTime(seedData, worldState));
  }

  @Nullable
  public PlantSyncData createPlantSyncData(CellData cell, WorldState worldState) {
    SeedInstance instance = cell.getSeedInstance();
    if (instance == null) return null;

    SeedData seedData = seedService.getSeedDataByUUID(instance.getSeedUuid());
    if (seedData == null) return null;

    return new PlantSyncData(
            seedData.getId(),
            instance.calculateCurrentStage(seedData, worldState),
            instance.getGrowthProgress(seedData, worldState),
            instance.isWatered(),
            instance.getRemainingGrowTime(seedData, worldState)
    );
  }

  public void syncWeather(Player player) {
    WorldState worldState = gameState.getWorldState();
    WeatherData currentWeather = worldState.getCurrentWeather();

    if (currentWeather == null) {
      return;
    }

    ModTransfer transfer = new ModTransfer();

    transfer.writeString(currentWeather.getType().name());
    transfer.writeDouble(currentWeather.getGrowthRateMultiplier());
    transfer.writeLong(currentWeather.getDurationMillis());
    transfer.writeLong(worldState.getWeatherStartedAt());

    transfer.send(ModChannels.WEATHER_SYNC, player);
  }
}*/
