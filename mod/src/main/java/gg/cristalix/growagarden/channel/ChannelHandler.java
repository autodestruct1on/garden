package gg.cristalix.growagarden.channel;

import dev.xdark.clientapi.event.network.PluginMessage;
import gg.cristalix.enginex.Enginex;
import gg.cristalix.enginex.transfer.ModTransfer;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChannelHandler {

  private static final String CHANNEL = "gag:";

  Map<String, Consumer<ModTransfer>> listeners = new HashMap<>();

  public ChannelHandler() {
    Enginex.registerEvent(PluginMessage.class, event -> {
      String channelEvent = event.getChannel();
      Consumer<ModTransfer> consumer = listeners.get(channelEvent);
      if (consumer != null) {
        consumer.accept(new ModTransfer(event.getData().retainedSlice()));
      }
    }, 1);
  }

  public void register(String subChannel, Consumer<ModTransfer> consumer) {
    listeners.put(CHANNEL + subChannel, consumer);
  }

  public static void send(String subChannel, ModTransfer transfer) {
    transfer.send(CHANNEL + subChannel);
  }

  public static void send(String subChannel, Consumer<ModTransfer> t) {
    send(subChannel, new ModTransfer().modify(t));
  }

  public static void send(String subChannel) {
    new ModTransfer().send(CHANNEL + subChannel);
  }
}
