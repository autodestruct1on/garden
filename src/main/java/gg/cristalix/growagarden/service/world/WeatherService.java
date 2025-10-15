package gg.cristalix.growagarden.service.world;

import gg.cristalix.growagarden.model.garden.vegetation.MutationType;
import gg.cristalix.growagarden.model.world.WorldState;
import gg.cristalix.growagarden.model.world.weather.WeatherData;
import gg.cristalix.growagarden.model.world.weather.WeatherType;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WeatherService {

    List<WeatherData> weatherPool;

    public WeatherService() {
        this.weatherPool = new ArrayList<>();
        initializeWeatherPool();
    }

    private void initializeWeatherPool() {
        weatherPool.add(createSunWeather());
        weatherPool.add(createNightWeather());
        weatherPool.add(createRainWeather());
        weatherPool.add(createFrostWeather());
        weatherPool.add(createThunderstormWeather());
        weatherPool.add(createBloodMoonWeather());
        weatherPool.add(createHeatwaveWeather());
        weatherPool.add(createWindyWeather());
        weatherPool.add(createTropicalRainWeather());
        weatherPool.add(createDroughtWeather());
        weatherPool.add(createAuroraWeather());
    }

    private WeatherData createSunWeather() {
        WeatherData weather = new WeatherData();
        weather.setId("sun");
        weather.setName("§eСолнечно");
        weather.setType(WeatherType.SUN);
        weather.setGrowthRateMultiplier(1.0);
        weather.setDurationMillis(3600000);
        return weather;
    }

    private WeatherData createNightWeather() {
        WeatherData weather = new WeatherData();
        weather.setId("night");
        weather.setName("§8Ночь");
        weather.setType(WeatherType.NIGHT);
        weather.setGrowthRateMultiplier(1.0);
        weather.setDurationMillis(1800000);
        weather.addMutation(MutationType.MOONLIT, 0.05);
        return weather;
    }

    private WeatherData createRainWeather() {
        WeatherData weather = new WeatherData();
        weather.setId("rain");
        weather.setName("§9Дождь");
        weather.setType(WeatherType.RAIN);
        weather.setGrowthRateMultiplier(1.5);
        weather.setDurationMillis(2400000);
        weather.addMutation(MutationType.WET, 0.15);
        return weather;
    }

    private WeatherData createFrostWeather() {
        WeatherData weather = new WeatherData();
        weather.setId("frost");
        weather.setName("§bМороз");
        weather.setType(WeatherType.FROST);
        weather.setGrowthRateMultiplier(1.5);
        weather.setDurationMillis(2100000);
        weather.addMutation(MutationType.CHILLED, 0.12);
        weather.addConditionalMutation(MutationType.FROZEN, 0.3, MutationType.WET);
        return weather;
    }

    private WeatherData createThunderstormWeather() {
        WeatherData weather = new WeatherData();
        weather.setId("thunderstorm");
        weather.setName("§5Гроза");
        weather.setType(WeatherType.THUNDERSTORM);
        weather.setGrowthRateMultiplier(1.5);
        weather.setDurationMillis(1500000);
        weather.addMutation(MutationType.SHOCKED, 0.08);
        weather.addMutation(MutationType.WET, 0.1);
        return weather;
    }

    private WeatherData createBloodMoonWeather() {
        WeatherData weather = new WeatherData();
        weather.setId("blood_moon");
        weather.setName("§cКровавая луна");
        weather.setType(WeatherType.BLOOD_MOON);
        weather.setGrowthRateMultiplier(1.0);
        weather.setDurationMillis(1200000);
        weather.addMutation(MutationType.BLOODLIT, 0.03);
        return weather;
    }

    private WeatherData createHeatwaveWeather() {
        WeatherData weather = new WeatherData();
        weather.setId("heatwave");
        weather.setName("§6Жара");
        weather.setType(WeatherType.HEATWAVE);
        weather.setGrowthRateMultiplier(1.0);
        weather.setDurationMillis(3000000);
        weather.addMutation(MutationType.SUNDRIED, 0.02);
        return weather;
    }

    private WeatherData createWindyWeather() {
        WeatherData weather = new WeatherData();
        weather.setId("windy");
        weather.setName("§fВетер");
        weather.setType(WeatherType.WINDY);
        weather.setGrowthRateMultiplier(1.0);
        weather.setDurationMillis(1800000);
        weather.addMutation(MutationType.WINDSTRUCK, 0.1);
        return weather;
    }

    private WeatherData createTropicalRainWeather() {
        WeatherData weather = new WeatherData();
        weather.setId("tropical_rain");
        weather.setName("§3Тропический дождь");
        weather.setType(WeatherType.TROPICAL_RAIN);
        weather.setGrowthRateMultiplier(1.5);
        weather.setDurationMillis(2700000);
        weather.addMutation(MutationType.DRENCHED, 0.06);
        return weather;
    }

    private WeatherData createDroughtWeather() {
        WeatherData weather = new WeatherData();
        weather.setId("drought");
        weather.setName("§8Засуха");
        weather.setType(WeatherType.DROUGHT);
        weather.setGrowthRateMultiplier(0.8);
        weather.setDurationMillis(3600000);
        weather.addMutation(MutationType.WILT, 0.15);
        return weather;
    }

    private WeatherData createAuroraWeather() {
        WeatherData weather = new WeatherData();
        weather.setId("aurora");
        weather.setName("§dСеверное сияние");
        weather.setType(WeatherType.AURORA);
        weather.setGrowthRateMultiplier(1.5);
        weather.setDurationMillis(1800000);
        weather.addMutation(MutationType.AURORA, 0.02);
        return weather;
    }

    public WeatherData getRandomWeather() {
        int totalWeight = calculateTotalWeight();
        int randomValue = ThreadLocalRandom.current().nextInt(totalWeight);

        int currentWeight = 0;
        for (WeatherData weather : weatherPool) {
            currentWeight += getWeatherWeight(weather.getType());
            if (randomValue < currentWeight) {
                return copyWeather(weather);
            }
        }

        return copyWeather(weatherPool.get(0));
    }

    private int calculateTotalWeight() {
        int total = 0;
        for (WeatherData weather : weatherPool) {
            total += getWeatherWeight(weather.getType());
        }
        return total;
    }

    private int getWeatherWeight(WeatherType type) {
        return switch (type) {
            case SUN -> 300;
            case NIGHT -> 150;
            case RAIN -> 200;
            case FROST -> 100;
            case THUNDERSTORM -> 50;
            case BLOOD_MOON -> 20;
            case HEATWAVE -> 30;
            case WINDY -> 120;
            case TROPICAL_RAIN -> 60;
            case DROUGHT -> 80;
            case AURORA -> 15;
        };
    }

    private WeatherData copyWeather(WeatherData original) {
        WeatherData copy = new WeatherData();
        copy.setId(original.getId());
        copy.setName(original.getName());
        copy.setType(original.getType());
        copy.setGrowthRateMultiplier(original.getGrowthRateMultiplier());
        copy.setDurationMillis(original.getDurationMillis());
        copy.setPossibleMutations(new ArrayList<>(original.getPossibleMutations()));
        return copy;
    }

    public void changeWeather(WorldState worldState) {
        WeatherData newWeather = getRandomWeather();
        worldState.setCurrentWeather(newWeather);
        worldState.setWeatherStartedAt(System.currentTimeMillis());
    }

    public boolean shouldChangeWeather(WorldState worldState) {
        long now = System.currentTimeMillis();
        WeatherData currentWeather = worldState.getCurrentWeather();

        if (currentWeather == null) {
            return true;
        }

        long elapsed = now - worldState.getWeatherStartedAt();
        return elapsed >= currentWeather.getDurationMillis();
    }
}