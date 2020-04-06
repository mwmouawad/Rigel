package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class ClosedInterval extends Interval  {

    private ClosedInterval(double low, double high){
        super(low, high);
    }

    /**
     * Constructs an instance of the class with an upper and lower bound for the interval.
     * @param low
     * @param high
     * @return
     * @throws IllegalArgumentException
     */
    public static ClosedInterval of(double low, double high){
        Preconditions.checkArgument(low < high);
        return new ClosedInterval(low, high);
    }

    /**
     * Constructs an interval centered around zero of the size given in argument.
     * @param size
     * @return
     * @throws IllegalArgumentException
     */
    public static ClosedInterval symmetric(double size){
        Preconditions.checkArgument(size>0);
        double centeredLow = -(size / 2);
        double centeredHigh = -centeredLow;
        return new ClosedInterval(centeredLow, centeredHigh);

    }

    /**
     * 
     * @param v
     * @see Interval#contains(double) 
     */
    @Override
    public boolean contains(double v) { return (v >= low() && v <= high()); }

    /**
     * Method based on clipping interval. If v is lower (higher) than the interval
     * return the lower bound (higher bound) .
     * @param v the input value to be clipped.
     * @return
     */
    public double clip(double v){
        if( v <= this.low()){
            return this.low();
        } else if( v > this.high()){
            return this.high();
        }
        return v;
    }

    /**
     * 
     * @see Object#toString() 
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "[%.2f,%.2f]", this.low(), this.high());
    }
}
