package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

public final class Polynomial {

    double coefficients[];


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

        //Take a look at the degree of the Polynomial
        int coeffLength = this.coefficients.length;

        if(coeffLength == 1){
            return this.coefficients[0];
        }

        double value = this.coefficients[0] * x + this.coefficients[1];


        for (var i = 2; i < this.coefficients.length; i++) {

            value = value * x + this.coefficients[i];
        }

        return value;

    }

    @Override
    //TODO
    public String toString() {
        StringBuilder sb = new StringBuilder();

        //Check the degree of the Polynomial
        int coeffLength = this.coefficients.length;

        if(coeffLength == 1){
            sb.append(this.coefficients[0]);
        }
        else if (coeffLength > 1){


            for(var i = 0;i<coeffLength ; i++ ){

                if(this.coefficients[i] != 0 ){

                    if(this.coefficients[i] == 1){

                    }
                    else if(this.coefficients[i] == -1){
                        sb.append("-");
                    }
                    else{
                        sb.append(this.coefficients[i]);
                    }

                    //Add x to the string if it isn't the last coefficient
                    if(i < coeffLength - 1){

                        sb.append("x");
                        //Variable storing the current loop degree x of the polynomial
                        int currentDeg = coeffLength - i -1;
                        //If degree equal x do not add the ^ and the degree coefficient.
                        if(currentDeg != 1){
                            sb.append("^");
                            sb.append(currentDeg);
                        }

                        //Check if the next coefficient is negative. Otherwise add a "+" string.
                        if(this.coefficients[i+1] > 0){
                            sb.append("+");
                        }

                    }

                }



            }


        }

        return sb.toString();

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
