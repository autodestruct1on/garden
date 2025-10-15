package gg.cristalix.growagarden.other.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;


@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseCancellableEvent extends BaseEvent implements Cancellable {

  boolean cancelled;

  public boolean callEvent() {
    Bukkit.getPluginManager().callEvent(this);
    return !cancelled;
  }
}
