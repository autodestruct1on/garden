package gg.cristalix.growagarden.model.garden.vegetation;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SeedData {
  String id;
  String name;
  String displayName;
  SeedCategory category;
  int stages;
  long growTimeMillis;
  boolean multiHarvest;
  double baseWeightKgMin;
  double baseWeightKgMax;
  double baseValue;
  double seedPrice;
}