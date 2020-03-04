package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

public abstract class CelestialObject {

    private final String name;
    private final EquatorialCoordinates equatorialPos;
    private final float angularSize;
    private final float magnitude;


    /**
     * @param name          of the celestial object
     * @param equatorialPos equatorial position of the object
     * @param angularSize   size of the object
     * @param magnitude     magnitude of the object
     * @throws NullPointerException     if the name or the equatorial position are null
     * @throws IllegalArgumentException if the angular size is less than 0.
     */
    CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        Preconditions.checkArgument(angularSize >= 0);

        this.name = Objects.requireNonNull(name);
        this.equatorialPos = Objects.requireNonNull(equatorialPos);
        this.angularSize = angularSize;
        this.magnitude = magnitude;
    }

    public String name() {
        return name;
    }

    public EquatorialCoordinates equatorialPos() {
        return equatorialPos;
    }

    //TODO how does it return a double without a cast ?
    public double angularSize() {
        return angularSize;
    }

    public double magnitude() {
        return magnitude;
    }

    public String info() {
        return name;
    }

    @Override
    public String toString() {
        return info();
    }
}
