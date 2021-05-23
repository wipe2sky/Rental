package com.kurtsevich.rental.util;

public class MapUtil {
    private MapUtil() {
    }

    private static double rad(double d) {
        return d * Math.PI / 180.00; // Преобразовать угол в радианы
    }

    /*
     * Рассчитать расстояние между двумя точками на основе широты и долготы (в метрах)
     */
    public static double getDistanceInMeters(double longitude1, double latitude1, double longitude2, double latitude2) {

        double lat1 = rad (latitude1); // Широта

        double lat2 = rad(latitude2);

        double a = lat1-lat2; // Разница между двумя широтами

        double b = rad (longitude1) -rad (longitude2); // разница долготы

        double s = 2 * Math.asin(Math

                .sqrt (Math.pow (Math.sin (a / 2), 2) + Math.cos (lat1) * Math.cos (lat2) * Math.pow (Math.sin (b / 2), 2))) ; // Формула для расчета расстояния между двумя точками

        s = s * 6378137.0; // длина дуги, умноженная на радиус земли (радиус в метрах)

        s = Math.round (s * 10000d) / 10000d; // Точное значение расстояния
        // округлить до одного знака после запятой
        //DecimalFormat df = new DecimalFormat("#.0");

        return s;

    }
}
