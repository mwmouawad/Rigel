package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

public final class Sun extends CelestialObject{

    private EclipticCoordinates eclipticPos;
    private float meanAnomaly;

    /**
     * @param eclipticPos ecliptic position of the object
     * @param equatorialPos equatorial position of the object
     * @param angularSize   size of the object
     * @param meanAnomaly mean anomaly of the sun
     * @throws NullPointerException     if the name or the equatorial position are null
     * @throws IllegalArgumentException if the angular size is less than 0.
     */
    public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly) {
        super("Soleil", equatorialPos, angularSize, -26.7f);
        this.eclipticPos = Objects.requireNonNull(eclipticPos);
        this.meanAnomaly = meanAnomaly;
    }

    public EclipticCoordinates eclipticPos() { return eclipticPos; }
    public double meanAnomaly() { return meanAnomaly; }
}
