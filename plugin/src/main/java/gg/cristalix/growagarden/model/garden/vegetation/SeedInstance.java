package gg.cristalix.growagarden.model.garden.vegetation;

import gg.cristalix.growagarden.model.world.WorldState;
import gg.cristalix.growagarden.model.world.weather.WeatherData;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeedInstance {
  UUID seedUuid;
  long plantedAtMillis;
  boolean watered;
  boolean finalWeightCalculated;
  List<MutationType> mutations = new ArrayList<>();

  public void water() {
    this.watered = true;
  }

  public boolean hasMutation(MutationType mutation) {
    return mutations.contains(mutation);
  }

  public void addMutation(MutationType mutation) {
    if (!hasMutation(mutation)) {
      mutations.add(mutation);
    }
  }

  public boolean isFullyGrown(SeedData seedData, WorldState worldState) {
    if (seedData == null) return false;

    long now = System.currentTimeMillis();
    long timeSincePlanting = now - plantedAtMillis;

    double effectiveTime = calculateEffectiveGrowthTime(timeSincePlanting, worldState);
    long totalGrowTime = seedData.getGrowTimeMillis();

    return effectiveTime >= totalGrowTime;
  }

  public int calculateCurrentStage(SeedData seedData, WorldState worldState) {
    if (seedData == null) return 0;

    int totalStages = Math.max(1, seedData.getStages());
    double progress = getGrowthProgress(seedData, worldState);

    int stage = (int) (progress * totalStages);
    return Math.min(stage, totalStages);
  }

  public double getGrowthProgress(SeedData seedData, WorldState worldState) {
    if (seedData == null) return 0.0;

    long now = System.currentTimeMillis();
    long timeSincePlanting = now - plantedAtMillis;

    double effectiveTime = calculateEffectiveGrowthTime(timeSincePlanting, worldState);
    long totalGrowTime = seedData.getGrowTimeMillis();

    if (totalGrowTime <= 0) return 1.0;

    double progress = effectiveTime / (double) totalGrowTime;
    return Math.min(progress, 1.0);
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

    if (watered) {
      multiplier *= 1.1;
    }

    return multiplier;
  }
}