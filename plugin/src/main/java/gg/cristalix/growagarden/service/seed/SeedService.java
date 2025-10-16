package gg.cristalix.growagarden.service.seed;

import gg.cristalix.growagarden.model.garden.vegetation.SeedData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SeedService {

  @Getter
  Map<String, SeedData> seedDataMap;

  Map<UUID, SeedData> seedDataByUuid;

  public SeedService(Map<String, SeedData> seedDataMap) {
    this.seedDataMap = seedDataMap;
    this.seedDataByUuid = new HashMap<>();

    for (SeedData seed : seedDataMap.values()) {
      if (seed.getUuid() != null) {
        seedDataByUuid.put(seed.getUuid(), seed);
      }
    }
  }

  @Nullable
  public UUID getUuidByStringId(String seedId) {
    SeedData data = seedDataMap.get(seedId.toLowerCase());
    if (data == null) {
      log.error("Seed data not found for ID: {}", seedId);
      return null;
    }
    return data.getUuid();
  }

  @Nullable
  public SeedData getSeedDataByUUID(UUID uuid) {
    SeedData data = seedDataByUuid.get(uuid);
    if (data == null) {
      log.error("Seed data not found for UUID: {}", uuid);
    }
    return data;
  }

  @Nullable
  public SeedData getSeedDataByName(String name) {
    return seedDataMap.values().stream()
            .filter(sd -> sd.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
  }

  public boolean exists(String seedId) {
    return seedDataMap.containsKey(seedId.toLowerCase());
  }

  public boolean existsByUUID(UUID uuid) {
    return seedDataByUuid.containsKey(uuid);
  }

  public double calculateSeedPrice(SeedData seed) {
    double basePrice = seed.getSeedPrice();
    double withMultiplier = basePrice * seed.getCategory().getPriceMultiplier();
    return Math.max(seed.getCategory().getMinPrice(), withMultiplier);
  }
}