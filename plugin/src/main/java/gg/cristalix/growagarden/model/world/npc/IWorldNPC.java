package gg.cristalix.growagarden.model.world.npc;

import gg.cristalix.wada.Wada;
import gg.cristalix.wada.component.dialog.IDialogManager;
import gg.cristalix.wada.component.flexiblenpc.IFlexibleNPCManager;
import gg.cristalix.wada.component.flexiblenpc.common.FlexibleNPC;
import gg.cristalix.wada.component.menu.IMenuManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class IWorldNPC {

  protected static final IFlexibleNPCManager npcManager = Wada.get().getFlexibleNPCManager();
  protected static final IMenuManager menuManager = Wada.get().getMenuManager();
  protected static final IDialogManager dialogManager = Wada.get().getDialogManager();
  protected static final ItemStack ITEM_STACK_AIR = new ItemStack(Material.AIR);

  NPCData data;
  FlexibleNPC npc;

  protected IWorldNPC(NPCData npcData) {
    this.data = npcData;
    this.npc = createNpc();

    npcManager.add(npc);

    npc.setOnPlayerRightClick(this::onOpen);
  }

  private FlexibleNPC createNpc() {
    return FlexibleNPC.builder()
      .location(data.getLocation())
      .customName(data.getName())
      .skinFromUrl(data.getSkinUrl())
      .shouldLookAtPlayer(true)
      .build();
  }

  public void onOpen(Player player, FlexibleNPC npc) {
    if (this.npc != npc) return;

    open(player);
  }

  protected abstract void open(Player player);
}
