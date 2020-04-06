package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.lang.Math;
import java.util.Locale;

/**
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class RightOpenInterval extends Interval {

    private RightOpenInterval(double low, double high){
        super(low, high);
    }

    /**
     * Constructs an Interval open on the right with the given arguments
     * @param low
     * @param high
     * @return
     * @throws IllegalArgumentException
     */
    public static RightOpenInterval of(double low, double high){
        Preconditions.checkArgument(low < high);
        return new RightOpenInterval(low, high);
    }

    /**
     * Constructs an Interval open on the right centered around zero of the given size.
     * @param size
     * @return
     * @throws IllegalArgumentException
     */
    public static RightOpenInterval symmetric(double size){

        Preconditions.checkArgument( size > 0);

        //Create low and high bounds centered around zero.
        double centeredLow = -(size / 2);
        double centeredHigh = -centeredLow;

        return new RightOpenInterval(centeredLow, centeredHigh);

    }

    /**
     *
     * @see Interval#contains(double)
     */
    @Override
    public boolean contains(double v) { return v < this.high() && v >= this.low(); }

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

    /**
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "[%.2f,%.2f[",
                this.low(),
                this.high()
        );
    }
}
