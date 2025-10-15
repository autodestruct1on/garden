package gg.cristalix.growagarden.network.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlantSyncData {
    String seedId;
    int currentStage;
    double growthProgress;
    boolean isWatered;
    long remainingTimeMillis;
}