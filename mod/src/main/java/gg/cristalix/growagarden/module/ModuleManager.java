package gg.cristalix.growagarden.module;

import gg.cristalix.enginex.Enginex;
import gg.cristalix.growagarden.GrowAGardenMod;
import gg.cristalix.growagarden.channel.ChannelHandler;
import gg.cristalix.growagarden.component.menu.MenuManager;
import gg.cristalix.growagarden.component.overlay.OverlayManager;
import gg.cristalix.growagarden.component.world.WorldManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModuleManager {

  Map<Class<? extends IModule>, IModule> modules = new ConcurrentHashMap<>();

  GrowAGardenMod mod;
  ChannelHandler channelHandler;

  public ModuleManager(GrowAGardenMod mod) {
    this.mod = mod;
    this.channelHandler = mod.getChannelManager().getChannelHandler();

    Enginex.getCustomTooltip().setEnableCustomTooltip(true);
  }

  public <T extends IModule> T registerModule(Class<T> clazz, Supplier<T> supplier) {
    T module = (T) modules.get(clazz);
    if (module != null) return module;

    module = supplier.get();

    if (module instanceof AbstractModule) {
      ((AbstractModule) module).instance(this);
    } else if (module instanceof IModuleChannel) {
      ((IModuleChannel) module).registerChannels(channelHandler);
    }

    modules.put(clazz, module);

    return module;
  }

  @SuppressWarnings("unchecked")
  public <T extends IModule> T getModule(Class<T> clazz, Supplier<T> supplier) {
    T module = (T) modules.get(clazz);
    if (module == null) return registerModule(clazz, supplier);
    return module;
  }

  public void unloadModules() {
    for (IModule module : modules.values()) {
      if (module instanceof AbstractModule) {
        AbstractModule abstractModule = (AbstractModule) module;
        try {
          abstractModule.unload();
        } catch (Exception e) {
          GrowAGardenMod.error("Error unloading module: " + module.getClass().getName(), e);
        }
      }
    }
  }

  public Collection<IModule> getAllModules() {
    return Collections.unmodifiableCollection(modules.values());
  }

  public void onLoad() {
    registerSyncAllModules();
  }

  public void asyncOnLoad() {
    Enginex.getCustomTooltip().setEnableCustomTooltip(true);

    registerAsyncAllModules();

    ChannelHandler.send("mod:loaded");
  }

  private void registerSyncAllModules() {
  }

  private void registerAsyncAllModules() {
    registerModule(MenuManager.class, MenuManager::new);
    registerModule(OverlayManager.class, OverlayManager::new);

    registerModule(MenuManager.class, MenuManager::new);
    registerModule(WorldManager.class, WorldManager::new);
  }
}