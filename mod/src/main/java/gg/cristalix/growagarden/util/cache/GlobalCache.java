package gg.cristalix.growagarden.util.cache;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GlobalCache {

  int UPDATE_DELAY = 250;

  Map<String, Long> lastUpdateTimes = new ConcurrentHashMap<>();

  public boolean checkAndUpdate(CacheEnum cacheEnum) {
    return checkAndUpdate(cacheEnum.name());
  }

  public boolean checkAndUpdate(CacheEnum cacheEnum, long delay) {
    return checkAndUpdate(cacheEnum.name(), delay);
  }

  public boolean checkAndUpdate(String timeKey) {
    long time = Instant.now().toEpochMilli();
    if (time - lastUpdateTimes.computeIfAbsent(timeKey, it -> 0L) <= UPDATE_DELAY) return true;

    lastUpdateTimes.put(timeKey, time);
    return false;
  }

  public boolean checkAndUpdate(String timeKey, long delay) {
    long time = Instant.now().toEpochMilli();
    if (time - lastUpdateTimes.computeIfAbsent(timeKey, it -> 0L) <= delay) return true;

    lastUpdateTimes.put(timeKey, time);
    return false;
  }

  public boolean check(String timeKey, long delay) {
    long time = Instant.now().toEpochMilli();
    return time - lastUpdateTimes.computeIfAbsent(timeKey, it -> 0L) <= delay;
  }

  public boolean check(String timeKey) {
    return check(timeKey, UPDATE_DELAY);
  }
}
