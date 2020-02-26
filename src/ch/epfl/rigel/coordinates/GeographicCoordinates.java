package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Represents a pair of Geographic Coordinates.
 * NB: Is an immutable class. So no setters.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class GeographicCoordinates extends SphericalCoordinates {

    private GeographicCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }

    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
        Preconditions.checkArgument(isValidLonDeg(lonDeg));
        Preconditions.checkArgument(isValidLatDeg(latDeg));
        double lonRad = Angle.ofDeg(lonDeg);
        double latRad = Angle.ofDeg(latDeg);
        return new GeographicCoordinates(lonRad, latRad);
    }

    public static boolean isValidLonDeg(double lonDeg) {
        return (RightOpenInterval.of(-180, 180).contains(lonDeg));
    }

    public static boolean isValidLatDeg(double latDeg) {
        return (ClosedInterval.of(-90, 90).contains(latDeg));
    }

    public double lon(){
        return super.lon();
    }

    public double lat(){
        return super.lat();
    }

    public double lonDeg(){
        return super.lonDeg();
    }

    public double latDeg(){
        return super.latDeg();
    }


    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(lon=%.4f°, lat=%.4f°)", this.lonDeg(), this.latDeg()
        );
    }
}