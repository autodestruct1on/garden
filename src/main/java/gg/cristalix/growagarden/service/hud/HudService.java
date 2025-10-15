package gg.cristalix.growagarden.service.hud;

import gg.cristalix.growagarden.model.player.GamePlayer;
import gg.cristalix.growagarden.model.player.economy.CurrencyType;
import gg.cristalix.wada.Wada;
import gg.cristalix.wada.component.hud.IHudManager;
import gg.cristalix.wada.component.hud.data.LobbyHud;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

@UtilityClass
public class HudService {

  private final IHudManager hudManager = Wada.get().getHudManager();

  public void updateHud(Player player) {
    GamePlayer gamePlayer = player.getBungeePlayer();

    double balance = gamePlayer.getBalance(CurrencyType.MONEY);

    LobbyHud lobbyHud = LobbyHud.builder()
            .money((int) balance)
            .level("")
            .currentExp(0)
            .maxExp(0)
            .verticalPosition(-74.0)
            .build();

    hudManager.enableHudElement(lobbyHud, player);
  }

  public void removeHud(Player player) {
    LobbyHud emptyHud = LobbyHud.builder()
            .money(0)
            .level("")
            .currentExp(0)
            .maxExp(0)
            .build();

    hudManager.disableHudElement(emptyHud, player);
  }
}