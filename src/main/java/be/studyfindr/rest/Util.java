package be.studyfindr.rest;

import be.studyfindr.entities.User;

/**
 * Util contains general helper methods.
 */
public class Util {

    /**
     * Earth radius
     */
    public static final double R_EARTH = 6372.8; // In kilometers

    /**
     * Calculates the distance between 2 points based on there lat/lon and returns the distance in Km.
     * @param lat1 latitude point1
     * @param lon1 longitude point1
     * @param lat2 latitude point2
     * @param lon2 longitude point2
     * @return distance between points in Km
     */
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R_EARTH * c;
    }

    /**
     * Checks if 2 users are in range.
     * @param u1 first user
     * @param u2 second user
     * @param maxDist Max. distance between users
     * @return true if in range, else false
     */
    public boolean usersInRange(User u1, User u2, double maxDist){
        return haversine(u1.getLat(), u1.getLon(), u2.getLat(), u2.getLon()) < maxDist;
    }
}
