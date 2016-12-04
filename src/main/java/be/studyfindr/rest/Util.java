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
     * @param u1 first user
     * @param u2 second user
     * @return true if in range, else false
     */
    public boolean usersAreInRange(User u1, User u2){
        // bounding box
        /*double[] topLeft = computeOffset(u1.getLat(), u1.getLon(), u1.getPrefDistance(), 315.0);
        double[] botLeft = computeOffset(u1.getLat(), u1.getLon(), u1.getPrefDistance(), 225.0);
        double[] botRight = computeOffset(u1.getLat(), u1.getLon(), u1.getPrefDistance(), 135.0);
        double[] topRight = computeOffset(u1.getLat(), u1.getLon(), u1.getPrefDistance(), 45.0);
        return u2.getLat() < topLeft[0] &&
                u2.getLon() > topLeft[1] &&
                u2.getLat() > botRight[0] &&
                u2.getLon() < botRight[1];*/
        double[] bb = getBoundingBox(u1.getLat(), u1.getLon(), u1.getPrefDistance());
        return u2.getLat() > bb[0] &&
                u2.getLon() > bb[1] &&
                u2.getLat() < bb[2] &&
                u2.getLon() < bb[3];
        // the slow method
        //return computeDistance(u1.getLat(), u1.getLon(), u2.getLat(), u2.getLon()) < maxDist;
    }

    public static double[] computeOffset(double lat, double lon, double distance, double heading) {
        distance /= (R_EARTH * 1000);
        heading = Math.toRadians(heading);
        // http://williams.best.vwh.net/avform.htm#LL
        double fromLat = lat;
        double fromLng = lon;
        double cosDistance = Math.cos(distance);
        double sinDistance = Math.sin(distance);
        double sinFromLat = Math.sin(fromLat);
        double cosFromLat = Math.cos(fromLat);
        double sinLat = cosDistance * sinFromLat + sinDistance * cosFromLat * Math.cos(heading);
        double dLng = Math.atan2(
                sinDistance * cosFromLat * Math.sin(heading),
                cosDistance - sinFromLat * sinLat);
        return new double[] {Math.asin(sinLat), fromLng + dLng};
        //return new LatLng(toDegrees(asin(sinLat)), toDegrees(fromLng + dLng));
    }

    private double[] getBoundingBox(final double pLatitude, final double pLongitude, final int pDistanceInKm) {

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
