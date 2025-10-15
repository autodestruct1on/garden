package gg.cristalix.growagarden.model.garden.vegetation;

import gg.cristalix.growagarden.model.world.WorldState;
import gg.cristalix.growagarden.model.world.weather.WeatherData;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeedInstance {
  String seedId;
  long plantedAtMillis;
  boolean isWatered;
  double calculatedFinalWeightKg;
  boolean finalWeightCalculated;
  Set<MutationType> mutations = new HashSet<>();

  public void water() {
    this.isWatered = true;
  }

  public void addMutation(MutationType mutation) {
    if (mutation != MutationType.NONE) {
      mutations.add(mutation);
    }
  }

  public boolean hasMutation(MutationType mutation) {
    return mutations.contains(mutation);
  }

  public double getTotalPriceMultiplier() {
    if (mutations.isEmpty()) {
      return 1.0;
    }

    double multiplier = 1.0;
    for (MutationType mutation : mutations) {
      multiplier *= mutation.getPriceMultiplier();
    }
    return multiplier;
  }

  public int calculateCurrentStage(SeedData seedData, WorldState worldState) {
    if (seedData == null) return 0;

    long now = System.currentTimeMillis();
    long timeSincePlanting = now - plantedAtMillis;

    double effectiveTime = calculateEffectiveGrowthTime(timeSincePlanting, worldState);

    int maxStages = seedData.getStages();
    long totalGrowTime = seedData.getGrowTimeMillis();

    if (effectiveTime >= totalGrowTime) {
      return maxStages;
    }

    int currentStage = (int) ((effectiveTime / totalGrowTime) * maxStages);
    return Math.min(currentStage, maxStages);
  }

  public boolean isFullyGrown(SeedData seedData, WorldState worldState) {
    return calculateCurrentStage(seedData, worldState) >= seedData.getStages();
  }

  public double getGrowthProgress(SeedData seedData, WorldState worldState) {
    if (seedData == null) return 0.0;

    long now = System.currentTimeMillis();
    long timeSincePlanting = now - plantedAtMillis;

    double effectiveTime = calculateEffectiveGrowthTime(timeSincePlanting, worldState);
    long totalGrowTime = seedData.getGrowTimeMillis();

    double progress = (effectiveTime / totalGrowTime) * 100.0;
    return Math.min(progress, 100.0);
  }

  public long getRemainingGrowTime(SeedData seedData, WorldState worldState) {
    if (seedData == null) return 0;

    long now = System.currentTimeMillis();
    long timeSincePlanting = now - plantedAtMillis;

    double effectiveTime = calculateEffectiveGrowthTime(timeSincePlanting, worldState);
    long totalGrowTime = seedData.getGrowTimeMillis();

    if (effectiveTime >= totalGrowTime) {
      return 0;
    }

    double currentMultiplier = calculateGrowthMultiplier(worldState);
    double remainingEffectiveTime = totalGrowTime - effectiveTime;

    return (long) (remainingEffectiveTime / currentMultiplier);
  }

  private double calculateEffectiveGrowthTime(long realTimePassed, WorldState worldState) {
    double multiplier = calculateGrowthMultiplier(worldState);
    return realTimePassed * multiplier;
  }

  private double calculateGrowthMultiplier(WorldState worldState) {
    double multiplier = 1.0;

    if (worldState != null) {
      WeatherData weather = worldState.getCurrentWeather();
      if (weather != null && weather.getGrowthRateMultiplier() > 0) {
        multiplier *= weather.getGrowthRateMultiplier();
      }
    }

    if (isWatered) {
      multiplier *= 1.1;
    }

    return multiplier;
  }
}