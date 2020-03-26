package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class Planet extends CelestialObject {

    /**
     * @param name          of the celestial object
     * @param equatorialPos equatorial position of the object at a given time
     * @param angularSize   size of the object
     * @param magnitude     magnitude of the object
     * @throws NullPointerException     if the name or the equatorial position are null
     * @throws IllegalArgumentException if the angular size is less than 0.
     */
    public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        super(name, equatorialPos, angularSize, magnitude);
    }
}
