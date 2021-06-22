package com.kurtsevich.rental.util;

import org.springframework.stereotype.Component;

@Component
public class MapUtil {
    private MapUtil() {
    }

    private static double rad(double d) {
        return d * Math.PI / 180.00;
    }

    public static long getDistanceInMeters(double longitude1, double latitude1, double longitude2, double latitude2) {

        double lat1 = rad(latitude1);

        double lat2 = rad(latitude2);

        double a = lat1 - lat2;

        double b = rad(longitude1) - rad(longitude2);

        double s = 2 * Math.asin(Math

                .sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));

        s = s * 6378137.0;

        return Math.round(s);

    }
}
