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
    public static double computeDistance(double lat1, double lon1, double lat2, double lon2) {
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
     * @param u1 first user and used as reference
     * @param u2 second user
     * @return true if in range, else false
     */
    public boolean usersAreInRange(User u1, User u2){
        double[] bb = getBoundingBox(u1.getLat(), u1.getLon(), u1.getPrefDistance());
        boolean s = u2.getLat() > bb[0] &&
                u2.getLon() > bb[1] &&
                u2.getLat() < bb[2] &&
                u2.getLon() < bb[3];
        return s;
     }

    /**
     * Generates a bounding box the around a given point.
     * @param pLatitude latitude of the point
     * @param pLongitude longitude of the point
     * @param pDistanceInKm distance between the point and each corner
     * @return the bounding box as an array of points. [minLat, minLon, maxLat, maxLon]
     */
    public double[] getBoundingBox(final double pLatitude, final double pLongitude, final int pDistanceInKm) {

        final double[] boundingBox = new double[4];
        int pDistanceInMeters = pDistanceInKm * 1000;
        final double latRadian = Math.toRadians(pLatitude);

        final double degLatKm = 110.574235;
        final double degLongKm = 110.572833 * Math.cos(latRadian);
        final double deltaLat = pDistanceInMeters / 1000.0 / degLatKm;
        final double deltaLong = pDistanceInMeters / 1000.0 /
                degLongKm;

        final double minLat = pLatitude - deltaLat;
        final double minLong = pLongitude - deltaLong;
        final double maxLat = pLatitude + deltaLat;
        final double maxLong = pLongitude + deltaLong;

        boundingBox[0] = minLat;
        boundingBox[1] = minLong;
        boundingBox[2] = maxLat;
        boundingBox[3] = maxLong;

        return boundingBox;
    }
}
