package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * Represents a mathematical closed interval
 * of the form [a,b]. Also offers methods for clipping
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class ClosedInterval extends Interval {

    private ClosedInterval(double low, double high) {
        super(low, high);
    }


    /**
     * Creates an instance of ClosedInterval with inputs lower bound and higher bound.
     * The higher bound being included.
     * Low needs to be inferior than the high.
     *
     * @param low  the lower bound of the interval, needs to be inferior than the high param.
     * @param high the higher bound of the interval, needs to be superior than the low param.
     * @return instance of a closed interval of the form [low,high].
     * @throws IllegalArgumentException if high is not strictly inferior to low.
     */
    public static ClosedInterval of(double low, double high) {
        Preconditions.checkArgument(low < high);
        return new ClosedInterval(low, high);
    }


    /**
     * Creates an instance of ClosedInterval symmetric centered in 0 of given size. The interval
     * will be of the form [-size/2, size/2]
     *
     * @param size the size of the given interval.
     * @return a ClosedInterval of given size symmetric entered in 0.
     */
    public static ClosedInterval symmetric(double size) {
        Preconditions.checkArgument(size > 0);
        //Create low and high bounds centered around zero.
        double centeredLow = -(size / 2);
        double centeredHigh = -centeredLow;
        return new ClosedInterval(centeredLow, centeredHigh);

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
        return (v >= low() && v <= high());
    }

    /**
     * Method based on clipping interval. If v is lower (higher) than the interval
     * return the lower bound (higher bound) .
     *
     * @param v the input value to be clipped.
     * @return
     */
    public double clip(double v) {
        if (v <= this.low()) {
            return this.low();
        } else if (v > this.high()) {
            return this.high();
        }
        return v;
    }


    @Override
    public String toString() {
        return String.format(Locale.ROOT, "[%.2f,%.2f]", this.low(), this.high());
    }
}
