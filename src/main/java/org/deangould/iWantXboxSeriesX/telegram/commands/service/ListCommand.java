package org.deangould.iWantXboxSeriesX.telegram.commands.service;

import org.deangould.iWantXboxSeriesX.Utils;
import org.deangould.iWantXboxSeriesX.objects.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

/**
 * @author Dmitrii Zolotarev
 * Команда "Список"
 */
public class ListCommand extends ServiceCommand {
    private Logger logger = LoggerFactory.getLogger(SettingsCommand.class);

    public ListCommand (String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        logger.debug(String.format("Пользователь %s. Начато выполнение команды %s", userName,
                this.getCommandIdentifier()));

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                "*Города, доступные для отслеживания*\n" + getCityNames());

        logger.debug(String.format("Пользователь %s. Завершено выполнение команды %s", userName,
                this.getCommandIdentifier()));
    }

    private String getCityNames() {
        StringBuilder sb = new StringBuilder();
        List<City> cities = Utils.cities;
        for (City element: cities) {
            sb.append(element.getName());
            sb.append(", ");
        }
        sb.append("end");
        return sb.toString().replaceAll(", end", ".");
    }
}
