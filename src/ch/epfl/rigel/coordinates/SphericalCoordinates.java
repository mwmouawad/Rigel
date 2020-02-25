package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
abstract class SphericalCoordinates {

    private double longitude;
    private double latitude;

    SphericalCoordinates(double longitude, double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    double lon(){
        return this.longitude;
    }

    double lonDeg(){
        return Angle.toDeg(this.longitude);
    }

    double lat(){
        return this.latitude;
    }

    double latDeg(){
        return Angle.toDeg(this.latitude);
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}



