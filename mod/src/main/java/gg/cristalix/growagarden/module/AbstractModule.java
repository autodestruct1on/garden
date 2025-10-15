package gg.cristalix.growagarden.module;

import dev.xdark.clientapi.event.Event;
import dev.xdark.clientapi.event.RegisteredListener;
import dev.xdark.clientapi.resource.ResourceLocation;
import gg.cristalix.enginex.Enginex;
import gg.cristalix.enginex.transfer.ModTransfer;
import gg.cristalix.growagarden.GrowAGardenMod;
import gg.cristalix.growagarden.channel.ChannelHandler;
import gg.cristalix.growagarden.util.TextureLoader;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@Getter
@Setter
public abstract class AbstractModule implements IModuleChannel {

  protected GrowAGardenMod mod;
  protected ModuleManager moduleManager;
  protected ChannelHandler channelHandler;

  private Set<RegisteredListener> subscribedEvents = new HashSet<>();

  public void instance(ModuleManager moduleManager) {
    this.mod = moduleManager.getMod();
    this.moduleManager = moduleManager;
    this.channelHandler = moduleManager.getChannelHandler();

    init();
    registerChannels(channelHandler);
  }

  public abstract void init();

  public void load() {
  }

  public void unload() {
  }

  @Override
  public void registerChannels(ChannelHandler channelHandler) {
  }

  public void registerChannel(String channel, Consumer<ModTransfer> consumer) {
    channelHandler.register(channel, consumer);
  }

  public <E extends Event> void registerEvent(Class<E> event, Consumer<E> consumer) {
    registerEvent(event, consumer, 1);
  }

  public <E extends Event> void registerEvent(Class<E> event, Consumer<E> consumer, int priority) {
    Enginex.registerEvent(event, consumer, priority);
  }

  public void loadTexture(String url, Consumer<ResourceLocation> consumer) {
    TextureLoader.setTexture(url, consumer);
  }
}
