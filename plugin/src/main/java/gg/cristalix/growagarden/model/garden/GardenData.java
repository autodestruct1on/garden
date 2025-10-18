package gg.cristalix.growagarden.model.garden;

import gg.cristalix.wada.math.V3;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GardenData {
  Map<String, CellData> plantedCells = new HashMap<>();

  public void addPlantedCell(V3 position, CellData cellData) {
    String key = positionToKey(position);
    plantedCells.put(key, cellData);
  }

  public CellData getCellAtPosition(V3 position) {
    String key = positionToKey(position);
    return plantedCells.get(key);
  }

  public void removeCellAtPosition(V3 position) {
    String key = positionToKey(position);
    plantedCells.remove(key);
  }

  public boolean hasPlantAtPosition(V3 position) {
    String key = positionToKey(position);
    return plantedCells.containsKey(key);
  }


  public boolean hasPlantInRadius(V3 position, double radius) {
    double radiusSquared = radius * radius;

    for (CellData cell : plantedCells.values()) {
      V3 cellPos = cell.getPoint();
      double distanceSquared = Math.pow(cellPos.getX() - position.getX(), 2) +
        Math.pow(cellPos.getZ() - position.getZ(), 2);

      if (distanceSquared <= radiusSquared) return true;
    }

    return false;
  }


  public Map<String, CellData> getAllPlantedCells() {
    return new HashMap<>(plantedCells);
  }

  private String positionToKey(V3 position) {
    return String.format("%.2f_%.2f_%.2f",
      position.getX(),
      position.getY(),
      position.getZ());
  }
}