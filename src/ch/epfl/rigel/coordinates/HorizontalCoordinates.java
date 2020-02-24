package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

//NB: Is an immutable class. So no setters.
public final class HorizontalCoordinates extends SphericalCoordinates {

    private static RightOpenInterval azDegInterval = RightOpenInterval.of(0,360);
    private static ClosedInterval altDegInterval = ClosedInterval.of(-90,90);


    private HorizontalCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }

    public static HorizontalCoordinates of(double az, double alt){
        Preconditions.checkInInterval(azDegInterval, Angle.toDeg(az));
        Preconditions.checkInInterval(altDegInterval, Angle.toDeg(alt));
        return  new HorizontalCoordinates(az, alt);
    }

    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg){
        Preconditions.checkInInterval(azDegInterval,azDeg);
        Preconditions.checkInInterval(altDegInterval, altDeg);
        return  new HorizontalCoordinates(Angle.ofDeg(azDeg), Angle.ofDeg(altDeg));
    }

    public double az(){
        return super.lon();
    }

    public double alt(){
        return super.lat();
    }

    public double azDeg(){
        return super.lonDeg();
    }

    public double altDeg(){
        return super.latDeg();
    }


    public double angularDistanceTo(HorizontalCoordinates that){ return angularDistance(this, that); }

    /**
     * Calculates the angular distance in Rad between two Horizontal Coordinates objects.
     * @param a
     * @param b
     * @return
     */
    public static double angularDistance(HorizontalCoordinates a, HorizontalCoordinates b){
        return Math.acos(Math.sin(a.alt()) * Math.sin(b.alt())
                + Math.cos(a.alt()) * Math.cos(b.alt()) * Math.cos(a.az() - b.az())
        );
    }

    public String azOctantName(String N, String E, String S, String W) {
        switch ((int) (Math.round(az() * 8 / Angle.TAU))) {
            case 1:
                return N + E;
            case 2:
                return E;
            case 3:
                return S + E;
            case 4:
                return S;
            case 5:
                return S + W;
            case 6:
                return W;
            case 7:
                return N + W;
            case 8:
            case 0:
                return N;
            default:
                throw new IllegalStateException();
        }
    }

    public String toString() {
        return String.format(Locale.ROOT, "(az=%.4f°, alt=%.4f°)", this.azDeg(), this.altDeg());
    }

}
