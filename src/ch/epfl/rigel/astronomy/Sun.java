package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

/**
 * Represents the Sun at a given date/position.
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class Sun extends CelestialObject {

    private final EclipticCoordinates eclipticPos;
    private final float meanAnomaly;

    /**
     * Constructs a Sun object at a given date/position.
     *
     * @param eclipticPos   ecliptic position of the object
     * @param equatorialPos equatorial position of the object
     * @param angularSize   size of the object
     * @param meanAnomaly   mean anomaly of the sun
     * @throws NullPointerException     the equatorialPos or the eclipticPos are null
     * @throws IllegalArgumentException if the angular size is inferior to 0.
     */
    public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly) {
        super("Soleil", equatorialPos, angularSize, -26.7f);
        this.eclipticPos = Objects.requireNonNull(eclipticPos);
        this.meanAnomaly = meanAnomaly;
    }

    /**
     * Returns the position of this sun instance in ecliptic coordinates.
     *
     * @return the position of this sun instance in ecliptic coordinates.
     */
    public EclipticCoordinates eclipticPos() {
        return eclipticPos;
    }

    /**
     * Returns the mean anomaly of this sun instance.
     *
     * @return the position of this sun instance.
     */
    public double meanAnomaly() {
        return meanAnomaly;
    }
}
