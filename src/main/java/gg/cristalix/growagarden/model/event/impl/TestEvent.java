package gg.cristalix.growagarden.model.event.impl;

import gg.cristalix.growagarden.model.event.EventData;
import gg.cristalix.growagarden.model.event.IGameEvent;
import gg.cristalix.growagarden.utils.localization.LocalizationString;
import gg.cristalix.growagarden.utils.localization.LocalizationUtil;
import lombok.Getter;
import org.bukkit.Bukkit;

public class TestEvent implements IGameEvent {

    @Getter
    private EventData data;

    @Override
    public void onStart() {
        Bukkit.getLogger().info(LocalizationUtil.getLocalizedMessage(LocalizationString.EVENT_STARTED));
    }

    @Override
    public void onCancel() {

    }
}
