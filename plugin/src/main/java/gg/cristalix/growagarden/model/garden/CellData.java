package gg.cristalix.growagarden.model.garden;

import gg.cristalix.growagarden.model.garden.vegetation.SeedInstance;
import gg.cristalix.wada.math.V3;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CellData {
  V3 point;
  SeedInstance seedInstance;
}
