package gg.cristalix.growagarden.util;

import dev.xdark.clientapi.settings.KeyBinding;
import dev.xdark.clientapi.settings.SettingsManager;
import gg.cristalix.enginex.Enginex;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ModUtil {

  public KeyBinding registerBinding(String category, String description, int keyCode) {
    SettingsManager settingsManager = Enginex.getClientApi().settingsManager();
    KeyBinding keyBinding = settingsManager.getBindingByName(description);
    if (keyBinding == null) {
      keyBinding = KeyBinding.Builder.builder()
          .category(category)
          .keyCode(keyCode)
          .description(description)
          .build();
      settingsManager.registerBinding(keyBinding);
    }

    return keyBinding;
  }

  public KeyBinding getKeyBinding(String key) {
    return Enginex.getClientApi().settingsManager().getBindingByName(key);
  }
}
