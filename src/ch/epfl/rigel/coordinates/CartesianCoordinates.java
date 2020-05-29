package ch.epfl.rigel.coordinates;

import java.util.Locale;

/**
 * Represents a couple of Cartesian Coordinates (x,y).
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
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

    /**
     * Gives the x coordinates
     * @return
     */
    public double x() { return x; }

    /**
     * Gives the y coordinates
     * @return
     */
    public double y() { return y; }

    /**
     *
     *  Not used here
     * @throws UnsupportedOperationException
     */
    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     *
     *  Not used here
     * @throws UnsupportedOperationException
     */
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(x= %.4f, y= %.4f)", x, y); }
}
