package gg.cristalix.growagarden;

import gg.cristalix.enginex.Enginex;
import gg.cristalix.enginex.JavaMod;
import gg.cristalix.growagarden.channel.ChannelManager;
import gg.cristalix.growagarden.module.ModuleManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GrowAGardenMod extends JavaMod {

  public static GrowAGardenMod instance;

  ModuleManager moduleManager;
  ChannelManager channelManager;

  @Override
  public void onLoad() {
    GrowAGardenMod.instance = this;

    this.channelManager = new ChannelManager(this);
    this.moduleManager = new ModuleManager(this);

    this.onDisable.add(this::addDisableHook);

    this.moduleManager.onLoad();
  }

  private void addDisableHook() {
    moduleManager.unloadModules();
  }

  @Override
  public void asyncOnLoad() {
    channelManager.setupChannels();
    moduleManager.asyncOnLoad();
  }

  public static void error(String error, Throwable throwable) {
    Enginex.log(error);
    throwable.printStackTrace();
  }
}
