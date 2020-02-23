package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.lang.Math;
import java.util.Locale;

public final class RightOpenInterval extends Interval {

    private RightOpenInterval(double low, double high){
        super(low, high);
    }


    public static RightOpenInterval of(double low, double high){

        Preconditions.checkArgument(low < high);

        return new RightOpenInterval(low, high);
    }


    public static RightOpenInterval symmetric(double size){

        Preconditions.checkArgument( size > 0);

        //Create low and high bounds centered around zero.
        double centeredLow = -(size / 2);
        double centeredHigh = -centeredLow;

        return new RightOpenInterval(centeredLow, centeredHigh);

    }

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
    double reduce(double v){

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
