package gg.cristalix.growagarden.model.player.economy;

public interface ICurrency {
    void increase(double value);
    void decrease(double value);

    double getValue();
}
