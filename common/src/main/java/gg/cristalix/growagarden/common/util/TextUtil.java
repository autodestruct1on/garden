package gg.cristalix.growagarden.common.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class TextUtil {

  private final String[] keyRoman = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
  private final int[] valRoman = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
  private final NavigableMap<Double, String> suffixes = new TreeMap<>();

  static {
    suffixes.put(1.00E+03, "K");
    suffixes.put(1.00E+06, "M");
    suffixes.put(1.00E+09, "B");
    suffixes.put(1.00E+12, "T");
    suffixes.put(1.00E+15, "Qa");
    suffixes.put(1.00E+18, "Qi");
    suffixes.put(1.00E+21, "Sx");
    suffixes.put(1.00E+24, "Sp");
    suffixes.put(1.00E+27, "Oc");
    suffixes.put(1.00E+30, "No");
    suffixes.put(1.00E+33, "Dc");
    suffixes.put(1.00E+36, "Ud");
    suffixes.put(1.00E+39, "Dd");
    suffixes.put(1.00E+42, "Td");
    suffixes.put(1.00E+45, "Qad");
    suffixes.put(1.00E+48, "Qid");
    suffixes.put(1.00E+51, "Sxd");
    suffixes.put(1.00E+54, "Spd");
    suffixes.put(1.00E+57, "Ocd");
    suffixes.put(1.00E+60, "Nod");
    suffixes.put(1.00E+63, "Vg");
    suffixes.put(1.00E+66, "Uvg");
    suffixes.put(1.00E+69, "Dvg");
    suffixes.put(1.00E+72, "Tvg");
    suffixes.put(1.00E+75, "Qavg");
    suffixes.put(1.00E+78, "Qivg");
    suffixes.put(1.00E+81, "Sxvg");
    suffixes.put(1.00E+84, "Spvg");
    suffixes.put(1.00E+87, "Ocvg");
    suffixes.put(1.00E+90, "Nov");
    suffixes.put(1.00E+93, "Tt");
    suffixes.put(1.00E+96, "Unt");
    suffixes.put(1.00E+99, "Dtt");
    suffixes.put(1.00E+102, "Ttt");
    suffixes.put(1.00E+105, "Qut");
    suffixes.put(1.00E+108, "Qat");
    suffixes.put(1.00E+111, "Sst");
    suffixes.put(1.00E+114, "Spt");
    suffixes.put(1.00E+117, "Oct");
    suffixes.put(1.00E+120, "Nvt");
    suffixes.put(1.00E+123, "Qdt");
    suffixes.put(1.00E+126, "Qqt");
    suffixes.put(1.00E+129, "Sqt");
    suffixes.put(1.00E+132, "Sgt");
    suffixes.put(1.00E+135, "Oi");
    suffixes.put(1.00E+138, "Noi");
    suffixes.put(1.00E+141, "C");
    suffixes.put(1.00E+147, "Vgn");
    suffixes.put(1.00E+150, "Uvgn");
    suffixes.put(1.00E+153, "Dvgn");
    suffixes.put(1.00E+156, "Trvg");
    suffixes.put(1.00E+159, "Qnvn");
    suffixes.put(1.00E+162, "Sxvn");
    suffixes.put(1.00E+165, "Spvn");
    suffixes.put(1.00E+168, "Ocvn");
    suffixes.put(1.00E+171, "Nvgn");
    suffixes.put(1.00E+174, "Idk");
  }

  public String parseNumber(double value, int round) {
    if (value < 1000) return getNumber(value, round);
    Map.Entry<Double, String> e = suffixes.floorEntry(value);
    double amount = round(value / (e.getKey() / 100));
    return getNumber(amount / 100D, round) + e.getValue();
  }

  private String getNumber(double value, int round) {
    if (round < 0) round = 0;

    double pow = Math.pow(10, round);
    double roundedValue = Math.floor(value * pow) / pow;
    String format = String.format(Locale.US, "%." + round + "f", roundedValue);

    if (round == 0) return format;
    return format.replaceAll("\\.?0+$", "");
  }

  public double getPercent(double currentNumber, double maxNumber) {
    return (currentNumber * 100D) / maxNumber;
  }

  public static String parseNumberChance(double value) {
    if (value == 0) return "0";
    if (value >= 0.001) return getNumber(value, 4);

    value = (double) (Math.round(value * 10000000)) / 10000000;
    int scaleFactor = 1;

    while (value * scaleFactor < 1) {
      scaleFactor *= 10;
    }

    while ((value * scaleFactor) % 1 != 0) {
      scaleFactor *= 10;
    }

    double num = value * scaleFactor;
    return (int) num + "/" + getNumber(scaleFactor, 2);
  }

  public String intToRoman(long num) {
    StringBuilder ret = new StringBuilder();
    int ind = 0;
    while (ind < keyRoman.length) {
      while (num >= valRoman[ind]) {
        long d = num / valRoman[ind];
        num = num % valRoman[ind];
        for (int i = 0; i < d; i++)
          ret.append(keyRoman[ind]);
      }
      ind++;
    }
    return ret.toString();
  }

  public double round(double amount) {
    return Math.floor(amount);
  }

  public long getTime(long repetitionTime, TimeUnit timeUnit) {
    long millis = timeUnit.toMillis(repetitionTime);
    return getTimeAsMillis(millis);
  }

  public long getTimeAsMillis(long repetitionTime) {
    long time = Instant.now().toEpochMilli() % repetitionTime;
    return repetitionTime - time;
  }

  public String woc(String input) {
    if (input == null) return null;
    return input.replaceAll("(§|&).|(¨.{6})", "");
  }

  public String removeNewLine(String input) {
    if (input == null) return null;
    return input.replaceAll("\n", "");
  }

  public String formatTime(long elapsedMillis) {
    long hours = TimeUnit.MILLISECONDS.toHours(elapsedMillis);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedMillis) % 60;
    long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) % 60;

    return String.format("%02d:%02d:%02d", hours, minutes, seconds);
  }

  public String formatSeconds(int totalSec) {
    return formatSeconds((long) totalSec);
  }

  public static String formatSeconds(long totalSec) {
    if (totalSec <= 0) return "0с";
    int days = (int) Math.floor(totalSec / 86400f);
    int hours = (int) Math.floor((totalSec - 86400f * days) / 3600f);
    int mins = (int) Math.floor((totalSec - 86400f * days - 3600 * hours) / 60f);
    int secs = (int) (totalSec % 60);

    StringBuilder sb = new StringBuilder();

    if (days > 0) sb.append(days).append("д ");
    if (hours > 0) sb.append(hours).append("ч ");
    if (mins > 0) sb.append(mins).append("м ");
    if (secs > 0) sb.append(secs).append("с");

    return sb.toString().trim();
  }

  public static double roundUp(double number, int decimals) {
    if (decimals < 0) decimals = 0;
    BigDecimal bd = BigDecimal.valueOf(number);
    bd = bd.setScale(decimals, RoundingMode.CEILING);
    return bd.doubleValue();
  }

  public long getElapsedTime(long finishTime) {
    long currentTime = Instant.now().toEpochMilli();
    return finishTime - currentTime;
  }
}

