package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialToHorizontalConversion;
import ch.epfl.rigel.math.Angle;

public enum SunModel implements CelestialObjectModel<Sun>{

    SUN;

    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        double lonJ2010 = Angle.ofDeg(279.557208);
        double lonPerigee = Angle.ofDeg(283.112438);
        double eccentricity = 0.016705;

        double meanAnomaly = (Angle.TAU/365.242191)*daysSinceJ2010 + lonJ2010- lonPerigee;
        double trueAnomaly = meanAnomaly + 2*eccentricity*Math.sin(meanAnomaly);

        double lonEcliptic = trueAnomaly + lonPerigee;
        double latEcliptic = 0; //to delete
        //TODO : do we have to convert theta0 in radian ?
        double theta0 = Angle.ofDeg(0.533128);
        double angularSize = theta0*((1 + eccentricity*Math.cos(trueAnomaly)) / (1 - eccentricity*eccentricity));
        return null;
    }
}
