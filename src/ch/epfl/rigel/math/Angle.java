package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.lang.Math;

/**
 * Tools for angle conversions.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class Angle {

    public final static  double TAU = 2*Math.PI ;
    private static final double DEG_PER_RAD = 360.0 / TAU;
    static private RightOpenInterval ZERO_TO_TAU =  RightOpenInterval.of(0, TAU);


    private Angle(){ }

    /**
     * Normalize the input rad angle to the interval [0, TAU[.
     * @param rad angle to be normalized to the interval [0,TAU[
     * @return normalized angle in the interval [0,TAU[
     */
    public static double normalizePositive(double rad){
        return ZERO_TO_TAU.reduce(rad);
    };

    /** Converts arcsec angle to radians
     * @param sec angle in arcseconds.
     * @return
     */
    public static double ofArcsec(double sec){

        //Convert sec to degrees
        double deg = sec / 3600;

        return ofDeg(deg);

    }

    /**
     * Consvert an angle of radian degrees to celsius degrees.
     * @param rad  angle in radians to be converted.
     * @return  the angle in celsius degrees.
     */
    public static double toDeg(double rad){
        return Math.toDegrees(rad);
    }

    /**
     * Convert an angle of celsius degrees to celsius radian.
     * @param deg angle in celsius degrees.
     * @return the angle in radian degrees.
     */
    public static double ofDeg(double deg){ return Math.toRadians(deg); }

    /**
     * Convert the input angle to degrees.
     * @param hr input angles in hours.
     * @return
     */
    public static double ofHr(double hr){
        return ofDeg(hr * 15);
    }

    /**
     * Convert the input angle to hours.
     * @param rad input angles in hours.
     * @return
     */
    public static double toHr(double rad){

        double deg = toDeg(rad);
        return deg / 15;

    }

    /**
     * Converts the input from degree, minutes and seconds
     * to rad.
     * @param deg input in degrees.
     * @param min minutes.
     * @param sec seconds.
     * @return rad angle.
     */
    public static double ofDMS(int deg, int min, double sec){

        Preconditions.checkArgument(min<60 && min>=0 && sec<60 && sec>=0 && deg >= 0);

        //Convert arcminutes to Degrees
        double minInDeg = (double)min / 60;
        //Convert arcseconds to Degrees
        double secInDeg = sec / 3600;

        return ofDeg(deg + minInDeg + secInDeg);
    }


}
