package gg.cristalix.growagarden.mod.crop.data;

import gg.cristalix.wada.math.V3;
import gg.cristalix.wada.transfer.ModTransfer;

import java.util.List;
import java.util.UUID;

public class CropModData {
  UUID uuid;
  V3 position;
  int currentStage;
  double growthProgress;
  boolean isWatered;
  List<String> lore; //0 - name, >0 - lore
  String model;
  V3 modelSize;
//  long remainingTimeMillis;

  public void serialize(ModTransfer transfer) {
    transfer.writeUUID(uuid)
      .writeV3(position)
      .writeInt(currentStage)
      .writeDouble(growthProgress)
      .writeBoolean(isWatered)
      .writeCollection(lore, ModTransfer::writeString)
      .writeString("banana_stage_1")
      .writeV3(modelSize);
  }
}
