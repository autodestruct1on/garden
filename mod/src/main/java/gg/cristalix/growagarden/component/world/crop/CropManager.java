package gg.cristalix.growagarden.component.world.crop;

import gg.cristalix.enginex.Enginex;
import gg.cristalix.enginex.render.context.type.WorldPostContext;
import gg.cristalix.enginex.transfer.ModTransfer;
import gg.cristalix.growagarden.component.world.crop.data.CropData;
import gg.cristalix.growagarden.component.world.crop.element.CropModel;
import gg.cristalix.growagarden.module.AbstractModule;

import java.util.*;

public class CropManager extends AbstractModule {

  Map<UUID, CropModel> cropModelMap = new HashMap<>();

  @Override
  public void init() {
    registerChannel("crop:load", this::load);
    registerChannel("crop:remove", this::remove);
    registerChannel("crop:update", this::update);

//    EntityPlayerSP player = Enginex.getPlayer();
//    load(
//      new CropData(
//        UUID.randomUUID(),
//        new V3(player.getX(), player.getY(), player.getZ()),
//        new V3(5, 5, 5),
//        Arrays.asList("test message1", "test message2", "test message3"),
//        "banana_stage_1"
//      )
//    );
  }

  private void load(ModTransfer transfer) {
    List<CropData> cropData = transfer.readCollection(ArrayList::new, CropData::deserialize);

    for (CropData cropDatum : cropData) {
      load(cropDatum);
    }
  }

  private void load(CropData cropData) {
    Enginex.sendChatMessage(cropData.getPosition() + " " + cropData.getUuid());
    CropModel cropModel = new CropModel(cropData);

    this.cropModelMap.put(cropData.getUuid(), cropModel);

    WorldPostContext.add(cropModel);
  }

  private void update(ModTransfer transfer) {
  }

  private void remove(ModTransfer transfer) {
    List<UUID> uuids = transfer.readCollection(ArrayList::new, ModTransfer::readUUID);
    for (UUID uuid : uuids) {
      CropModel remove = this.cropModelMap.remove(uuid);
      if (remove != null) WorldPostContext.remove(remove);
    }
  }


}
