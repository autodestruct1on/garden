package gg.cristalix.growagarden.manager;

import gg.cristalix.growagarden.common.mod.inventory.ItemEnum;
import gg.cristalix.growagarden.model.garden.vegetation.MutationType;
import gg.cristalix.growagarden.model.garden.vegetation.SeedData;
import gg.cristalix.growagarden.model.item.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemManager {

    @Getter
    Map<String, CustomItemData> customItemDataMap;

    @Getter
    Map<String, SeedData> seedDataMap;

    public ItemManager() {
        this.customItemDataMap = new HashMap<>();
        this.seedDataMap = new HashMap<>();
    }

    public void registerItemData(String itemId, CustomItemData itemData) {
        customItemDataMap.put(itemId, itemData);
    }

    public void registerAllItemData(Map<String, CustomItemData> itemDataMap) {
        customItemDataMap.putAll(itemDataMap);
    }

    public void registerAllSeeds(Map<String, SeedData> seeds) {
        seedDataMap.putAll(seeds);
    }

    @Nullable
    public CustomItem createItem(String itemId) {
        CustomItemData customItemData = getCustomItemData(itemId);
        if (customItemData == null) return null;

        return switch (customItemData.getItemEnum()) {
            case CROP -> createItem(customItemData, new CropCustomItem(itemId));
            case ITEM -> createItem(customItemData, new ItemCustomItem(itemId));
            case SEED -> createItem(customItemData, new SeedCustomItem(itemId));
            default -> null;
        };
    }

    @Nullable
    private CustomItemData getCustomItemData(String itemId) {
        return customItemDataMap.get(itemId);
    }

    public void loadItem(CustomItem customItem) {
        CustomItemData customItemData = getCustomItemData(customItem.getItemId());
        if (customItemData != null) {
            customItem.load(customItemData);
        }
    }

    private CustomItem createItem(CustomItemData itemData, CustomItem customItem) {
        return customItem.load(itemData);
    }

    public boolean hasItemData(String itemId) {
        return customItemDataMap.containsKey(itemId);
    }

    @Nullable
    public CustomItemData getItemData(String itemId) {
        return customItemDataMap.get(itemId);
    }

    @Nullable
    public SeedData getSeedData(String seedId) {
        return seedDataMap.get(seedId);
    }

    @Nullable
    public SeedData getSeedDataByName(String seedName) {
        return seedDataMap.values().stream()
                .filter(seedData -> seedData.getName().equals(seedName))
                .findFirst()
                .orElse(null);
    }

    public void createSeedItemData() {
        for (Map.Entry<String, SeedData> entry : seedDataMap.entrySet()) {
            String seedId = entry.getKey();
            SeedData seedData = entry.getValue();

            Map<String, String> nbt = new HashMap<>();
            nbt.put("seed_id", seedId);

            CustomItemData seedItemData = new CustomItemData(
                    seedId,
                    seedData.getDisplayName(),
                    Material.CLAY_BALL.name(),
                    ItemEnum.SEED,
                    nbt,
                    seedData.getName(),
                    0
            );

            registerItemData(seedId, seedItemData);
        }
    }

    public CropCustomItem createCrop(String seedId, double weight, Set<MutationType> mutations) {
        CropCustomItem crop = new CropCustomItem(seedId);
        CustomItemData data = new CustomItemData(
                seedId,
                "Crop",
                Material.CLAY_BALL.name(),
                ItemEnum.CROP,
                new HashMap<>(),
                null,
                0
        );
        crop.load(data);
        crop.addInstance(weight, mutations);
        return crop;
    }
}