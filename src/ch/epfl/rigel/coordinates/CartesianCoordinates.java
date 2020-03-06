package ch.epfl.rigel.coordinates;

import java.util.Locale;

/**
 * Represents a couple of Cartesian Coordinates (x,y).
 */
public final class CartesianCoordinates {

    private double x;
    private double y;

    private CartesianCoordinates(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * Creates an instance of cartesian coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @return CartesianCoordinates instance.
     */
    public static CartesianCoordinates of(double x, double y){
        return new CartesianCoordinates(x,y);
    }

    public double x() { return x; }
    public double y() { return y; }

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return String.format(Locale.ROOT, "(x= %.4f, y= %.4f)", x, y); }
}
