package gg.cristalix.growagarden.other.event;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


@Getter
public class BaseEvent extends Event {

  private static final HandlerList handlers = new HandlerList();

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

}
