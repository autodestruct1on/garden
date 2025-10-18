package gg.cristalix.growagarden.manager;

import gg.cristalix.growagarden.GrowAGardenPlugin;
import gg.cristalix.growagarden.common.util.RandomUtil;
import gg.cristalix.growagarden.model.garden.CellData;
import gg.cristalix.growagarden.model.garden.vegetation.MutationType;
import gg.cristalix.growagarden.model.garden.vegetation.SeedData;
import gg.cristalix.growagarden.model.garden.vegetation.SeedInstance;
import gg.cristalix.growagarden.model.item.CropCustomItem;
import gg.cristalix.growagarden.model.player.GamePlayer;
import gg.cristalix.growagarden.model.world.WorldState;
import gg.cristalix.growagarden.model.world.weather.WeatherData;
import gg.cristalix.wada.math.V3;
import lombok.experimental.UtilityClass;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class GardenManager {

    private static final double MIN_PLANT_DISTANCE = 0.15;

    public boolean canPlantAt(GamePlayer player, V3 position) {
        return !player.getGarden().hasPlantInRadius(position, MIN_PLANT_DISTANCE);
    }

    public boolean plantSeed(GamePlayer player, V3 position, String seedId) {
        if (!canPlantAt(player, position)) return false;

        ItemManager itemManager = GrowAGardenPlugin.getInstance().getItemManager();
        SeedData seedData = itemManager.getSeedDataByName(seedId);

        if (seedData == null) return false;

        SeedInstance instance = new SeedInstance(seedData);

        CellData cellData = new CellData(position, instance);
        player.getGarden().addPlantedCell(position, cellData);

        return true;
    }

    @Nullable
    public CropCustomItem harvestCrop(GamePlayer player, V3 position, WorldState worldState) {
        CellData cell = player.getGarden().getCellAtPosition(position);
        if (cell == null) return null;

        SeedInstance instance = cell.getSeedInstance();
        ItemManager itemManager = GrowAGardenPlugin.getInstance().getItemManager();
        SeedData seedData = itemManager.getSeedDataByName(instance.getSeedId());

        if (seedData == null || !instance.isFullyGrown(seedData, worldState)) return null;

        applyHarvestMutations(instance, worldState.getCurrentWeather());

        double weight = calculateWeightKg(seedData);
        Set<MutationType> mutations = instance.getMutations();

        CropCustomItem crop = itemManager.createCrop(seedData.getId(), weight, mutations);

        if (seedData.isMultiHarvest()) {
            instance.setPlantedAtMillis(Instant.now().toEpochMilli());
            instance.setWatered(false);
            instance.setFinalWeightCalculated(false);
            instance.clearMutation();
        } else {
            player.getGarden().removeCellAtPosition(position);
        }

        return crop;
    }

    public boolean waterPlant(GamePlayer player, V3 position) {
        CellData cell = player.getGarden().getCellAtPosition(position);
        if (cell == null) return false;

        SeedInstance instance = cell.getSeedInstance();
        if (instance.isWatered()) return false;

        instance.setWatered(true);
        return true;
    }

    public boolean digPlant(GamePlayer player, V3 position) {
        if (!hasPlantAt(player, position)) return false;

        player.getGarden().removeCellAtPosition(position);
        return true;
    }

    public boolean hasPlantAt(GamePlayer player, V3 position) {
        return player.getGarden().getCellAtPosition(position) != null;
    }

    @Nullable
    public CellData getCellAt(GamePlayer player, V3 position) {
        return player.getGarden().getCellAtPosition(position);
    }

    public double getGrowthProgress(CellData cell, WorldState worldState) {
        if (cell == null || cell.getSeedInstance() == null) return 0.0;

        SeedInstance instance = cell.getSeedInstance();
        ItemManager itemManager = GrowAGardenPlugin.getInstance().getItemManager();
        SeedData seedData = itemManager.getSeedDataByName(instance.getSeedId());

        if (seedData == null) return 0.0;

        long currentTime = System.currentTimeMillis();
        long plantedAt = instance.getPlantedAtMillis();
        long growthDuration = seedData.getGrowTimeMillis();

        if (instance.isWatered()) {
            growthDuration = (long) (growthDuration * 0.9);
        }

        WeatherData currentWeather = worldState.getCurrentWeather();
        if (currentWeather != null) {
            growthDuration = (long) (growthDuration / currentWeather.getGrowthRateMultiplier());
        }

        long elapsed = currentTime - plantedAt;
        return Math.min(100.0, (elapsed * 100.0) / growthDuration);
    }

    private double calculateWeightKg(SeedData seed) {
        return RandomUtil.getRandomDouble(seed.getBaseWeightKgMin(), seed.getBaseWeightKgMax());
    }


    public void applyWeatherMutations(SeedInstance instance, WeatherData weather) {
        if (weather == null || weather.getPossibleMutations().isEmpty()) {
            return;
        }

        for (WeatherData.MutationConfig config : weather.getPossibleMutations()) {
            if (!canApplyMutation(instance, config)) {
                continue;
            }

            if (shouldApplyMutation(config)) {
                instance.addMutation(config.getMutationType());
            }
        }
    }

    public void applyHarvestMutations(SeedInstance instance, WeatherData weather) {
        if (weather == null || weather.getPossibleMutations().isEmpty()) {
            return;
        }

        for (WeatherData.MutationConfig config : weather.getPossibleMutations()) {
            MutationType mutationType = config.getMutationType();

            if (!isRareMutation(mutationType)) {
                continue;
            }

            if (instance.hasMutation(mutationType)) {
                continue;
            }

            if (!canApplyMutation(instance, config)) {
                continue;
            }

            double harvestChance = 0.1;
            if (ThreadLocalRandom.current().nextDouble() < harvestChance) {
                instance.addMutation(mutationType);
            }
        }
    }

    private boolean canApplyMutation(SeedInstance instance, WeatherData.MutationConfig config) {
        if (instance.hasMutation(config.getMutationType())) {
            return false;
        }

        if (config.getRequiredMutations().isEmpty()) {
            return true;
        }

        for (MutationType required : config.getRequiredMutations()) {
            if (!instance.hasMutation(required)) {
                return false;
            }
        }

        return true;
    }

    private boolean shouldApplyMutation(WeatherData.MutationConfig config) {
        double chance = config.getChance();
        return ThreadLocalRandom.current().nextDouble() < chance;
    }

    private boolean isRareMutation(MutationType mutation) {
        return mutation == MutationType.AURORA || mutation == MutationType.SUNDRIED;
    }
}