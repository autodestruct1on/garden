package gg.cristalix.growagarden.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import dev.xdark.clientapi.entity.AbstractClientPlayer;
import dev.xdark.clientapi.entity.Entity;
import dev.xdark.clientapi.entity.EntityPlayerSP;
import dev.xdark.clientapi.entity.EntityProvider;
import dev.xdark.clientapi.p13n.P13nProvider;
import gg.cristalix.enginex.Enginex;
import gg.cristalix.growagarden.common.util.RandomUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import java.util.Base64;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@UtilityClass
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FakePlayer {

  AbstractClientPlayer playerRender;
  AbstractClientPlayer currentPlayerRender;

  public AbstractClientPlayer getPlayerRender() {
    if (playerRender == null) loadPlayer();
    return playerRender;
  }

  public AbstractClientPlayer getCurrentPlayerRender() {
    if (currentPlayerRender == null) loadCurrentPlayer();
    return currentPlayerRender;
  }

  private void loadPlayer() {
    EntityPlayerSP player = Enginex.getPlayer();

    Entity entity = Enginex.getClientApi().entityProvider().newEntity(EntityProvider.PLAYER, Enginex.getWorld());
    entity.setEntityId(gg.cristalix.growagarden.common.util.RandomUtil.getRandom().nextInt() * Integer.MAX_VALUE);

    playerRender = (AbstractClientPlayer) entity;
    playerRender.setPlayerInfo(player.getPlayerInfo());
  }

  private void loadCurrentPlayer() {
    EntityPlayerSP player = Enginex.getPlayer();

    Entity entity = Enginex.getClientApi().entityProvider().newEntity(EntityProvider.PLAYER, Enginex.getWorld());
    entity.setEntityId(RandomUtil.getRandom().nextInt() * Integer.MAX_VALUE);
    PropertyMap properties = player.getPlayerInfo().getGameProfile().getProperties();

    UUID uuid = player.getUniqueID();
    currentPlayerRender = (AbstractClientPlayer) entity;
    GameProfile gameProfile = new GameProfile(uuid, player.getName());
    PropertyMap properties1 = gameProfile.getProperties();
    for (Map.Entry<String, Property> entry : properties.entries()) {
      properties1.put(entry.getKey(), entry.getValue());
    }
    currentPlayerRender.setGameProfile(gameProfile);

    currentPlayerRender.setUniqueId(uuid);

    P13nProvider provider = Enginex.getClientApi().p13nProvider();
    Set<UUID> playerModels = provider.getPlayerModels(uuid);
    for (UUID playerModel : playerModels) {
      provider.enableModel(currentPlayerRender, playerModel);
    }
  }

  public GameProfile getGameProfile(String url) {
    String path = String.format("{\"textures\":{\"SKIN\":{\"url\":\"%s\"}}}", url);
    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
    profile.getProperties().put("textures", new Property("textures", Base64.getEncoder().encodeToString(path.getBytes())));
    return profile;
  }
}
