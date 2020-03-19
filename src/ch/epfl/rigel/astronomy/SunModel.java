package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.coordinates.EquatorialToHorizontalConversion;
import ch.epfl.rigel.math.Angle;

/**
 * Sun model.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public enum SunModel implements CelestialObjectModel<Sun>{

    SUN;

    private static double lonJ2010 = Angle.ofDeg(279.557208);
    private static double lonPerigee = Angle.ofDeg(283.112438);
    private static double eccentricity = 0.016705;

    @Override
    /**
     * Computes the position, angularSize and meanAnomaly of the SUN object at a given date and position.
     * @param daysSinceJ2010 time difference for the given date. ( can be negative )
     * @param eclipticToEquatorialConversion conversion to be used with.
     * @return the Sun in its position and other attributes for the given inputs.
     */
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        double meanAnomaly = (Angle.TAU/365.242191) * daysSinceJ2010 + lonJ2010 - lonPerigee;
        meanAnomaly = Angle.normalizePositive(meanAnomaly);
        double trueAnomaly = meanAnomaly + 2 * eccentricity * Math.sin(meanAnomaly);
        trueAnomaly = Angle.normalizePositive(trueAnomaly);

        double lonEcliptic = Angle.normalizePositive(trueAnomaly + lonPerigee);
        double theta0 = Angle.ofDeg(0.533128);
        double angularSize = theta0 * ((1 + eccentricity*Math.cos(trueAnomaly)) / (1 - eccentricity*eccentricity));

        EclipticCoordinates eclipticPos = EclipticCoordinates.of(lonEcliptic, 0);
        EquatorialCoordinates equatorialPos = eclipticToEquatorialConversion.apply(eclipticPos);

        return new Sun(eclipticPos, equatorialPos, (float) angularSize, (float) meanAnomaly);
    }
}
