package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

public enum MoonModel implements CelestialObjectModel<Moon> {

    MOON;


    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        //TODO : CHECK magic numbers
        // + How can we re-use the numbers from sunModel ?
        // + something to normalize ? !!!!!!!!!!!!!!!!!!!!!!!!!!!!!! and in step 4-5!!!!!!!!!!!!!!!!!!!!!!!
        //computes sun constants
        double lonJ2010 = Angle.ofDeg(279.557208);
        double lonPerigee = Angle.ofDeg(283.112438);
        double eccentricitySun = 0.016705;
        double meanAnomalySun = (Angle.TAU/365.242191) * daysSinceJ2010 + lonJ2010 - lonPerigee;
        double sinSun = Math.sin(meanAnomalySun);
        double trueAnomalySun = meanAnomalySun + 2 * eccentricitySun * sinSun;
        double lonSun = trueAnomalySun + lonPerigee;

        //computes Moon constants
        double meanLon = Angle.ofDeg(13.1763966) * daysSinceJ2010 + Angle.ofDeg(91.929336);
        double meanAnomaly = meanLon - Angle.ofDeg(0.1114041) * daysSinceJ2010 - Angle.ofDeg(130.143076);
        double evection = Angle.ofDeg(1.2739) * Math.sin(2*(meanLon - lonSun) - meanAnomaly);
        double annualCorrection = Angle.ofDeg(0.1858) * sinSun;
        double correction3 = Angle.ofDeg(0.37) * sinSun;
        double correctedAnomaly = meanAnomaly + evection - annualCorrection - correction3;
        double correctionCenter = Angle.ofDeg(6.2886) * Math.sin(correctedAnomaly);
        double correction4 = Angle.ofDeg(0.214) * Math.sin(2*correctedAnomaly);
        double lonCorrected = meanLon + evection + correctionCenter - annualCorrection + correction4;
        double variation = Angle.ofDeg(0.6583) * Math.sin(2*(lonCorrected - lonSun));
        double trueLonMoon = lonCorrected + variation;

        //computes ecliptic position
        double lonAsc = Angle.ofDeg(291.682547) - Angle.ofDeg(0.0529539) * daysSinceJ2010;
        double lonAscCorrected = lonAsc - Angle.ofDeg(0.16) * sinSun;
        double tilt = Angle.ofDeg(5.145396);
        double lonEcliptic = Math.atan2(Math.sin(trueLonMoon - lonAscCorrected) * Math.cos(tilt),
                Math.cos(trueLonMoon - lonAscCorrected)) + lonAscCorrected;
        double latEcliptic = Math.asin(Math.sin(trueLonMoon - lonAscCorrected) * Math.sin(tilt));

        //computes the phase
        double phase = ( 1 - Math.cos(trueLonMoon - lonSun) )/ 2;

        //computes angular size
        double eccentricityMoon = 0.0549;
        double distance = (1 - eccentricityMoon * eccentricityMoon)/
                (1 + eccentricityMoon * Math.cos(correctedAnomaly + correctionCenter));
        double theta0 = Angle.ofDeg(0.5181);
        double angularSize = theta0/distance;

        //TODO : do we have to normalize lat and lon ?
        EclipticCoordinates eclipticPos = EclipticCoordinates.of(lonEcliptic, latEcliptic);
        EquatorialCoordinates equatorialPos = eclipticToEquatorialConversion.apply(eclipticPos);

        return new Moon(equatorialPos,(float) angularSize, 0, (float) phase);
    }
}
