package ch.epfl.rigel.astronomy;


import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

/**
 * Interface for celestial objects models.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public interface CelestialObjectModel<O> {

    /**
     * Computes the position and attributes of the celestial object at a given date and position.
     * @param daysSinceJ2010 time difference for the given date. ( can be negative )
     * @param eclipticToEquatorialConversion conversion to be used with.
     * @return the object in its position for the given inputs.
     */
    O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);

}


