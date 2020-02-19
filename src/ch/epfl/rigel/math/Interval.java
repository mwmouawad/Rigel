package ch.epfl.rigel.math;

public abstract class Interval {

    private final double low;
    private final double high;


    protected Interval(double low, double high){

        this.low = low;
        this.high = high;

    }

    public double low(){
        return this.low;
    }

    public double high(){
        return this.high;
    }

    public double size(){
        return (this.high - this.low);
    }

    //Should return true if and only if v belongs to the interval.
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
