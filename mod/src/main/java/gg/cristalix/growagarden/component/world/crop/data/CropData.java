package gg.cristalix.growagarden.component.world.crop.data;


import gg.cristalix.enginex.math.V3;
import gg.cristalix.enginex.transfer.ModTransfer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CropData {
  UUID uuid;
  V3 position;
  int currentStage;
  double growthProgress;
  boolean isWatered;
  List<String> lore; //0 - name, >0 - lore
  String model;
  V3 modelSize;
//  long remainingTimeMillis;

  public static CropData deserialize(ModTransfer transfer) {
    CropData cropData = new CropData();

    cropData.uuid = transfer.readUUID();
    cropData.position = transfer.readLocation();
    cropData.currentStage = transfer.readInt();
    cropData.growthProgress = transfer.readDouble();
    cropData.isWatered = transfer.readBoolean();
    cropData.lore = transfer.readCollection(ArrayList::new, ModTransfer::readString);
    cropData.model = transfer.readString();
    cropData.modelSize = transfer.readV3();

    return cropData;
  }
}
