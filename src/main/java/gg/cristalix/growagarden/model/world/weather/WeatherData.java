package gg.cristalix.growagarden.model.world.weather;

import gg.cristalix.growagarden.model.garden.vegetation.MutationType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WeatherData {
    private String id;
    private String name;
    private WeatherType type;
    private double growthRateMultiplier;
    private long durationMillis;
    private List<MutationConfig> possibleMutations = new ArrayList<>();

    public void addMutation(MutationType mutationType, double chance) {
        MutationConfig config = new MutationConfig();
        config.setMutationType(mutationType);
        config.setChance(chance);
        possibleMutations.add(config);
    }

    public void addConditionalMutation(MutationType mutationType, double chance, MutationType... required) {
        MutationConfig config = new MutationConfig();
        config.setMutationType(mutationType);
        config.setChance(chance);
        for (MutationType req : required) {
            config.getRequiredMutations().add(req);
        }
        possibleMutations.add(config);
    }

    @Data
    public static class MutationConfig {
        private MutationType mutationType;
        private double chance;
        private List<MutationType> requiredMutations = new ArrayList<>();
    }
}