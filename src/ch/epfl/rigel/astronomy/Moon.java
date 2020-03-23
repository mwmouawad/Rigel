package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

import java.util.Locale;

/**
 * Represents the moon planet.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class Moon extends CelestialObject {

    private final static ClosedInterval PHASE_INTERVAL = ClosedInterval.of(0,1);
    private final float phase;

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
        this.phase = (float) Preconditions.checkInInterval(PHASE_INTERVAL, phase);
    }

    @Override
    public String info() {
        return String.format(Locale.ROOT, "Lune (%.1f%%)", phase*100);
    }
}
