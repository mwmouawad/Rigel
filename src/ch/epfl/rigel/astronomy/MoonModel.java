package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

public enum MoonModel implements CelestialObjectModel<Moon> {

    MOON;

    private final double ECCENTRICITY_MOON = 0.0549;
    private final double TILT = Angle.ofDeg(5.145396);
    private final double MEAN_LON = Angle.ofDeg(91.929336);
    private final double MEAN_LON_PERI = Angle.ofDeg(130.143076);
    private final double LON_ASC = Angle.ofDeg(291.682547);
    private final double THETA_0 = Angle.ofDeg(0.5181);

    /**
     *
     * @param daysSinceJ2010 time difference for the given date. ( can be negative )
     * @param eclipticToEquatorialConversion conversion to be used with.
     * @return a Moon at a given time.
     */
    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        //creates a sun model in order to use its constants.
        Sun sun = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);
        double meanAnomalySun = sun.meanAnomaly();;
        double sinSun = Math.sin(meanAnomalySun);
        double lonSun = sun.eclipticPos().lon();

        //computes Moon constants
        double meanLonOrb = Angle.ofDeg(13.1763966) * daysSinceJ2010 + MEAN_LON;
        double meanAnomaly = Angle.normalizePositive(meanLonOrb - Angle.ofDeg(0.1114041) * daysSinceJ2010 - MEAN_LON_PERI);
        double evection = Angle.ofDeg(1.2739) * Math.sin(2*(meanLonOrb - lonSun) - meanAnomaly);
        double annualCorrection = Angle.ofDeg(0.1858) * sinSun;
        double correction3 = Angle.ofDeg(0.37) * sinSun;
        double correctedAnomaly = meanAnomaly + evection - annualCorrection - correction3;
        double correctionCenter = Angle.ofDeg(6.2886) * Math.sin(correctedAnomaly);
        double correction4 = Angle.ofDeg(0.214) * Math.sin(2*correctedAnomaly);
        double lonCorrected = meanLonOrb + evection + correctionCenter - annualCorrection + correction4;
        double variation = Angle.ofDeg(0.6583) * Math.sin(2*(lonCorrected - lonSun));
        double trueLonMoon = lonCorrected + variation;

        //computes ecliptic position
        double meanLonAsc = LON_ASC - Angle.ofDeg(0.0529539) * daysSinceJ2010;
        double lonAscCorrected = meanLonAsc - Angle.ofDeg(0.16) * sinSun;
        double lonEcliptic = Math.atan2(Math.sin(trueLonMoon - lonAscCorrected) * Math.cos(TILT),
                Math.cos(trueLonMoon - lonAscCorrected)) + lonAscCorrected;
        double latEcliptic = Math.asin(Math.sin(trueLonMoon - lonAscCorrected) * Math.sin(TILT));

        //computes the phase
        double phase = ( 1 - Math.cos(trueLonMoon - lonSun) )/ 2;

        //computes angular size
        double distance = (1 - ECCENTRICITY_MOON * ECCENTRICITY_MOON)/
                (1 + ECCENTRICITY_MOON * Math.cos(correctedAnomaly + correctionCenter));
        double angularSize = THETA_0 /distance;

        EclipticCoordinates eclipticPos = EclipticCoordinates.of(Angle.normalizePositive(lonEcliptic), latEcliptic);
        EquatorialCoordinates equatorialPos = eclipticToEquatorialConversion.apply(eclipticPos);

        return new Moon(equatorialPos,(float) angularSize, 0, (float) phase);
    }
}
