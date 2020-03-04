package ch.epfl.rigel.coordinates;

import java.util.Locale;

public final class CartesianCoordinates {

    private double x;
    private double y;

    private CartesianCoordinates(double x, double y){
        this.x = x;
        this.y = y;
    }

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

    //TODO check if correct
    public String toString() {
        return String.format(Locale.ROOT, "(x= %.4f, y= %.4f)", x, y); }
}
