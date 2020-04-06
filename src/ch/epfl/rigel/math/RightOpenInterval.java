package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.lang.Math;
import java.util.Locale;

/**
 * Represents a mathematical closed interval
 * of the form [a,b[, meaning b is EXCLUDED.
 * Also offers methods for clipping.
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class RightOpenInterval extends Interval {

    private RightOpenInterval(double low, double high){
        super(low, high);
    }


    /**
     * Creates an instance of RightOpenInterval with inputs lower bound and higher bound.
     * The higher bound being excluded. Lower needs to be inferior than the higher.
     *
     * @param low  the lower bound of the interval, needs to be inferior than the high param.
     * @param high the higher bound of the interval, needs to be superior than the low param.
     * @return instance of a closed interval of the form [low,high].
     * @throws IllegalArgumentException if high is not strictly inferior to low.
     */
    public static RightOpenInterval of(double low, double high){

        Preconditions.checkArgument(low < high);
        return new RightOpenInterval(low, high);
    }


    /**
     * Creates an instance of RightOpenInterval symmetric centered in 0 of given size. The interval
     * will be of the form [-size/2, size/2[.
     *
     * @param size the size of the given interval.
     * @return a ClosedInterval of given size symmetric entered in 0.
     */
    public static RightOpenInterval symmetric(double size){

        Preconditions.checkArgument( size > 0);
        //Create low and high bounds centered around zero.
        double centeredLow = -(size / 2);
        double centeredHigh = -centeredLow;
        return new RightOpenInterval(centeredLow, centeredHigh);

    }

    /**
     * Returns true if the input value is contained in the interval.
     * Returns false otherwise.
     *
     * @param v
     * @return true if the input value is contained in the interval. False otherwise.
     */
    @Override
    public boolean contains(double v) {
        if( v < this.high() && v >= this.low() ){
            return true;
        }

        return false;
    }

    /**
     * Method based on reducing interval.
     * @param v the input value to be reduced to the instance interval.
     * @return
     */
    public double reduce(double v){

        double a = this.low();
        double b = this.high();
        double x = v - a;
        double y = b - a;

        double floorModResult = x - y * Math.floor(x/y);

        return (a + floorModResult);

    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "[%.2f,%.2f[",
                this.low(),
                this.high()
        );
    }
}
