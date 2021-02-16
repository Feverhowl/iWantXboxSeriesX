package org.deangould.iWantXboxSeriesX.telegram.commands.service;

import org.deangould.iWantXboxSeriesX.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Dmitrii Zolotarev
 * Команда "Помощь"
 */
public class HelpCommand extends ServiceCommand {
    private Logger logger = LoggerFactory.getLogger(HelpCommand.class);

    public HelpCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        logger.debug(String.format("Пользователь %s. Начато выполнение команды %s", userName,
                this.getCommandIdentifier()));
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                "Я бот, который поможет Вам своевременно (в течение минуты) узнать о наличии Xbox Series X для " +
                        "возможности сделать заказ в популярных сетевых магазинах электроники в России.\n" +
                        "Станьте первым, кто узнает о наличии приставки!\n\n" +
                        "Как только желанная приставка появится в наличии на продажу - Вам поступит соответствующее " +
                        "сообщение, содержащее также ссылку на страницу товара в том или ином онлайн-магазине.\n\n" +
                        "❗*Список команд*\n/settings - просмотреть текущие настройки\n/help - помощь\n" +
                        "/list - посмотреть список городов, доступных для отслеживания\n\n" +
                        "Желаю удачи\uD83D\uDE42");
        logger.debug(String.format("Пользователь %s. Завершено выполнение команды %s", userName,
                this.getCommandIdentifier()));
    }
}
