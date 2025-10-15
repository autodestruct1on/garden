package gg.cristalix.growagarden.utils.gson;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;

public class LocationSerializer implements JsonSerializer<Location>, JsonDeserializer<Location> {

    @Override
    public Location deserialize(JsonElement jsonString, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject json = jsonString.getAsJsonObject();

        String worldName = json.get("world").getAsString();
        World world = Bukkit.getWorld(worldName);

        double x = json.get("x").getAsDouble();
        double y = json.get("y").getAsDouble();
        double z = json.get("z").getAsDouble();

        float yaw = json.has("yaw") ? json.get("yaw").getAsFloat() : 0.0f;
        float pitch = json.has("pitch") ? json.get("pitch").getAsFloat() : 0.0f;

        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("world", location.getWorld().getName());
        obj.addProperty("x", location.getX());
        obj.addProperty("y", location.getY());
        obj.addProperty("z", location.getZ());
        obj.addProperty("yaw", location.getYaw());
        obj.addProperty("pitch", location.getPitch());
        return obj;
    }
}
