package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

import java.util.Locale;

public final class Moon extends CelestialObject {

    private float phase;

    /**
     * @param equatorialPos equatorial position of the object at a given time
     * @param angularSize   size of the object
     * @param magnitude     magnitude of the object
     * @param phase phase of the moon, between [0,1]
     * @throws NullPointerException     if the name or the equatorial position are null
     * @throws IllegalArgumentException if the angular size is less than 0.
     */
    public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase) {
        super("Lune", equatorialPos, angularSize, magnitude);
        //TODO check if we are allowed to add a method to preconditions
        this.phase = Preconditions.checkInInterval(ClosedInterval.of(0,1), phase);
    }

    @Override
    public String info() {
        return String.format(Locale.ROOT, "Lune (%.1f)", phase*100);
    }
}
