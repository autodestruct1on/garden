package gg.cristalix.growagarden.config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import gg.cristalix.growagarden.model.garden.vegetation.SeedData;
import gg.cristalix.growagarden.model.item.CustomItemData;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@UtilityClass
public class ConfigLoader {

    private static final String SEED_UUID_NAMESPACE = "growagarden-seeds";

    public Map<String, SeedData> loadSeeds(InputStream stream) {
        if (stream == null) {
            log.error("Seeds config stream is null");
            throw new RuntimeException("Seeds config not found");
        }

        try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            JsonObject seedsJson = json.getAsJsonObject("seeds");

            Type type = new TypeToken<Map<String, SeedData>>(){}.getType();
            Map<String, SeedData> seeds = gson.fromJson(seedsJson, type);

            if (seeds == null || seeds.isEmpty()) {
                throw new RuntimeException("No seeds loaded from config");
            }

            seeds.forEach((id, data) -> {
                data.setId(id);
                data.setUuid(generateUuid(id));
            });

            log.info("Successfully loaded {} seed definitions", seeds.size());
            return seeds;

        } catch (Exception e) {
            log.error("Failed to load seeds config", e);
            throw new RuntimeException("Critical error loading seed configuration", e);
        }
    }

    private UUID generateUuid(String seedId) {
        String fullName = SEED_UUID_NAMESPACE + ":" + seedId;
        return UUID.nameUUIDFromBytes(fullName.getBytes(StandardCharsets.UTF_8));
    }

    public Map<String, CustomItemData> loadItems(InputStream stream) {
        if (stream == null) {
            log.error("Items config stream is null");
            throw new RuntimeException("Items config not found");
        }

        try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            JsonObject itemsJson = json.getAsJsonObject("items");

            Map<String, CustomItemData> items = new HashMap<>();

            itemsJson.entrySet().forEach(entry -> {
                String itemId = entry.getKey();
                JsonObject itemJson = entry.getValue().getAsJsonObject();

                String displayName = itemJson.get("displayName").getAsString();
                String material = itemJson.get("material").getAsString();
                String itemEnum = itemJson.get("itemEnum").getAsString();
                int amountUsage = itemJson.has("amountUsage") ? itemJson.get("amountUsage").getAsInt() : 0;

                Map<String, String> nbt = new HashMap<>();
                if (itemJson.has("nbt")) {
                    itemJson.getAsJsonObject("nbt").entrySet().forEach(nbtEntry ->
                            nbt.put(nbtEntry.getKey(), nbtEntry.getValue().getAsString())
                    );
                }

                String lore = "";
                if (itemJson.has("lore")) {
                    if (itemJson.get("lore").isJsonArray()) {
                        JsonArray loreArray = itemJson.getAsJsonArray("lore");
                        StringBuilder loreBuilder = new StringBuilder();
                        for (int i = 0; i < loreArray.size(); i++) {
                            if (i > 0) {
                                loreBuilder.append("\n");
                            }
                            loreBuilder.append(loreArray.get(i).getAsString());
                        }
                        lore = loreBuilder.toString();
                    } else {
                        lore = itemJson.get("lore").getAsString();
                    }
                }

                CustomItemData itemData = new CustomItemData(
                        itemId,
                        displayName,
                        material,
                        getItemEnum(itemEnum),
                        nbt,
                        lore,
                        amountUsage
                );

                items.put(itemId, itemData);
            });

            log.info("Successfully loaded {} custom items", items.size());
            return items;

        } catch (Exception e) {
            log.error("Failed to load items config", e);
            throw new RuntimeException("Critical error loading items configuration", e);
        }
    }

    private gg.cristalix.growagarden.common.mod.inventory.ItemEnum getItemEnum(String value) {
        try {
            return gg.cristalix.growagarden.common.mod.inventory.ItemEnum.valueOf(value);
        } catch (IllegalArgumentException e) {
            log.warn("Unknown ItemEnum: {}, defaulting to ITEM", value);
            return gg.cristalix.growagarden.common.mod.inventory.ItemEnum.ITEM;
        }
    }
}