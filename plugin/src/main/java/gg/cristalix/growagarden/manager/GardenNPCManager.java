package gg.cristalix.growagarden.manager;

import gg.cristalix.growagarden.GrowAGardenPlugin;
import gg.cristalix.growagarden.model.world.npc.impl.CropBuyerNPC;
import gg.cristalix.growagarden.model.world.npc.impl.ItemSellerNPC;
import gg.cristalix.growagarden.model.world.npc.impl.SeedSellerNPC;
import gg.cristalix.growagarden.utils.map.MapUtil;
import org.bukkit.Location;

public class GardenNPCManager {
  private final GrowAGardenPlugin plugin;

  public GardenNPCManager(GrowAGardenPlugin plugin) {
    this.plugin = plugin;
    initializeNPCs();
  }

  private void initializeNPCs() {
    Location seedSellerLoc = MapUtil.getMapLocation("npc_seed_seller");
    if (seedSellerLoc != null) {
      new SeedSellerNPC(seedSellerLoc);
      plugin.getLogger().info("Создан NPC продавца семян в " + locationToString(seedSellerLoc));
    } else {
      plugin.getLogger().warning("Не найдена точка карты 'npc_seed_seller' для создания NPC продавца семян");
    }

    Location cropBuyerLoc = MapUtil.getMapLocation("npc_crop_buyer");
    if (cropBuyerLoc != null) {
      new CropBuyerNPC(cropBuyerLoc);
      plugin.getLogger().info("Создан NPC скупщика урожая в " + locationToString(cropBuyerLoc));
    } else {
      plugin.getLogger().warning("Не найдена точка карты 'npc_crop_buyer' для создания NPC скупщика урожая");
    }

    Location itemSellerLoc = MapUtil.getMapLocation("npc_item_seller");
    if (itemSellerLoc != null) {
      new ItemSellerNPC(itemSellerLoc);
      plugin.getLogger().info("Создан NPC продавца инструментов в " + locationToString(itemSellerLoc));
    } else {
      plugin.getLogger().warning("Не найдена точка карты 'npc_item_seller' для создания NPC продавца инструментов");
    }
  }

  private String locationToString(Location loc) {
    return String.format("X:%.1f Y:%.1f Z:%.1f", loc.getX(), loc.getY(), loc.getZ());
  }
}