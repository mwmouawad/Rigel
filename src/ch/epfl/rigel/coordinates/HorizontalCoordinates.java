package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Represents a pair of Horizontal Coordinates.
 * NB: Is an immutable class. So no setters.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class HorizontalCoordinates extends SphericalCoordinates {

    private final  static RightOpenInterval azDegInterval = RightOpenInterval.of(0,360);
    private final static ClosedInterval altDegInterval = ClosedInterval.of(-90,90);


    private HorizontalCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }


    public static HorizontalCoordinates of(double az, double alt){
        Preconditions.checkInInterval(azDegInterval, Angle.toDeg(az));
        Preconditions.checkInInterval(altDegInterval, Angle.toDeg(alt));
        return  new HorizontalCoordinates(az, alt);
    }

    /**
     * Creates an instance of HorizontalCoordinates.
     * @param azDeg ascension input in degrees between [0°,360°[
     * @param altDeg altitude input in degrees between [-90°, +90°]
     * @throws IllegalArgumentException if the inputs are not contained in the bounds specified above.
     * @return HorizontalCoordinates object.
     */
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


    /**
     * Returns the angular distance between the instance with
     * the input.
     * @param that Horizontal coordinates to be calculated with.
     * @return angular distance.
     */
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

    /**
     * Returns the Octant name in terms of the instance's coordinates.
     * The parameters used compose the output String.
     * @param n string representing North
     * @param e string representing East
     * @param s string representing South
     * @param w string represernting West
     * @return corresponding octant name.
     */
    public String azOctantName(String n, String e, String s, String w) {

        switch ((int) (((az() * 8))/Angle.TAU + 0.5)){
            case 1:
                return n + e;
            case 2:
                return e;
            case 3:
                return s + e;
            case 4:
                return s;
            case 5:
                return s + w;
            case 6:
                return w;
            case 7:
                return n + w;
            case 8:
            case 0:
                return n;
            default:
                throw new IllegalStateException();
        }
    }

    public String toString() {
        return String.format(Locale.ROOT, "(az=%.4f°, alt=%.4f°)", this.azDeg(), this.altDeg());
    }

}
