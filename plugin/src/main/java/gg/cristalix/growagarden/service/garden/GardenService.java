package gg.cristalix.growagarden.service.garden;

import gg.cristalix.growagarden.GrowAGardenPlugin;
import gg.cristalix.growagarden.factory.ItemFactory;
import gg.cristalix.growagarden.model.garden.CellData;
import gg.cristalix.growagarden.model.garden.vegetation.MutationType;
import gg.cristalix.growagarden.model.garden.vegetation.SeedData;
import gg.cristalix.growagarden.model.garden.vegetation.SeedInstance;
import gg.cristalix.growagarden.model.item.CropCustomItem;
import gg.cristalix.growagarden.model.player.GamePlayer;
import gg.cristalix.growagarden.model.world.WorldState;
import lombok.experimental.UtilityClass;
import ru.cristalix.core.math.V3;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class GardenService {

  private static final double MIN_PLANT_DISTANCE = 0.15;

  public boolean canPlantAt(GamePlayer player, V3 position) {
    return !player.getGarden().hasPlantInRadius(position, MIN_PLANT_DISTANCE);
  }

  public boolean plantSeed(GamePlayer player, V3 position, String seedId) {
    if (!canPlantAt(player, position)) {
      return false;
    }

    SeedData seedData = GrowAGardenPlugin.getInstance().getSeedService().getSeedData(seedId);
    if (seedData == null) {
      return false;
    }

    long now = System.currentTimeMillis();
    SeedInstance instance = new SeedInstance();
    instance.setSeedId(seedData.getId());
    instance.setPlantedAtMillis(now);
    instance.setWatered(false);
    instance.setFinalWeightCalculated(false);

    CellData cellData = new CellData(position);
    cellData.setSeedInstance(instance);
    player.getGarden().addPlantedCell(position, cellData);

    return true;
  }

  @Nullable
  public CropCustomItem harvestCrop(GamePlayer player, V3 position, WorldState worldState) {
    CellData cell = player.getGarden().getCellAtPosition(position);
    if (cell == null || cell.getSeedInstance() == null) {
      return null;
    }

    SeedInstance instance = cell.getSeedInstance();
    SeedData seedData = GrowAGardenPlugin.getInstance().getSeedService().getSeedData(instance.getSeedId());

    if (seedData == null || !instance.isFullyGrown(seedData, worldState)) {
      return null;
    }

    MutationService.applyHarvestMutations(instance, worldState.getCurrentWeather());

    double weight = calculateWeightKg(seedData);
    Set<MutationType> mutations = new HashSet<>(instance.getMutations());

    CropCustomItem crop = ItemFactory.createCrop(seedData.getId(), weight, mutations);

    if (seedData.isMultiHarvest()) {
      instance.setPlantedAtMillis(System.currentTimeMillis());
      instance.setWatered(false);
      instance.setFinalWeightCalculated(false);
      instance.getMutations().clear();
    } else {
      player.getGarden().removeCellAtPosition(position);
    }

    return crop;
  }

  public boolean waterPlant(GamePlayer player, V3 position) {
    CellData cell = player.getGarden().getCellAtPosition(position);
    if (cell == null || cell.getSeedInstance() == null) {
      return false;
    }

    SeedInstance instance = cell.getSeedInstance();
    if (instance.isWatered()) {
      return false;
    }

    instance.water();
    return true;
  }

  public boolean digPlant(GamePlayer player, V3 position) {
    CellData cell = player.getGarden().getCellAtPosition(position);
    if (cell == null || cell.getSeedInstance() == null) {
      return false;
    }

    player.getGarden().removeCellAtPosition(position);
    return true;
  }

  @Nullable
  public CellData getCellAt(GamePlayer player, V3 position) {
    return player.getGarden().getCellAtPosition(position);
  }

  public boolean hasPlantAt(GamePlayer player, V3 position) {
    return player.getGarden().hasPlantAtPosition(position);
  }

  public double getGrowthProgress(CellData cell, WorldState worldState) {
    if (cell == null || cell.getSeedInstance() == null) {
      return 0.0;
    }

    SeedInstance instance = cell.getSeedInstance();
    SeedData seedData = GrowAGardenPlugin.getInstance().getSeedService().getSeedData(instance.getSeedId());

    if (seedData == null) {
      return 0.0;
    }

    return instance.getGrowthProgress(seedData, worldState);
  }

  private double calculateWeightKg(SeedData seed) {
    double min = Math.max(0.0, seed.getBaseWeightKgMin());
    double max = Math.max(min, seed.getBaseWeightKgMax());
    return ThreadLocalRandom.current().nextDouble(min, max);
  }
}