package gg.cristalix.growagarden.model.player.economy;

import lombok.Getter;
import lombok.Setter;

public class CurrencyData implements ICurrency {

  @Getter
  @Setter
  private double value = 0.0;

  @Override
  public void increase(double value) {
    this.value += value;
  }

  @Override
  public void decrease(double value) {
    this.value -= value;
    if (this.value < 0) {
      this.value = 0;
    }
  }
}