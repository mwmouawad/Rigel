package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.lang.Math;

public final class Angle {

    static private double TAU = 2*Math.PI ;
    private static final double DEG_PER_RAD = 360.0 / TAU;
    static private RightOpenInterval zeroToTAU =  RightOpenInterval.of(0, TAU);


    private Angle(){

    }

    /**
     * Normalize the input rad angle to the interval [0, TAU[.
     * @param rad
     * @return normalized angle.
     */
    public static double normalizePositive(double rad){
        return zeroToTAU.reduce(rad);
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

    public static double toDeg(double rad){
        return Math.toDegrees(rad);
    }

    public static double ofDeg(double deg){
        return Math.toRadians(deg);
    }

    /**
     * Convert the input angle to degrees.
     * @param hr input angles in hours.
     * @return
     */
    public static double ofHr(double hr){
        return hr * 15;
    }

    /**
     * Convert the input angle to degrees.
     * @param rad input angles in hours.
     * @return
     */
    public static double toHr(double rad){

        double deg = ofDeg(rad);

        return deg / 15;

    }

    double ofDMS(int deg, int min, double sec){

        Preconditions.checkArgument(min<60 && min>=0 && sec<60 && sec>=0);

        //Convert arcminutes to Degrees
        double minInDeg = min / 60;
        //Convert arcseconds to Degrees
        double secInDeg = sec / 3600;


        return Math.toRadians(deg)  + Math.toRadians(minInDeg) + Math.toRadians(secInDeg);

    }


}
