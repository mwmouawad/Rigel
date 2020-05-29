package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;


/**
 * Represents a star at a given date/position.
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class Star extends CelestialObject {

    private final int hipparcosId;
    private final ClosedInterval COLOR_INDEX_INTERVAL = ClosedInterval.of(-0.5, 5.5);
    private final int colorTemperature;
    private final double CONSTANT_TEMPERATURE = 0.92;

    /**
     * Constructs a Star object at a given date/position.
     * The color temperature of the Star is computed given its color index.
     *
     * @param name          of the celestial object
     * @param equatorialPos equatorial position of the object
     * @param magnitude     magnitude of the object
     * @throws NullPointerException     if the name or the equatorial position are null
     * @throws IllegalArgumentException if the hipparcosId is inferior to 0.
     */
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex) {
        super(name, equatorialPos, 0, magnitude);
        Preconditions.checkArgument(hipparcosId >= 0);

        this.hipparcosId = hipparcosId;
        float color = (float) Preconditions.checkInInterval(COLOR_INDEX_INTERVAL, colorIndex);
        colorTemperature = (int) (4600d * ((1d / (CONSTANT_TEMPERATURE * color + 1.7))
                + (1d / (CONSTANT_TEMPERATURE * color + 0.62))));
    }

    /**
     * Returns the hipparcos id this star instance.
     *
     * @return the hipparcos id of this star instance.
     */
    public int hipparcosId() {
        return hipparcosId;
    }

    /**
     * Returns the computed color temperature of a black body model.
     *
     * @return color temperature of this star instance.
     */
    public int colorTemperature() {
        return colorTemperature;
    }
}
