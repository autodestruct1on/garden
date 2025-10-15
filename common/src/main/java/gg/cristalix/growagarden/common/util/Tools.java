package gg.cristalix.growagarden.common.util;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public class Tools {

  public List<String> lineText(String text) {
    return Arrays.asList(text.split("\n"));
  }

  public String pluralRu(int count, String form1, String form2, String form5) {
    count = Math.abs(count) % 100;
    int count1 = count % 10;
    return count > 10 && count < 20 ? form5 : (count1 > 1 && count1 < 5 ? form2 : (count1 == 1 ? form1 : form5));
  }
}
