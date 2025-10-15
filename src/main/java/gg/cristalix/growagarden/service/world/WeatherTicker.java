package gg.cristalix.growagarden.service.world;

import gg.cristalix.growagarden.GrowAGardenPlugin;
import gg.cristalix.growagarden.model.garden.CellData;
import gg.cristalix.growagarden.model.garden.vegetation.SeedInstance;
import gg.cristalix.growagarden.model.player.GamePlayer;
import gg.cristalix.growagarden.model.world.WorldState;
import gg.cristalix.growagarden.model.world.weather.WeatherData;
import gg.cristalix.growagarden.service.alert.AlertService;
import gg.cristalix.growagarden.service.garden.MutationService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherTicker extends BukkitRunnable {

    static final int WEATHER_CHECK_INTERVAL = 100;

    @Getter
    static WeatherTicker instance;

    final WeatherService weatherService;
    final WorldState worldState;

    public WeatherTicker(WeatherService weatherService, WorldState worldState) {
        this.weatherService = weatherService;
        this.worldState = worldState;
    }

    @Override
    public void run() {
        if (weatherService.shouldChangeWeather(worldState)) {
            WeatherData oldWeather = worldState.getCurrentWeather();
            weatherService.changeWeather(worldState);
            WeatherData newWeather = worldState.getCurrentWeather();

            if (!newWeather.getId().equals(oldWeather.getId())) {
                announceWeatherChange(newWeather);
                applyMutationsToAllPlants(newWeather);
            }
        }
    }

    private void announceWeatherChange(WeatherData weather) {
        String message = "§6Погода изменилась: " + weather.getName();

        for (Player player : Bukkit.getOnlinePlayers()) {
            AlertService.sendInfo(player, message);
        }
    }

    private void applyMutationsToAllPlants(WeatherData weather) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            GamePlayer gamePlayer = player.getBungeePlayer();
            if (gamePlayer == null) continue;

            Map<String, CellData> cells = gamePlayer.getGarden().getAllPlantedCells();
            for (CellData cell : cells.values()) {
                SeedInstance instance = cell.getSeedInstance();
                if (instance != null) {
                    MutationService.applyWeatherMutations(instance, weather);
                }
            }
        }
    }

    public static void start(GrowAGardenPlugin plugin, WeatherService weatherService, WorldState worldState) {
        if (instance != null && !instance.isCancelled()) {
            return;
        }
        instance = new WeatherTicker(weatherService, worldState);
        instance.runTaskTimer(plugin, WEATHER_CHECK_INTERVAL, WEATHER_CHECK_INTERVAL);
    }

    public static void stop() {
        if (instance != null) {
            instance.cancel();
            instance = null;
        }
    }
}