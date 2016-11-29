package be.studyfindr.rest;

import be.studyfindr.entities.User;

/**
 * Created by anthony on 29/11/2016.
 */
public class Util {
    public static final double R_EARTH = 6372.8; // In kilometers

    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R_EARTH * c;
    }

    public boolean usersInRange(User u1, User u2, double maxDist){
        return haversine(u1.getLat(), u1.getLon(), u2.getLat(), u2.getLon()) < maxDist;
    }
}
