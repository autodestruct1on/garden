package gg.cristalix.growagarden.model.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemCustomItem extends CustomItem {

  int amountUsage;

  public ItemCustomItem(String itemId) {
    super(itemId);
  }

  @Override
  public CustomItem load(CustomItemData customItemData) {
    super.load(customItemData);

    this.amountUsage = customItemData.getAmountUsage();

    return this;
  }
}
