package ch.epfl.rigel.math;


/**
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public abstract class Interval {

    private final double low;
    private final double high;


    protected Interval(double low, double high){
        this.low = low;
        this.high = high;
    }

    /**
     *
     * @return the lower bound of the interval
     */
    public double low(){
        return this.low;
    }

    /**
     *
     * @return the upper bound of the interval
     */
    public double high(){
        return this.high;
    }

    /**
     *
     * @return the size of the interval
     */
    public double size(){
        return (this.high - this.low);
    }

    /**
     * Returns true if and only if v belongs to the interval.
     */
    public abstract boolean contains(double v);

    /**
     *
     * @throws  UnsupportedOperationException
     */
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @throws  UnsupportedOperationException
     */
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}
