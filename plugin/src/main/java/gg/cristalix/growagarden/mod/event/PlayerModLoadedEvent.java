package gg.cristalix.growagarden.mod.event;

import gg.cristalix.growagarden.other.event.BaseEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerModLoadedEvent extends BaseEvent {

  Player player;

}
