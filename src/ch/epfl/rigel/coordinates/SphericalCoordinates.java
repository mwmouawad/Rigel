package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 * Mother class representing a wide variety of =coordinates
 * systems based on Spherical Coordinates
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public abstract class SphericalCoordinates {

    private double longitude;
    private double latitude;

    /**
     * Constructs a Spherical Coordinates object.
     *
     * @param longitude
     * @param latitude
     */
    //TODO: package private ?
    SphericalCoordinates(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Returns the longitude in radian degrees
     *
     * @return longitude
     */
    public double lon() {
        return this.longitude;
    }

    /**
     * Returns the longitude in celsius degrees.
     *
     * @return
     */
    public double lonDeg() {
        return Angle.toDeg(this.longitude);
    }

    /**
     * Returns the latitude in radian degrees
     *
     * @return latitude in radian degrees.
     */
    public double lat() {
        return this.latitude;
    }

    /**
     * Returns the latitude in celsius degrees
     *
     * @return latitude in celsius degrees.
     */
    public double latDeg() {
        return Angle.toDeg(this.latitude);
    }

    /**
     * Not intended to be used. Will throw an exception.
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     * Not intended to be used. Will throw an exception.
     *
     * @param obj object to be compared with.
     * @throws UnsupportedOperationException
     */
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}



