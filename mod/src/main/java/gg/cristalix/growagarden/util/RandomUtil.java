package gg.cristalix.growagarden.util;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RandomUtil {

  Random random = new Random();

  public int getRandomInt(int max) {
    return getRandomInt(0, max);
  }

  public int getRandomInt(int min, int max) {
    return random.nextInt(max - min + 1) + min;
  }

  public double getRandomDouble(double min, double max) {
    return random.nextDouble() * (max - min) + min;
  }

  public boolean getRandom(int i) {
    return random.nextInt(100) < i;
  }

  public Random getRandom() {
    return random;
  }

  public int getRandomInt() {
    return random.nextInt(100000 - 100) + 100;
  }

}
