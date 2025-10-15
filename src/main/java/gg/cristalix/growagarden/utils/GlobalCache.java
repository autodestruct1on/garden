package gg.cristalix.growagarden.utils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GlobalCache {

  int UPDATE_DELAY = 500;

  Map<UUID, Map<String, Long>> lastUpdateTimes = new ConcurrentHashMap<>();

  public boolean checkAndUpdate(UUID uuid, String timeKey) {
    long time = Instant.now().toEpochMilli();

    Map<String, Long> cache = lastUpdateTimes.computeIfAbsent(uuid, map -> new HashMap<>());
    if (time - cache.computeIfAbsent(timeKey, it -> 0L) <= UPDATE_DELAY) return true;

    cache.put(timeKey, time);
    return false;
  }

  public boolean checkAndUpdate(UUID uuid, String timeKey, long delay) {
    long time = Instant.now().toEpochMilli();

    Map<String, Long> cache = lastUpdateTimes.computeIfAbsent(uuid, map -> new HashMap<>());
    if (time - cache.computeIfAbsent(timeKey, it -> 0L) <= delay) return true;

    cache.put(timeKey, time);
    return false;
  }

  public void clear(UUID uuid) {
    lastUpdateTimes.remove(uuid);
  }

}