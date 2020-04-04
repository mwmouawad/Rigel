package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Represents a pair of Geographic Coordinates.
 * NB: Is an immutable class. So no setters.
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class GeographicCoordinates extends SphericalCoordinates {

    private static final RightOpenInterval LON_DEG_INTERVAL = RightOpenInterval.of(-180, 180);
    private static final ClosedInterval LAT_DEG_INTERVAL = ClosedInterval.symmetric(180);


    private GeographicCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }

    /**
     * Creates an instance of GeographicCoordinates Coordinates.
     *
     * @param lonDeg longitude input in degrees between [-180°,180°[
     * @param latDeg latitude input in degrees between [-90°, +90°]
     * @return Geographic Coordinates object.
     * @throws IllegalArgumentException if the inputs are not contained in the bounds specified above.
     */
    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
        double lonRad = Angle.ofDeg(Preconditions.checkInInterval(LON_DEG_INTERVAL, lonDeg));
        double latRad = Angle.ofDeg(Preconditions.checkInInterval(LAT_DEG_INTERVAL, latDeg));
        return new GeographicCoordinates(lonRad, latRad);
    }

    /**
     * Check if given longitude coordinates
     * is valid according to the geographic coordinates system.
     *
     * @param lonDeg longitude input in degrees.
     * @return true if valid
     */
    public static boolean isValidLonDeg(double lonDeg) {
        return (LON_DEG_INTERVAL.contains(lonDeg));
    }

    /**
     * Check if given latitude coordinates
     * is valid according to the geographic coordinates system.
     *
     * @param latDeg longitude input in degrees.
     * @return true if valid
     */
    public static boolean isValidLatDeg(double latDeg) {
        return (LAT_DEG_INTERVAL.contains(latDeg));
    }

    public double lon() {
        return super.lon();
    }

    public double lat() {
        return super.lat();
    }

    public double lonDeg() {
        return super.lonDeg();
    }

    public double latDeg() {
        return super.latDeg();
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(lon=%.4f°, lat=%.4f°)", this.lonDeg(), this.latDeg());
    }
}