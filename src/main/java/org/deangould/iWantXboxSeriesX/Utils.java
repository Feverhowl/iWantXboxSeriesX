package org.deangould.iWantXboxSeriesX;

import org.apache.commons.io.IOUtils;
import org.deangould.iWantXboxSeriesX.objects.City;
import org.deangould.iWantXboxSeriesX.telegram.nonCommand.Settings;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitrii Zolotarev
 */
public class Utils {

    private static Logger logger = LoggerFactory.getLogger(Utils.class);

    public static List<City> cities = getCityObjectsFromJson(Settings.dataFile);

    /**
     * Формирование имени пользователя
     * @param msg сообщение
     */
    public static String getUserName(Message msg) {
        return getUserName(msg.getFrom());
    }

    /**
     * Формирование имени пользователя. Если заполнен никнейм, используем его. Если нет - используем фамилию и имя
     * @param user пользователь
     */
    public static String getUserName(User user) {
        return (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
    }


    /**
     * Формирование объектов городов.
     * @param jsonFilePath путь к JSON-файлу, содержащему данные о городах
     */
    private static List<City> getCityObjectsFromJson(String jsonFilePath) {
        List<City> result = new ArrayList<>();
        JSONObject jsonObj = getJsonObject(jsonFilePath);
        JSONArray array = (JSONArray) jsonObj.get("city");
        for (Object element: array) {
            result.add((City)element);
        }
        return result;
    }

    private static JSONObject getJsonObject(String jsonFilePath) {
        logger.debug("Получение JSON-объекта");
        JSONObject jsonObject = null;
        try {
            if (jsonFilePath == null || jsonFilePath.isEmpty())
                throw new FileNotFoundException("Путь к файлу JSON не задан");
            JSONParser parser = new JSONParser();
            jsonObject = (JSONObject) parser.parse(IOUtils.toString(Utils.class.getResourceAsStream(jsonFilePath),
                    StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            logger.debug(String.format("JSON-файл не был найден - %s - %s", e.getClass().getName(), e.getMessage()));
            e.printStackTrace();
        } catch (IOException | ParseException e) {
            logger.debug(String.format("Ошибка парсинга JSON из файла - %s", jsonFilePath));
            e.printStackTrace();
        }
        return jsonObject;
    }
}
