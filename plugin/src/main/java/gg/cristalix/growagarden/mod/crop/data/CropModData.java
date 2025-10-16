package gg.cristalix.growagarden.mod.crop.data;

import gg.cristalix.wada.math.V3;
import gg.cristalix.wada.transfer.ModTransfer;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CropModData {
  UUID uuid;
  V3 position;
  int currentStage;
  double growthProgress;
  boolean watered;
  List<String> lore;
  String model;
  V3 modelSize;
//  long remainingTimeMillis;

  public void serialize(ModTransfer transfer) {
    transfer.writeUUID(uuid)
            .writeV3(position)
            .writeInt(currentStage)
            .writeDouble(growthProgress)
            .writeBoolean(watered)
            .writeCollection(lore, ModTransfer::writeString)
            .writeString(model)
            .writeV3(modelSize);
  }
}