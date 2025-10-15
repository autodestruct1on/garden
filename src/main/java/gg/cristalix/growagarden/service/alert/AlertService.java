package gg.cristalix.growagarden.service.alert;

import gg.cristalix.wada.Wada;
import gg.cristalix.wada.component.alert.IAlertManager;
import gg.cristalix.wada.component.alert.data.ToastMessage;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

@UtilityClass
public class AlertService {

  private final IAlertManager alertManager = Wada.get().getAlertManager();

  public void sendInfo(Player player, String message) {
    alertManager.sendToastMessage(
      ToastMessage.builder()
        .message(message)
        .duration(3.0)
        .enableProgressLine(true)
        .stackable(true)
        .build(),
      player
    );
  }

  public void sendSuccess(Player player, String message) {
    alertManager.sendToastMessage(
      ToastMessage.builder()
        .message("§a" + message)
        .duration(3.0)
        .enableProgressLine(true)
        .stackable(true)
        .build(),
      player
    );
  }

  public void sendError(Player player, String message) {
    alertManager.sendToastMessage(
      ToastMessage.builder()
        .message("§c" + message)
        .duration(4.0)
        .enableProgressLine(true)
        .stackable(true)
        .build(),
      player
    );
  }

  public void sendWarning(Player player, String message) {
    alertManager.sendToastMessage(
      ToastMessage.builder()
        .message("§e" + message)
        .duration(3.5)
        .enableProgressLine(true)
        .stackable(true)
        .build(),
      player
    );
  }

  public void sendTransaction(Player player, String message, double duration) {
    alertManager.sendToastMessage(
      ToastMessage.builder()
        .message("§6" + message)
        .duration(duration)
        .enableProgressLine(true)
        .stackable(true)
        .build(),
      player
    );
  }
}