package gg.cristalix.growagarden.model.garden.vegetation;

import lombok.Getter;

@Getter
public enum MutationType {
    NONE("Нет", 1.0, "§7"),
    MOONLIT("Лунный", 2.0, "§b"),
    WET("Влажный", 1.3, "§9"),
    CHILLED("Охлаждённый", 2.0, "§b"),
    FROZEN("Замороженный", 3.0, "§f"),
    SHOCKED("Заряженный", 5.0, "§e"),
    BLOODLIT("Кровавый", 4.0, "§c"),
    SUNDRIED("Высушенный солнцем", 85.0, "§6"),
    WINDSTRUCK("Обдутый ветром", 2.0, "§f"),
    DRENCHED("Промокший", 5.0, "§3"),
    WILT("Увядший", 0.5, "§8"),
    AURORA("Северное сияние", 90.0, "§d");

    private final String displayName;
    private final double priceMultiplier;
    private final String colorCode;

    MutationType(String displayName, double priceMultiplier, String colorCode) {
        this.displayName = displayName;
        this.priceMultiplier = priceMultiplier;
        this.colorCode = colorCode;
    }

    public static final MutationType[] VALUES = values();
}