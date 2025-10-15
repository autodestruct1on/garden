package gg.cristalix.growagarden.common.util;


import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true)
public class ColorHex {
  public static final String S = "¨";
  public static final String ORANGE = S + "e07614";
  public static final String GREEN_LIGHT = S + "49df74";
  public static String GOLD = S + "ffca42";
  public static String GRAY = S + "a8a8a8";
  public static String PURPLE = S + "6826f5";
  public static String GRAY_BORDER = S + "bebebe";
  public static String WHITE = S + "ffffff";
  public static String GREEN = S + "49df74";
  public static String RED = S + "ff0000";
  public static String RED_LIGHT = S + "e73d4b";
  public static String BLUE = S + "4a8cec";
  public static String GRAY_CHAT = S + "555555";

  public static String getColor(String hex) {
    return S + hex.toLowerCase();
  }

  public static String clearColor(String input) {
    if (input == null) return "";
    return input.replaceAll("(§|&).|(¨.{6})", "");
  }
}