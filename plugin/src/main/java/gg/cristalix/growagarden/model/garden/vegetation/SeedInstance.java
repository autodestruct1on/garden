package gg.cristalix.growagarden.model.garden.vegetation;

import gg.cristalix.growagarden.mod.crop.data.CropModData;
import gg.cristalix.growagarden.model.world.WorldState;
import gg.cristalix.growagarden.model.world.weather.WeatherData;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeedInstance {
  final UUID uuid = UUID.randomUUID();

  String seedId;
  long plantedAtMillis = Instant.now().toEpochMilli();
  boolean watered = false;
  boolean finalWeightCalculated = false;
  Set<MutationType> mutations;

  @Nullable
  transient CropModData cropModData;

  public SeedInstance(SeedData seedData) {
    this.seedId = seedData.getId();
  }

  public void water() {
    this.watered = true;
  }

  public boolean hasMutation(MutationType mutation) {
    if (this.mutations == null) return false;
    return this.mutations.contains(mutation);
  }

  public void addMutation(MutationType mutation) {
    if (!hasMutation(mutation)) {
      if (this.mutations == null) this.mutations = new HashSet<>();
      this.mutations.add(mutation);
    }
  }

  public void clearMutation() {
    this.mutations = null;
  }

  public Set<MutationType> getMutations() {
    if (this.mutations == null) return Collections.EMPTY_SET;
    return new HashSet<>(this.mutations);
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

    long now = Instant.now().toEpochMilli();
    long timeSincePlanting = now - plantedAtMillis;

    double effectiveTime = calculateEffectiveGrowthTime(timeSincePlanting, worldState);
    long totalGrowTime = seedData.getGrowTimeMillis();

    if (totalGrowTime <= 0) return 1.0;

    double progress = effectiveTime / totalGrowTime;
    return Math.min(progress, 1.0);
  }

  public long getRemainingGrowTime(SeedData seedData, WorldState worldState) {
    if (seedData == null) return 0;

    long now = Instant.now().toEpochMilli();
    long timeSincePlanting = now - plantedAtMillis;

    double effectiveTime = calculateEffectiveGrowthTime(timeSincePlanting, worldState);
    long totalGrowTime = seedData.getGrowTimeMillis();

    if (effectiveTime >= totalGrowTime) return 0;

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

    if (watered) multiplier *= 1.1;

    return multiplier;
  }
}