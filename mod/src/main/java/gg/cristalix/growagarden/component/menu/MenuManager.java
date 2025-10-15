package gg.cristalix.growagarden.component.menu;

import gg.cristalix.growagarden.component.menu.inventory.InventoryManager;
import gg.cristalix.growagarden.module.AbstractModule;
import gg.cristalix.growagarden.module.IModule;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.function.Supplier;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuManager extends AbstractModule {

  @Override
  public void init() {
    registerModule(InventoryManager.class, InventoryManager::new);
  }

  public <T extends IModule> void registerModule(Class<T> clazz, Supplier<T> supplier) {
    moduleManager.registerModule(clazz, supplier);
  }

}
