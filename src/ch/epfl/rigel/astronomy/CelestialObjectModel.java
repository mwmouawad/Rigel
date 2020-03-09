package ch.epfl.rigel.astronomy;


import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;


public interface CelestialObjectModel<O> {

    O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);

}


