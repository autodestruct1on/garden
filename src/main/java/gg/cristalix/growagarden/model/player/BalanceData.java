package gg.cristalix.growagarden.model.player;

import gg.cristalix.growagarden.model.player.economy.CurrencyData;
import gg.cristalix.growagarden.model.player.economy.CurrencyType;
import gg.cristalix.growagarden.model.player.economy.ICurrency;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.EnumMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BalanceData {

  Map<CurrencyType, CurrencyData> currencies = new EnumMap<>(CurrencyType.class);

  private ICurrency getCurrency(CurrencyType currencyType) {
    return this.currencies.computeIfAbsent(currencyType, type -> new CurrencyData());
  }

  public double getBalance(CurrencyType currencyType) {
    return getCurrency(currencyType).getValue();
  }

  public boolean hasBalance(CurrencyType currencyType, double amount) {
    return getBalance(currencyType) >= amount;
  }

  public void addBalance(CurrencyType currencyType, double amount) {
    getCurrency(currencyType).increase(amount);
  }

  public boolean removeBalance(CurrencyType currencyType, double amount) {
    if (!hasBalance(currencyType, amount)) {
      return false;
    }
    getCurrency(currencyType).decrease(amount);
    return true;
  }

  public void setBalance(CurrencyType currencyType, double amount) {
    ICurrency currency = getCurrency(currencyType);
    if (currency instanceof CurrencyData currencyData) {
      currencyData.setValue(amount);
    }
  }
}