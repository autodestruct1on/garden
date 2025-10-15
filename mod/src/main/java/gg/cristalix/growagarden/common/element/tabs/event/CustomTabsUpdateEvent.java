package gg.cristalix.growagarden.common.element.tabs.event;

import gg.cristalix.enginex.event.ElementEvent;
import gg.cristalix.growagarden.common.element.tabs.CustomTabs;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Событие вызывается при выборе новой категории в Tabs.
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomTabsUpdateEvent extends ElementEvent {

  final int index;
  @Setter
  boolean cancelled;

  public CustomTabsUpdateEvent(CustomTabs element, int index) {
    super(element);
    this.index = index;
  }
}