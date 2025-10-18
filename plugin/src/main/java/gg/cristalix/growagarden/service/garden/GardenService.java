/*
package gg.cristalix.growagarden.service.garden;

import gg.cristalix.growagarden.GrowAGardenPlugin;
import gg.cristalix.growagarden.common.util.RandomUtil;
import gg.cristalix.growagarden.factory.ItemFactory;
import gg.cristalix.growagarden.model.garden.CellData;
import gg.cristalix.growagarden.model.garden.GardenData;
import gg.cristalix.growagarden.model.garden.vegetation.MutationType;
import gg.cristalix.growagarden.model.garden.vegetation.SeedData;
import gg.cristalix.growagarden.model.garden.vegetation.SeedInstance;
import gg.cristalix.growagarden.model.item.CropCustomItem;
import gg.cristalix.growagarden.model.player.GamePlayer;
import gg.cristalix.growagarden.model.world.WorldState;
import gg.cristalix.growagarden.service.item.seed.SeedService;
import gg.cristalix.wada.math.V3;
import lombok.experimental.UtilityClass;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.Set;

@UtilityClass
public class GardenService {

  private static final double MIN_PLANT_DISTANCE = 0.15;

  public boolean canPlantAt(GamePlayer player, V3 position) {
    return !player.getGarden().hasPlantInRadius(position, MIN_PLANT_DISTANCE);
  }

  public boolean plantSeed(GamePlayer player, V3 position, String seedId) {
    if (!canPlantAt(player, position)) return false;

    SeedService seedService = GrowAGardenPlugin.getInstance().getSeedService();
    SeedData seedData = seedService.getSeedDataByName(seedId);

    if (seedData == null) return false;

    SeedInstance instance = new SeedInstance(seedData);

    CellData cellData = new CellData(position, instance);
    player.getGarden().addPlantedCell(position, cellData);

    //CropMod.loadCrops(player, cellData);
    return true;
  }

  @Nullable
  public CropCustomItem harvestCrop(GamePlayer player, V3 position, WorldState worldState) {
    CellData cell = player.getGarden().getCellAtPosition(position);
    if (cell == null) return null;

    SeedInstance instance = cell.getSeedInstance();
    SeedService seedService = GrowAGardenPlugin.getInstance().getSeedService();
    SeedData seedData = seedService.getSeedDataByName(instance.getSeedId());

    if (seedData == null || !instance.isFullyGrown(seedData, worldState)) return null;

    MutationService.applyHarvestMutations(instance, worldState.getCurrentWeather());

    double weight = calculateWeightKg(seedData);
    Set<MutationType> mutations = instance.getMutations();

    CropCustomItem crop = ItemFactory.createCrop(seedData.getId(), weight, mutations);

    if (seedData.isMultiHarvest()) {
      instance.setPlantedAtMillis(Instant.now().toEpochMilli());
      instance.setWatered(false);
      instance.setFinalWeightCalculated(false);
      instance.clearMutation();
      //CropMod.update(player, instance);
    } else {
      player.getGarden().removeCellAtPosition(position);
      //CropMod.removeCrops(player, instance.getSeedId());
    }

    return crop;
  }

  public boolean waterPlant(GamePlayer player, V3 position) {
    CellData cell = player.getGarden().getCellAtPosition(position);
    if (cell == null) return false;

    SeedInstance instance = cell.getSeedInstance();
    if (instance.isWatered()) return false;

    instance.water();
    //CropMod.update(player, instance);
    return true;
  }

  public boolean digPlant(GamePlayer player, V3 position) {
    GardenData garden = player.getGarden();
    CellData cell = garden.getCellAtPosition(position);
    if (cell == null) return false;

    garden.removeCellAtPosition(position);
    //CropMod.removeCrops(player, Collections.singleton(instance.getSeedId()));
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
    if (cell == null || cell.getSeedInstance() == null) return 0.0;

    SeedInstance instance = cell.getSeedInstance();
    SeedService seedService = GrowAGardenPlugin.getInstance().getSeedService();
    SeedData seedData = seedService.getSeedDataByName(instance.getSeedId());

    if (seedData == null) return 0.0;

    return instance.getGrowthProgress(seedData, worldState);
  }

  private double calculateWeightKg(SeedData seed) {
    return RandomUtil.getRandomDouble(seed.getBaseWeightKgMin(), seed.getBaseWeightKgMax());
  }
}*/
