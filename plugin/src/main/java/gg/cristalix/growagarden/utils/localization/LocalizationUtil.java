package gg.cristalix.growagarden.utils.localization;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.entity.Player;
import ru.cristalix.core.i18n.I18n;

/**
 * Класс для работы с локализируемыми строками.
 * Рекомендуется работа с любыми сообщениями для игроков только через него, предварительно внеся в LocalizationString
 */
@UtilityClass
public class LocalizationUtil {

    /**
     * Получает готовое локализированное сообщение.
     * */
    public String getLocalizedMessage(LocalizationString string, Object... args) {
        return args.length > 0 ? I18n.of(string.getI18NString(), args) : string.getI18NString();
    }

    /**
    * Отправляет локализированное сообщение игроку.
    * */
    public void sendLocalizedMessage(Player player, LocalizationString string, Object... args) {
        if (player == null || !player.isOnline()) return;

        player.sendMessage(new TranslatableComponent(getLocalizedMessage(string, args)));
    }
}
