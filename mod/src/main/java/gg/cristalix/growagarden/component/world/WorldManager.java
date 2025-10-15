package gg.cristalix.growagarden.component.world;

import gg.cristalix.growagarden.component.world.crop.CropManager;
import gg.cristalix.growagarden.module.AbstractWorldModule;
import gg.cristalix.growagarden.module.IModule;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class WorldManager extends AbstractWorldModule {

  Map<Class<? extends IModule>, IModule> modules = new ConcurrentHashMap<>();

  @Override
  public void init() {
    registerModule(CropManager.class, CropManager::new);
  }

  public <T extends IModule> void registerModule(Class<T> clazz, Supplier<T> supplier) {
    T t = moduleManager.registerModule(clazz, supplier);
    modules.put(clazz, t);
  }

  @Override
  public void changeWorld(String world) {
    for (IModule value : modules.values()) {
      if (value instanceof AbstractWorldModule) {
        ((AbstractWorldModule) value).changeWorld(world);
      }
    }
  }
}
