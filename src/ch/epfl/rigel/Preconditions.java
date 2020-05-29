package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;


/**
 * Class Precondtions
 * Intended to be used for checking arguments.
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class Preconditions {
    private Preconditions() { }

    /**
     *
     * @param isTrue condition to be met
     * @throws IllegalArgumentException if the condition is not met.
     */
    static public void checkArgument(boolean isTrue) {
        if (!isTrue) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Checks if a value is contained in a given interval
     * @param interval
     * @param value
     * @return
     */
    static public double checkInInterval(Interval interval, double value) {
        if (!interval.contains(value)) {
            throw new IllegalArgumentException("Not in interval");
        }
        return value;
    }


}
