package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;


/**
 * Class Precondtions
 * Intended to be used for checking arguments.
 */
public final class Preconditions {
    private Preconditions() {}

        static public void checkArgument(boolean isTrue){
            if(!isTrue){
                throw new IllegalArgumentException();
            }
        }

        static public double checkInInterval(Interval interval, double value){
            if(!interval.contains(value)){
                throw new IllegalArgumentException();
            }
            else{
                return value;
            }
        }


}
