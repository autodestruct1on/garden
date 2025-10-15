package gg.cristalix.growagarden.model.player;

import gg.cristalix.growagarden.model.garden.GardenData;
import gg.cristalix.growagarden.model.player.economy.CurrencyType;
import gg.cristalix.growagarden.model.player.inventory.InventoryData;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GamePlayer {

  final UUID id;

  BalanceData balance;
  GardenData garden;
  InventoryData inventoryData;

  /**
   * Метод первоначальной загрузки игрока.
   * Все данные, которые должны быть заданы при первом входе - задаются здесь.
   */
  public void init(Player player) {
    if (balance == null) this.balance = new BalanceData();
    if (garden == null) this.garden = new GardenData();
    if (inventoryData == null) this.inventoryData = new InventoryData();

    inventoryData.load(player);

    player.setBungeePlayer(this);
  }

  public void firstInit() {
    balance.setBalance(CurrencyType.MONEY, 100.0);
  }

  public double getBalance(CurrencyType currencyType) {
    return balance.getBalance(currencyType);
  }

  public boolean hasBalance(CurrencyType currencyType, double amount) {
    return balance.hasBalance(currencyType, amount);
  }

  public void addBalance(CurrencyType currencyType, double amount) {
    balance.addBalance(currencyType, amount);
  }

  public boolean removeBalance(CurrencyType currencyType, double amount) {
    return balance.removeBalance(currencyType, amount);
  }

  public static GamePlayer of(Player player) {
    return of(player.getUniqueId());
  }

  @Nullable
  public static GamePlayer of(UUID uuid) {
    Player player = Bukkit.getPlayer(uuid);
    return player != null ? player.getBungeePlayer() : null;
  }
}