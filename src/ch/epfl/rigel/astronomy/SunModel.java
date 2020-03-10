package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.coordinates.EquatorialToHorizontalConversion;
import ch.epfl.rigel.math.Angle;

public enum SunModel implements CelestialObjectModel<Sun>{

    SUN;

    private static double lonJ2010 = Angle.ofDeg(279.557208);
    private static double lonPerigee = Angle.ofDeg(283.112438);
    private static double eccentricity = 0.016705;

    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        double meanAnomaly = (Angle.TAU/365.242191) * daysSinceJ2010 + lonJ2010 - lonPerigee;
        double trueAnomaly = meanAnomaly + 2 * eccentricity * Math.sin(meanAnomaly);

        double lonEcliptic = trueAnomaly + lonPerigee;
        //TODO : do we have to convert theta0 in radian ?
        // explicit cast for angularSize, don't want to lose precision ?
        // mean anomaly is the one computed above?
        // something to normalize?
        double theta0 = Angle.ofDeg(0.533128);
        double angularSize = theta0 * ((1 + eccentricity*Math.cos(trueAnomaly)) / (1 - eccentricity*eccentricity));

        EclipticCoordinates eclipticPos = EclipticCoordinates.of(lonEcliptic, 0);
        EquatorialCoordinates equatorialPos = eclipticToEquatorialConversion.apply(eclipticPos);

        return new Sun(eclipticPos, equatorialPos, (float) angularSize, (float) meanAnomaly);
    }
}
