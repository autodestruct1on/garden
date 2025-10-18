package gg.cristalix.growagarden.mod.crop.data;

import gg.cristalix.growagarden.model.garden.CellData;
import gg.cristalix.growagarden.model.garden.vegetation.SeedData;
import gg.cristalix.growagarden.model.garden.vegetation.SeedInstance;
import gg.cristalix.growagarden.model.world.WorldState;
import gg.cristalix.wada.math.V3;
import gg.cristalix.wada.transfer.ModTransfer;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CropModData {
  UUID uuid;
  V3 position;
  int currentStage;
  double growthProgress;
  boolean watered;
  List<String> lore; //0 - name, >0 - lore
  String model;
  V3 modelSize;
//  long remainingTimeMillis;

  public CropModData(CellData cellData, SeedData seedData, WorldState worldState) {
    SeedInstance seedInstance = cellData.getSeedInstance();
    this.uuid = seedInstance.getUuid();
    this.position = cellData.getPoint();
    this.currentStage = seedInstance.calculateCurrentStage(seedData, worldState);
    this.growthProgress = seedInstance.getGrowthProgress(seedData, worldState);
    this.watered = seedInstance.isWatered();
    this.lore = updateLore(seedData);
    this.model = "banana_stage_1";
    this.modelSize = new V3(5, 7, 5);
  }

  private List<String> updateLore(SeedData seedData) {
    List<String> lore = new ArrayList<>();

    lore.add(seedData.getDisplayName());
    lore.add("§7Стадия: §f" + getCurrentStage() + "/" + seedData.getStages());
    lore.add("§7Прогресс: §f" + String.format("%.1f", getGrowthProgress() * 100) + "%");

    if (isWatered()) lore.add("§a✓ Полито");

    return lore;
  }

  public void serialize(ModTransfer transfer) {
    transfer.writeUUID(uuid)
      .writeV3(position)
      .writeInt(currentStage)
      .writeDouble(growthProgress)
      .writeBoolean(watered)
      .writeCollection(lore, ModTransfer::writeString)
      .writeString("banana_stage_1")
      .writeV3(modelSize);
  }
}