//package gg.cristalix.growagarden.model.garden;
//
//import gg.cristalix.growagarden.model.garden.vegetation.Crop;
//import gg.cristalix.growagarden.model.garden.vegetation.SeedData;
//import gg.cristalix.growagarden.model.garden.vegetation.SeedInstance;
//import gg.cristalix.growagarden.model.world.WorldState;
//import gg.cristalix.growagarden.model.world.weather.WeatherData;
//import lombok.Data;
//import org.bukkit.Location;
//import ru.cristalix.core.math.V2;
//
//import java.util.*;
//import java.util.concurrent.ThreadLocalRandom;
//import java.util.function.Function;
//
//@Data
//public class GardenBed {
//  private static final int SIZE_X = 3;
//  private static final int SIZE_Y = 3;
//
//  private UUID bedId;
//  private
//  private Map<String, CellData> cells;
//  private Location centerLocation;
//
//  private Map<String, V2> blockToCellMap;
//
//  public void init() {
//    bedId = UUID.randomUUID();
//    cells = new HashMap<>();
//    blockToCellMap = new HashMap<>();
//
//    // Создаем ячейки для грядки 3x3 с центром в 0,0
//    for (int x = -1; x <= 1; x++) {
//      for (int z = -1; z <= 1; z++) {
//        V2 point = new V2(x, z);
//        CellData cell = new CellData(point);
//        cells.put(x + "," + z, cell);
//      }
//    }
//  }
//
//  /**
//   * Устанавливает центральную позицию грядки и создает маппинг координат
//   */
//  public void setCenterLocation(Location centerLocation) {
//    this.centerLocation = centerLocation.clone();
//    updateBlockMapping();
//  }
//
//  /**
//   * Обновляет маппинг блоков к ячейкам грядки
//   */
//  private void updateBlockMapping() {
//    blockToCellMap.clear();
//    if (centerLocation == null) return;
//
//    // Создаем маппинг для грядки 3x3
//    for (int x = -1; x <= 1; x++) {
//      for (int z = -1; z <= 1; z++) {
//        int blockX = centerLocation.getBlockX() + x;
//        int blockZ = centerLocation.getBlockZ() + z;
//        String blockKey = blockX + "," + blockZ;
//        blockToCellMap.put(blockKey, new V2(x, z));
//      }
//    }
//  }
//
//  /**
//   * Получает координаты ячейки по координатам блока
//   */
//  public V2 getCellFromBlockLocation(Location blockLocation) {
//    if (blockToCellMap.isEmpty()) return null;
//
//    String blockKey = blockLocation.getBlockX() + "," + blockLocation.getBlockZ();
//    return blockToCellMap.get(blockKey);
//  }
//
//  /**
//   * Проверяет, принадлежит ли блок этой грядке
//   */
//  public boolean containsBlock(Location blockLocation) {
//    String blockKey = blockLocation.getBlockX() + "," + blockLocation.getBlockZ();
//    return blockToCellMap.containsKey(blockKey);
//  }
//
//  /**
//   * Посадить семя в указанную клетку.
//   *
//   * @return true если посадка успешна
//   */
//  public boolean plantSeed(V2 point, SeedData seedData) {
//    String key = (int) point.getX() + "," + (int) point.getY();
//    CellData cell = cells.get(key);
//    if (cell == null) return false;
//    if (cell.getSeedInstance() != null) return false;
//
//    long now = System.currentTimeMillis();
//    SeedInstance instance = new SeedInstance();
//    instance.setSeedId(seedData.getId());
//    instance.setCurrentStage(0);
//    instance.setPlantedAtMillis(now);
//    instance.setLastStageChangeAtMillis(now);
//    instance.setWatered(false);
//    instance.setLastWateredAtMillis(0L);
//    instance.setFinalWeightCalculated(false);
//    cell.setSeedInstance(instance);
//    return true;
//  }
//
//  /**
//   * Тик роста для всех клеток грядки.
//   */
//  public void tickGrowth(WorldState worldState, Function<String, SeedData> seedResolver) {
//    long now = System.currentTimeMillis();
//    for (CellData cell : cells.values()) {
//      SeedInstance instance = cell.getSeedInstance();
//      if (instance == null) continue;
//
//      SeedData seed = seedResolver.apply(instance.getSeedId());
//      if (seed == null) continue;
//
//      int stages = Math.max(1, seed.getStages());
//      if (instance.getCurrentStage() >= stages) continue; // Полностью созрело
//
//      long basePerStage = Math.max(1L, seed.getGrowTimeMillis() / stages);
//      double multiplier = computeGrowthMultiplier(worldState, instance);
//      long required = (long) Math.max(1.0, basePerStage / multiplier);
//
//      if (now - instance.getLastStageChangeAtMillis() >= required) {
//        instance.setCurrentStage(instance.getCurrentStage() + 1);
//        instance.setLastStageChangeAtMillis(now);
//      }
//    }
//  }
//
//  /**
//   * Вычисляет множитель роста с учетом погоды и полива
//   */
//  public double computeGrowthMultiplier(WorldState worldState, SeedInstance instance) {
//    double multiplier = 1.0;
//    if (worldState != null) {
//      WeatherData weather = worldState.getCurrentWeather();
//      if (weather != null && weather.getGrowthRateMultiplier() > 0) {
//        multiplier *= weather.getGrowthRateMultiplier();
//      }
//    }
//
//    instance.updateWateringStatus();
//    if (instance.isWateringActive()) {
//      multiplier *= 1.1;
//    }
//
//    return multiplier;
//  }
//
//  /**
//   * Сбор урожая из клетки, если растение созрело.
//   * Возвращает Crop при успешном сборе, иначе empty.
//   */
//  public Optional<Crop> harvestCell(V2 point, Function<String, SeedData> seedResolver) {
//    String key = (int) point.getX() + "," + (int) point.getY();
//    CellData cell = cells.get(key);
//    if (cell == null) return Optional.empty();
//    SeedInstance instance = cell.getSeedInstance();
//    if (instance == null) return Optional.empty();
//
//    SeedData seed = seedResolver.apply(instance.getSeedId());
//    if (seed == null) return Optional.empty();
//
//    if (instance.getCurrentStage() < seed.getStages()) {
//      return Optional.empty();
//    }
//
//    Crop crop = new Crop();
//    crop.setSeedId(seed.getId());
//    crop.setWeightKg(calculateWeightKg(seed));
//    crop.setAmount(seed.isMultiHarvest() ? 1 : 1);
//
//    if (seed.isMultiHarvest()) {
//      instance.setCurrentStage(Math.max(0, seed.getStages() - 1));
//      instance.setWatered(false);
//      instance.setLastStageChangeAtMillis(System.currentTimeMillis());
//    } else {
//      cell.setSeedInstance(null);
//    }
//
//    return Optional.of(crop);
//  }
//
//  /**
//   * Полить клетку.
//   */
//  public boolean waterCell(V2 point) {
//    String key = (int) point.getX() + "," + (int) point.getY();
//    CellData cell = cells.get(key);
//    if (cell == null) return false;
//    SeedInstance instance = cell.getSeedInstance();
//    if (instance == null) return false;
//
//    // Проверяем, не действует ли уже полив
//    if (instance.isWateringActive()) {
//      return false; // Нельзя поливать повторно пока действует эффект
//    }
//
//    instance.water();
//    return true;
//  }
//
//  private double calculateWeightKg(SeedData seed) {
//    double min = Math.max(0.0, seed.getBaseWeightKgMin());
//    double max = Math.max(min, seed.getBaseWeightKgMax());
//    return ThreadLocalRandom.current().nextDouble(min, max);
//  }
//}