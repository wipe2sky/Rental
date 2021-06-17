package com.kurtsevich.rental.util;

import org.springframework.stereotype.Component;

@Component
public class MapUtil {
    private MapUtil() {
    }

    private static double rad(double d) {
        return d * Math.PI / 180.00; // Преобразовать угол в радианы
    }

    public static long getDistanceInMeters(double longitude1, double latitude1, double longitude2, double latitude2) {

        double lat1 = rad (latitude1); // Широта

        double lat2 = rad(latitude2);

        double a = lat1-lat2; // Разница между двумя широтами

        double b = rad (longitude1) -rad (longitude2); // разница долготы

        double s = 2 * Math.asin(Math

                .sqrt (Math.pow (Math.sin (a / 2), 2) + Math.cos (lat1) * Math.cos (lat2) * Math.pow (Math.sin (b / 2), 2))) ; // Формула для расчета расстояния между двумя точками

        s = s * 6378137.0; // длина дуги, умноженная на радиус земли (радиус в метрах)

        return Math.round(s);

    }
}
