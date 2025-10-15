package gg.cristalix.growagarden.model.garden.vegetation;

import lombok.Data;

@Data
public class Crop {
    private String seedId;                       // ID исходного семени
    private double weightKg;                     // Вес урожая в килограммах
    private int amount;                          // Количество единиц урожая (для multiHarvest)
}
