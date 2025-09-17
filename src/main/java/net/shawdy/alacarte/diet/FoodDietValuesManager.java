package net.shawdy.alacarte.diet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.shawdy.alacarte.ALaCarte;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FoodDietValuesManager {
    private static final Map<ResourceLocation, DietValuesHolder> DIET_VALUES = new HashMap<>();

    public static void load(ResourceManager manager) {
        DIET_VALUES.clear();
        try {
            Optional<Resource> optionalResource = manager.getResource(new ResourceLocation(ALaCarte.MOD_ID, "food_diet_values.json"));

            if(optionalResource.isEmpty()) {
                LogManager.getLogger().warn("Diet values are not present!");
                return;
            }
            try (Reader reader = optionalResource.get().openAsReader()) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

                for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                    ResourceLocation itemId = new ResourceLocation(entry.getKey());
                    DietValuesHolder data = parseDietValues(entry.getValue().getAsJsonObject());
                    DIET_VALUES.put(itemId, data);
                }
            }
        } catch (IOException e) {
            LogManager.getLogger().error("Failed to load food data values!", e);
        }
    }

    private static DietValuesHolder parseDietValues(JsonObject json) {
        DietValuesHolder data = new DietValuesHolder();
        data.setProteins(json.has("proteins") ? json.get("proteins").getAsFloat() : 0);
        data.setFats(json.has("fats") ? json.get("fats").getAsFloat() : 0);
        data.setCarbohydrates(json.has("carbohydrates") ? json.get("carbohydrates").getAsFloat() : 0);
        data.setFiber(json.has("fiber") ? json.get("fiber").getAsFloat() : 0);
        data.setMinerals(json.has("minerals") ? json.get("minerals").getAsFloat() : 0);
        data.setVitamins(json .has("vitamins") ? json.get("vitamins").getAsFloat() : 0);
        data.setWater(json.has("water") ? json.get("water").getAsFloat() : 0);
        return data;
    }

    public static DietValuesHolder getData(ResourceLocation itemId) {
        return DIET_VALUES.get(itemId);
    }
}