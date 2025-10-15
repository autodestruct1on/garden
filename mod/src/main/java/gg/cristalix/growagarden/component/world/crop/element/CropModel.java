package gg.cristalix.growagarden.component.world.crop.element;

import gg.cristalix.enginex.Enginex;
import gg.cristalix.enginex.element.AbstractElement;
import gg.cristalix.enginex.element.model.type.WorldBedrockModel;
import gg.cristalix.enginex.event.HoverEvent;
import gg.cristalix.enginex.event.input.MouseClickEvent;
import gg.cristalix.enginex.event.input.MouseLeftClickEvent;
import gg.cristalix.enginex.event.input.MouseRightClickEvent;
import gg.cristalix.enginex.event.tick.PreTransformEvent;
import gg.cristalix.enginex.manager.InteractionManager;
import gg.cristalix.enginex.math.Relative;
import gg.cristalix.enginex.math.V3;
import gg.cristalix.enginex.math.boundingbox.util.BoundingBoxUtil;
import gg.cristalix.growagarden.channel.ChannelHandler;
import gg.cristalix.growagarden.component.world.crop.data.CropData;
import gg.cristalix.growagarden.util.cache.GlobalCache;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class CropModel extends WorldBedrockModel {

  CropData cropData;
  boolean hover3d;

  public CropModel(CropData cropData) {
    super("bedrock/item/" + cropData.getModel() + ".model");

    this.cropData = cropData;

    setIs3dRender(true);
    setOrigin(new V3(0.5, 0, 0.5));
    setAlign(Relative.BOTTOM);
    setRenderSize(cropData.getModelSize());
    setPos(cropData.getPosition());

    registerEvent(PreTransformEvent.class, preTransformEvent -> {
      setHover3dState(isHover3d());
    });

    registerEvent(MouseLeftClickEvent.class, event -> {
      if (GlobalCache.checkAndUpdate("CLICK_CROP")) return;

      Enginex.sendChatMessage("click: " + cropData.getUuid());
      ChannelHandler.send("crop:click", transfer -> transfer.writeUUID(cropData.getUuid()));
    });

    registerEvent(HoverEvent.class, hoverEvent -> {
      if (hoverEvent.isHover()) {
        Enginex.sendChatMessage("lore: " + cropData.getLore());
      }
    });
  }

  public void setHover3dState(boolean hover3d) {
    if (this.hover3d == hover3d) return;
    this.hover3d = hover3d;
    fireEvent(new HoverEvent(this, hover3d));
  }

  public boolean isHover3d() {
    return BoundingBoxUtil.raytraceFromPlayerToBox(boundingBox);
  }

  @Override
  public boolean mouseClick(InteractionManager interactionManager, int mouse) {
    if (!enabled) return false;
    for (AbstractElement<?> child : children) {
      if (child.mouseClick(interactionManager, mouse)) {
        return true;
      }
    }
    if (!isHover3d()) return false;
    if (mouse == 0) {
      isLeftPressed = true;
      interactionManager.setFocusedElement(this);
      fireEvent(new MouseLeftClickEvent(this));
    } else if (mouse == 1) {
      isRightPressed = true;
      fireEvent(new MouseRightClickEvent(this));
    }

    fireEvent(new MouseClickEvent(this, mouse));
    return true;
  }
}
