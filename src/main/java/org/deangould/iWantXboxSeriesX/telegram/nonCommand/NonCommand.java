package org.deangould.iWantXboxSeriesX.telegram.nonCommand;

import org.apache.commons.io.IOUtils;
import org.deangould.iWantXboxSeriesX.exceptions.IllegalSettingsException;
import org.deangould.iWantXboxSeriesX.telegram.Bot;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Dmitrii Zolotarev
 * Обработка сообщения, не являющегося командой (т.е. обычного текста не начинающегося с "/")
 */
public class NonCommand {
    private Logger logger = LoggerFactory.getLogger(NonCommand.class);

    public String nonCommandExecute(Long chatId, String userName, String text) {
        logger.debug(String.format("Пользователь %s. Начата обработка сообщения \"%s\", не являющегося " +
                "зарегестрированной командой", userName, text));

        Settings settings;
        String answer;

        try {
            logger.debug(String.format("Пользователь %s. Пробуем создать объект настроек из сообщения \"%s\"",
                    userName, text));
            settings = createSettings(text);
            saveUserSettings(chatId, settings);
            logger.debug(String.format("Пользователь %s. Объект настроек из сообщения \"%s\" создан и сохранён",
                    userName, text));
            answer = "Настройки обновлены. Вы всегда можете их посмотреть с помощью /settings";
        } catch (IllegalSettingsException e) {
            logger.debug(String.format("Пользователь %s. Не удалось создать объект настроек из сообщения \"%s\". " +
                    "%s", userName, text, e.getMessage()));
            answer = e.getMessage() +
                    "\n\n❗ Настройки не были изменены. Вы всегда можете их посмотреть с помощью /settings";
        } catch (Exception e) {
            logger.debug(String.format("Пользователь %s. Не удалось создать объект настроек из сообщения \"%s\". " +
                    "%s. %s", userName, text, e.getClass().getSimpleName(), e.getMessage()));
            answer = "Простите, я не понимаю Вас. Похоже, что Вы ввели сообщение, не соответствующее формату, или " +
                    "такого города не существует.\n\n" +
                    "Возможно, Вам поможет /help";
        }

        logger.debug(String.format("Пользователь %s. Завершена обработка сообщения \"%s\", не являющегося командой",
                userName, text));
        return answer;
    }

    /**
     * Создание настроек из полученного пользователем сообщения
     * @param text текст сообщения
     * @throws IllegalArgumentException пробрасывается, если сообщение пользователя не соответствует формату
     */
    private Settings createSettings(String text)
            throws IOException, ParseException, IllegalArgumentException {
        //отсекаем файлы, стикеры, гифки и прочий мусор
        if (text == null) {
            throw new IllegalArgumentException("Сообщение не является текстом");
        }
        validateSettings(text);
        return new Settings(text);
    }

    /**
     * Валидация настроек
     */
    private void validateSettings(String text) throws IOException, ParseException{
        JSONObject jsonObj = getJsonObject(Settings.dataFile);
        JSONArray array = (JSONArray) jsonObj.get("city");
        for (Object element: array) {
            JSONObject innerObject = (JSONObject) element;
            if (text.equals(innerObject.get("name"))) return;
        }
        throw new IllegalSettingsException("\uD83D\uDCA9 Указанный город отсутствует в списке отслеживаемых.");
    }

    public JSONObject getJsonObject(String jsonFilePath) throws IOException, ParseException {
        logger.debug("Получение JSON-объекта");
        JSONObject jsonObject = null;
        try {
            if (jsonFilePath == null || jsonFilePath.isEmpty())
                throw new FileNotFoundException("Путь к файлу JSON не задан");
            JSONParser parser = new JSONParser();
            jsonObject = (JSONObject) parser.parse(IOUtils.toString(NonCommand.class.getResourceAsStream(jsonFilePath),
                    StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            logger.debug(String.format("JSON-файл не был найден - %s - %s", e.getClass().getName(), e.getMessage()));
            e.printStackTrace();
        } catch (IOException | ParseException e) {
            logger.debug(String.format("Ошибка парсинга JSON из файла - %s", jsonFilePath));
            throw e;
        }
        return jsonObject;
    }

    /**
     * Добавление настроек пользователя в мапу, чтобы потом их использовать для этого пользователя при генерации файла
     * Если настройки совпадают с дефолтными, они не сохраняются, чтобы впустую не раздувать мапу
     * @param chatId id чата
     * @param settings настройки
     */
    private void saveUserSettings(Long chatId, Settings settings) {
        if (!settings.equals(Bot.getDefaultSettings())) {
            Bot.getUserSettings().put(chatId, settings);
        } else {
            Bot.getUserSettings().remove(chatId);
        }
    }

}
