/*
package gg.cristalix.growagarden.service.garden;

import gg.cristalix.growagarden.model.garden.vegetation.MutationType;
import gg.cristalix.growagarden.model.garden.vegetation.SeedInstance;
import gg.cristalix.growagarden.model.world.weather.WeatherData;
import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class MutationService {

    public void applyWeatherMutations(SeedInstance instance, WeatherData weather) {
        if (weather == null || weather.getPossibleMutations().isEmpty()) {
            return;
        }

        for (WeatherData.MutationConfig config : weather.getPossibleMutations()) {
            if (!canApplyMutation(instance, config)) {
                continue;
            }

            if (shouldApplyMutation(config)) {
                instance.addMutation(config.getMutationType());
            }
        }
    }

    public void applyHarvestMutations(SeedInstance instance, WeatherData weather) {
        if (weather == null || weather.getPossibleMutations().isEmpty()) {
            return;
        }

        for (WeatherData.MutationConfig config : weather.getPossibleMutations()) {
            MutationType mutationType = config.getMutationType();

            if (!isRareMutation(mutationType)) {
                continue;
            }

            if (instance.hasMutation(mutationType)) {
                continue;
            }

            if (!canApplyMutation(instance, config)) {
                continue;
            }

            double harvestChance = 0.1;
            if (ThreadLocalRandom.current().nextDouble() < harvestChance) {
                instance.addMutation(mutationType);
            }
        }
    }

    private boolean canApplyMutation(SeedInstance instance, WeatherData.MutationConfig config) {
        if (instance.hasMutation(config.getMutationType())) {
            return false;
        }

        if (config.getRequiredMutations().isEmpty()) {
            return true;
        }

        for (MutationType required : config.getRequiredMutations()) {
            if (!instance.hasMutation(required)) {
                return false;
            }
        }

        return true;
    }

    private boolean shouldApplyMutation(WeatherData.MutationConfig config) {
        double chance = config.getChance();
        return ThreadLocalRandom.current().nextDouble() < chance;
    }

    private boolean isRareMutation(MutationType mutation) {
        return mutation == MutationType.AURORA || mutation == MutationType.SUNDRIED;
    }
}*/
