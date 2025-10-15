package gg.cristalix.growagarden.model.item;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeedCustomItem extends CustomItem {
  public SeedCustomItem(String itemId) {
    super(itemId);
  }
}
