package gg.cristalix.growagarden.common.util;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

@UtilityClass
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RandomUtil {

  SecureRandom secureRandom = new SecureRandom();

  public int getRandomInt(int max) {
    return getRandomInt(0, max);
  }

  public int getRandomInt(SecureRandom random, int max) {
    return getRandomInt(random, 0, max);
  }

  public int getRandomInt(int min, int max) {
    return secureRandom.nextInt(max - min + 1) + min;
  }

  public int getRandomInt(SecureRandom random, int min, int max) {
    return random.nextInt(max - min + 1) + min;
  }

  public double getRandomDouble(double min, double max) {
    return secureRandom.nextDouble() * (max - min) + min;
  }

  public double getRandomDouble(SecureRandom random, double min, double max, int decimalPlaces) {
    BigDecimal randomBigDecimal = BigDecimal.valueOf(random.nextDouble());
    BigDecimal range = BigDecimal.valueOf(max).subtract(BigDecimal.valueOf(min));
    BigDecimal scaledValue = randomBigDecimal.multiply(range).add(BigDecimal.valueOf(min));

    return scaledValue.setScale(decimalPlaces, RoundingMode.HALF_UP).doubleValue();

//    double randomValue = random.nextDouble() * (max - min) + min;
//    return (double) Math.round(randomValue * (10 * round)) / (10 * round);
  }

  public boolean getRandom(int i) {
    return secureRandom.nextInt(100) < i;
  }

  public static SecureRandom getRandom() {
    return secureRandom;
  }

  public int getRandomInt() {
    return secureRandom.nextInt(100000 - 100) + 100;
  }

  public byte[] longToBytes(long value) {
    return new byte[]{
      (byte) (value >>> 56),
      (byte) (value >>> 48),
      (byte) (value >>> 40),
      (byte) (value >>> 32),
      (byte) (value >>> 24),
      (byte) (value >>> 16),
      (byte) (value >>> 8),
      (byte) value
    };
  }


}
