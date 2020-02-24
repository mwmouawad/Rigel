package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

public final class ClosedInterval extends Interval  {

    private ClosedInterval(double low, double high){
        super(low, high);
    }


    public static ClosedInterval of(double low, double high){
        Preconditions.checkArgument(low < high);
        return new ClosedInterval(low, high);
    }


    public static ClosedInterval symmetric(double size){
        Preconditions.checkArgument(size>0);
        //Create low and high bounds centered around zero.
        double centeredLow = -(size / 2);
        double centeredHigh = -centeredLow;
        return new ClosedInterval(centeredLow, centeredHigh);

    }

    @Override
    public boolean contains(double v) { return (v >= low() && v <= high()); }

    /**
     * Method based on clipping interval. If v is lower (higher) than the interval
     * return the lower bound (higher bound) .
     * @param v the input value to be clipped.
     * @return
     */
    double clip(double v){
        if( v <= this.low()){
            return this.low();
        } else if( v > this.high()){
            return this.high();
        }
        return v;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "[%.2f,%.2f]", this.low(), this.high());
    }
}
