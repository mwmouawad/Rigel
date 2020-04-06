package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

/**
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public abstract class CelestialObject {

    private final String name;
    private final EquatorialCoordinates equatorialPos;
    private final float angularSize;
    private final float magnitude;

    //TODO : constructeur public ?
    /**
     * @param name          of the celestial object
     * @param equatorialPos equatorial position of the object
     * @param angularSize   size of the object
     * @param magnitude     magnitude of the object
     * @throws NullPointerException     if the name or the equatorial position are null
     * @throws IllegalArgumentException if the angular size is less than 0.
     */
    public CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        Preconditions.checkArgument(angularSize >= 0);

        this.name = Objects.requireNonNull(name);
        this.equatorialPos = Objects.requireNonNull(equatorialPos);
        this.angularSize = angularSize;
        this.magnitude = magnitude;
    }

    /**
     *
     * @return name of object
     */
    public String name() {
        return name;
    }
    /**
     *
     * @return the equatorial position of the object
     */
    public EquatorialCoordinates equatorialPos() {
        return equatorialPos;
    }

    /**
     *
     * @return the angular size of the object
     */
    public double angularSize() {
        return angularSize;
    }

    /**
     *
     * @return the magnitude of the object
     */
    public double magnitude() {
        return magnitude;
    }

    /**
     *
     * @return a description of the object
     */
    public String info() {
        return name;
    }

    /**
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return info();
    }
}
