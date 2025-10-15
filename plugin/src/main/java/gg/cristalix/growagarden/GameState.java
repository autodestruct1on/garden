package gg.cristalix.growagarden;

import gg.cristalix.growagarden.model.world.WorldState;
import gg.cristalix.growagarden.model.world.weather.WeatherData;
import gg.cristalix.growagarden.model.world.weather.WeatherType;
import lombok.Getter;

@Getter
public class GameState {
  private final WorldState worldState;

  public GameState() {
    this.worldState = new WorldState();
    WeatherData defaultWeather = new WeatherData();
    defaultWeather.setId("sun");
    defaultWeather.setName("Sunny");
    defaultWeather.setType(WeatherType.SUN);
    defaultWeather.setGrowthRateMultiplier(1.0);
    defaultWeather.setDurationMillis(Long.MAX_VALUE);
    this.worldState.setCurrentWeather(defaultWeather);
    this.worldState.setWeatherStartedAt(System.currentTimeMillis());
    this.worldState.setWorldTimeMillis(System.currentTimeMillis());
  }
}
