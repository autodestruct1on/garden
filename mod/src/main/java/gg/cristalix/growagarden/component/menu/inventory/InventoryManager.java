package gg.cristalix.growagarden.component.menu.inventory;

import dev.xdark.clientapi.event.input.KeyPress;
import gg.cristalix.growagarden.channel.ChannelHandler;
import gg.cristalix.growagarden.common.mod.inventory.ItemEnum;
import gg.cristalix.growagarden.module.AbstractModule;
import gg.cristalix.growagarden.util.cache.GlobalCache;
import org.lwjgl.input.Keyboard;

public class InventoryManager extends AbstractModule {
  @Override
  public void init() {
    InventoryMenu inventoryMenu = new InventoryMenu();

    registerChannel("inv:open", inventoryMenu::open);

    registerEvent(KeyPress.class, event -> {
      if (event.getKey() != Keyboard.KEY_E) return;
      event.setCancelled(true);

      if (GlobalCache.check("OPEN_INVENTORY")) return;
      ChannelHandler.send("inv:open", t -> t.writeInt(ItemEnum.ALL.ordinal()));
    });
  }
}
