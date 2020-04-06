package ch.epfl.rigel.math;


/**
 * Mother class representing mathematical intervals, such as [a,b], [a,b[...
 * with a < b.
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public abstract class Interval {

    private final double low;
    private final double high;


    /**
     * Creates an interval instance.
     *
     * @param low
     * @param high
     */
    protected Interval(double low, double high) {

        this.low = low;
        this.high = high;

    }

    /**
     * Returns the lower bound of the interval.
     *
     * @return the lower bound of the interval.
     */
    public double low() {
        return this.low;
    }

    /**
     * Returns the higher bound of the interval.
     *
     * @return the higher bound of the interval.
     */
    public double high() {
        return this.high;
    }

    /**
     * Returns the size of the interval by computing higherBound - lowerBound.
     *
     * @return the size of the interval.
     */
    public double size() {
        return (this.high - this.low);
    }

    public abstract boolean contains(double v);

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}
