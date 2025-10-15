package gg.cristalix.growagarden.model.world.weather;

import lombok.Getter;

@Getter
public enum WeatherType {
    SUN("Солнечно", 1.0, 0.0),
    NIGHT("Ночь", 1.0, 0.05),
    RAIN("Дождь", 1.5, 0.15),
    FROST("Мороз", 1.5, 0.12),
    THUNDERSTORM("Гроза", 1.5, 0.08),
    BLOOD_MOON("Кровавая луна", 1.0, 0.03),
    HEATWAVE("Жара", 1.0, 0.02),
    WINDY("Ветер", 1.0, 0.1),
    TROPICAL_RAIN("Тропический дождь", 1.5, 0.06),
    DROUGHT("Засуха", 0.8, 0.15),
    AURORA("Северное сияние", 1.5, 0.02);

    private final String displayName;
    private final double growthMultiplier;
    private final double baseMutationChance;

    WeatherType(String displayName, double growthMultiplier, double baseMutationChance) {
        this.displayName = displayName;
        this.growthMultiplier = growthMultiplier;
        this.baseMutationChance = baseMutationChance;
    }

    public static final WeatherType[] VALUES = values();
}