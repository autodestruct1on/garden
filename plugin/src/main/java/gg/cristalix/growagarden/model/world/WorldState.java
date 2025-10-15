package gg.cristalix.growagarden.model.world;

import gg.cristalix.growagarden.model.event.EventData;
import gg.cristalix.growagarden.model.world.weather.WeatherData;
import lombok.Data;

@Data
public class WorldState {
    private WeatherData currentWeather;              // Текущая погода
    private long weatherStartedAt;                   // Время начала текущей погоды
    private EventData currentEvent;                  // Текущее активное событие
    private long worldTimeMillis;                    // Игровое время мира
}
