package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

public final class Polynomial {

    double coefficients[];

    public static void main(String[] args) {

        Polynomial poly = Polynomial.of(3,2,1,1);
        System.out.println(poly.at(2));

    }

    private Polynomial(double coefficientN, double... coefficients){

        double[] newTable = new double[coefficients.length +1];

        newTable[0] = coefficientN;
        System.arraycopy(coefficients, 0, newTable, 1, coefficients.length);

        this.coefficients = newTable;

    }

    public static Polynomial of(double coefficientN, double... coefficients){

        Preconditions.checkArgument(coefficientN != 0);

        return new Polynomial(coefficientN,coefficients);

    }

    public double at(double x){

        double value = this.coefficients[0] * x + this.coefficients[1];


        for(var i = 2; i< this.coefficients.length; i++){

            value = value * x + this.coefficients[i];
        }

        return value;

    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}
