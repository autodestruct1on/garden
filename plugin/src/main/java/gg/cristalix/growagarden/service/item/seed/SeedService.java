package gg.cristalix.growagarden.service.item.seed;

import gg.cristalix.growagarden.model.garden.vegetation.SeedData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.Map;

@Slf4j
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SeedService {
  Map<String, SeedData> seedDataMap;

  public SeedService(Map<String, SeedData> seedDataMap) {
    this.seedDataMap = seedDataMap;
  }

  @Nullable
  public SeedData getSeedDataByName(String name) {
    return seedDataMap.get(name);
  }

  public boolean exists(String seedId) {
    return seedDataMap.containsKey(seedId.toLowerCase());
  }

  public double calculateSeedPrice(SeedData seed) {
    double basePrice = seed.getSeedPrice();
    double withMultiplier = basePrice * seed.getCategory().getPriceMultiplier();
    return Math.max(seed.getCategory().getMinPrice(), withMultiplier);
  }
}