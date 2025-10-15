package gg.cristalix.growagarden.model.garden;

import gg.cristalix.growagarden.model.garden.vegetation.SeedInstance;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.cristalix.core.math.V3;

import javax.annotation.Nullable;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CellData {
  final V3 point;

  @Setter
  @Nullable
  SeedInstance seedInstance;
}
