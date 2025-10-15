package gg.cristalix.growagarden.model.item;

import gg.cristalix.growagarden.model.garden.vegetation.MutationType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CropCustomItem extends CustomItem {

  List<CropInstance> instances = new ArrayList<>();

  public CropCustomItem(String itemId) {
    super(itemId);
  }

  @Override
  public CustomItem load(CustomItemData customItemData) {
    return super.load(customItemData);
  }

  public void addInstance(double weight, Set<MutationType> mutations) {
    CropInstance instance = new CropInstance(weight, mutations);
    instances.add(instance);
    this.amount = instances.size();
  }

  public CropInstance removeInstance() {
    if (instances.isEmpty()) return null;
    CropInstance removed = instances.remove(instances.size() - 1);
    this.amount = instances.size();
    return removed;
  }

  public double getTotalWeight() {
    return instances.stream().mapToDouble(CropInstance::getWeight).sum();
  }

  public double getAveragePriceMultiplier() {
    if (instances.isEmpty()) return 1.0;
    return instances.stream()
            .mapToDouble(CropInstance::getTotalPriceMultiplier)
            .average()
            .orElse(1.0);
  }

  @Override
  protected ItemStack setLore(ItemStack itemStack) {
    ItemMeta meta = itemStack.getItemMeta();
    List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

    lore.add("§7Количество: §f" + instances.size());
    lore.add("§7Общий вес: §f" + String.format("%.2f", getTotalWeight()) + " кг");

    if (!instances.isEmpty()) {
      Set<MutationType> allMutations = new HashSet<>();
      for (CropInstance instance : instances) {
        allMutations.addAll(instance.getMutations());
      }

      if (!allMutations.isEmpty()) {
        lore.add("");
        lore.add("§6Мутации:");
        for (MutationType mutation : allMutations) {
          lore.add(mutation.getColorCode() + "  • " + mutation.getDisplayName() +
                  " §7(×" + String.format("%.1f", mutation.getPriceMultiplier()) + ")");
        }
      }
    }

    meta.setLore(lore);
    itemStack.setItemMeta(meta);
    return itemStack;
  }

  @Getter
  @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
  public static class CropInstance {
    double weight;
    Set<MutationType> mutations;

    public CropInstance(double weight, Set<MutationType> mutations) {
      this.weight = weight;
      this.mutations = mutations != null ? new HashSet<>(mutations) : new HashSet<>();
    }

    public double getTotalPriceMultiplier() {
      if (mutations.isEmpty()) {
        return 1.0;
      }

      double multiplier = 1.0;
      for (MutationType mutation : mutations) {
        multiplier *= mutation.getPriceMultiplier();
      }
      return multiplier;
    }
  }
}