package org.deangould.iWantXboxSeriesX.telegram.commands.service;

import org.deangould.iWantXboxSeriesX.Utils;
import org.deangould.iWantXboxSeriesX.telegram.Bot;
import org.deangould.iWantXboxSeriesX.telegram.nonCommand.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Dmitrii Zolotarev
 * Команда получения текущих настроек
 */
public class SettingsCommand extends ServiceCommand {
    private Logger logger = LoggerFactory.getLogger(SettingsCommand.class);

    public SettingsCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        logger.debug(String.format("Пользователь %s. Начато выполнение команды %s", userName,
                this.getCommandIdentifier()));

        Long chatId = chat.getId();
        Settings settings = Bot.getUserSettings(chatId);
        sendAnswer(absSender, chatId, this.getCommandIdentifier(), userName,
                String.format("*Текущие настройки*\n" + "- выбранный город - *%s*\n", settings.getCity()));

        logger.debug(String.format("Пользователь %s. Завершено выполнение команды %s", userName,
                this.getCommandIdentifier()));
    }
}
