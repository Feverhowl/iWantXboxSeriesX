package org.deangould.iWantXboxSeriesX.telegram.nonCommand;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author Dmitrii Zolotarev
 * Пользовательские настройки
 */
@Getter
@EqualsAndHashCode
public class Settings {

    public static final String dataFile = "/data.json";

    /**
     * Город, в котором идёт мониторинг наличия товара в магазинах
     */
    private String city;

    public Settings(String city) {
        this.city = city;
    }
}
