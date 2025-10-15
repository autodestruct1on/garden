package gg.cristalix.growagarden.channel;

import gg.cristalix.growagarden.GrowAGardenMod;
import gg.cristalix.growagarden.component.world.WorldManager;
import gg.cristalix.growagarden.module.ModuleManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChannelManager {

  GrowAGardenMod mod;
  @Getter
  ChannelHandler channelHandler;

  public ChannelManager(GrowAGardenMod mod) {
    this.mod = mod;
    this.channelHandler = new ChannelHandler();
  }

  public void setupChannels() {
    channelHandler.register("world:change", transfer -> {
      ModuleManager moduleManager = mod.getModuleManager();
      if (moduleManager == null) return;

      WorldManager module = moduleManager.getModule(WorldManager.class, WorldManager::new);
      String world = transfer.readString();
      module.changeWorld(world);
    });
  }
}