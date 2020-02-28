package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class EclipticCoordinates extends SphericalCoordinates {

    private final static RightOpenInterval lonInterval = RightOpenInterval.of(0, Angle.TAU);
    private final static ClosedInterval latInterval = ClosedInterval.symmetric(Math.PI);

    private EclipticCoordinates(double lon, double lat ){
        super(lon, lat);
    }

    /**
     * Creates an instance of Ecliptic Coordinates.
     * @param lon right ascencion input between [0, 2PI[
     * @param lat declination input between [-PI/2, PI]
     * @throws IllegalArgumentException if the inputs are not contained in the bounds specified above.
     * @return EclipticCoordinates instance.
     */
    public static EclipticCoordinates of(double lon, double lat){
        return new EclipticCoordinates(
                Preconditions.checkInInterval(lonInterval,lon),
                Preconditions.checkInInterval(latInterval,lat)
        );
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


    public String toString() {
        return String.format(Locale.ROOT, "(λ=%.4f°, β=%.4f°)", this.lonDeg(), this.latDeg());
    }



}
