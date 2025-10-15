package gg.cristalix.growagarden.component.overlay;

import gg.cristalix.growagarden.module.AbstractModule;
import gg.cristalix.growagarden.module.IModule;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.function.Supplier;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class OverlayManager extends AbstractModule {

  @Override
  public void init() {
  }

  public <T extends IModule> void registerModule(Class<T> clazz, Supplier<T> supplier) {
    moduleManager.registerModule(clazz, supplier);
  }

}
