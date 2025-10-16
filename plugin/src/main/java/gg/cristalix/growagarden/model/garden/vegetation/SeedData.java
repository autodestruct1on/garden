package gg.cristalix.growagarden.model.garden.vegetation;

import lombok.Data;

import java.util.UUID;

@Data
public class SeedData {
    private UUID uuid;
    private String id;
    private String name;
    private String displayName;
    private SeedCategory category;
    private int stages;
    private long growTimeMillis;
    private boolean multiHarvest;
    private double baseWeightKgMin;
    private double baseWeightKgMax;
    private double baseValue;
    private double seedPrice;
}