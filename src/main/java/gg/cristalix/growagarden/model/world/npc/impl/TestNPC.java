package gg.cristalix.growagarden.model.world.npc.impl;

import gg.cristalix.growagarden.model.world.npc.IWorldNPC;
import gg.cristalix.growagarden.model.world.npc.NPCData;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TestNPC extends IWorldNPC {

  protected TestNPC(Location location) {
    super(NPCData.of("test", "", location));
  }

  @Override
  protected void open(Player player) {

  }
}
