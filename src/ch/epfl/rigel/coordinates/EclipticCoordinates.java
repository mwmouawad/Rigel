package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class EclipticCoordinates extends SphericalCoordinates {

    private final static RightOpenInterval lonInterval = RightOpenInterval.of(0,360);
    private final static ClosedInterval latInterval = ClosedInterval.of(-90,90);


    private EclipticCoordinates(double lon, double lat ){
        super(lon, lat);
    }

    public static EclipticCoordinates of(double lon, double lat){

        //TODO: Check conditions
        Preconditions.checkArgument(lonInterval.contains(lon));
        Preconditions.checkArgument(latInterval.contains(lat));


        return new EclipticCoordinates(lon,lat);

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
