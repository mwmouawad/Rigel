package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import java.util.List;
import java.util.Objects;

public enum PlanetModel implements CelestialObjectModel<Planet> {
    MERCURY("Mercure", 0.24085, 75.5671, 77.612, 0.205627,
            0.387098, 7.0051, 48.449, 6.74, -0.42),
    VENUS("Vénus", 0.615207, 272.30044, 131.54, 0.006812,
            0.723329, 3.3947, 76.769, 16.92, -4.40),
    EARTH("Terre", 0.999996, 99.556772, 103.2055, 0.016671,
            0.999985, 0, 0, 0, 0),
    MARS("Mars", 1.880765, 109.09646, 336.217, 0.093348,
            1.523689, 1.8497, 49.632, 9.36, -1.52),
    JUPITER("Jupiter", 11.857911, 337.917132, 14.6633, 0.048907,
            5.20278, 1.3035, 100.595, 196.74, -9.40),
    SATURN("Saturne", 29.310579, 172.398316, 89.567, 0.053853,
            9.51134, 2.4873, 113.752, 165.60, -8.88),
    URANUS("Uranus", 84.039492, 271.063148, 172.884833, 0.046321,
            19.21814, 0.773059, 73.926961, 65.80, -7.19),
    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
            30.1985, 1.7673, 131.879, 62.20, -6.87);

    public static final List<PlanetModel> ALL = List.of(values());
    private final String name;
    private final double period;
    private final double lonJ2010;
    private final double lonPerigee;
    private final double eccentricity;
    private final double axe;
    private final double obliquity;
    private final double lon_nod;
    private final double angularSize1UA;
    private final double magnitude;

    //Pourquoi t avais mis en argument daystillJ2010 ?
    // check conditions, null pointers have to take into account ?
    PlanetModel(String name, double period, double lonJ2010, double lonPerigee, double eccentricity, double axe,
                double obliquity, double lon_nod, double size, double magnitude) {
        this.name = Objects.requireNonNull(name);
        this.period = period;
        this.lonJ2010 = Angle.ofDeg(lonJ2010);
        this.lonPerigee = Angle.ofDeg(lonPerigee);
        this.eccentricity = eccentricity;
        this.axe = axe;
        this.obliquity = Angle.ofDeg(obliquity);
        this.lon_nod = Angle.ofDeg(lon_nod);
        this.angularSize1UA = Angle.ofArcsec(size); //unite ?
        this.magnitude = magnitude;
    }


    /**
     * Computes the planet's position, it's angular size and magnitude at a given time and position.
     * It returns a Planet object.
     * @param daysSinceJ2010 time difference for the given date. ( can be negative )
     * @param eclipticToEquatorialConversion conversion to be used with.
     * @return Planet instance with computed position, angular size and magnitude.
     */
    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        double trueAnomaly = computeTrueAnomaly(daysSinceJ2010);
        double radius = computeRadius(trueAnomaly);
        double lon = computeLongitude(trueAnomaly);
        double phi = computeLatitude(lon);
        radius *= Math.cos(phi);
        lon = Math.atan2(Math.sin(lon - lon_nod) * Math.cos(obliquity), Math.cos(lon - lon_nod)) + lon_nod;

        //TODO : to check + tester.
        //Pas besoin de la normaliser.
        lon = (lon);

        //Earth's Coordinates
        double earthTrueAnomaly = EARTH.computeTrueAnomaly(daysSinceJ2010);
        double R = EARTH.computeRadius(earthTrueAnomaly);
        double L = EARTH.computeLongitude(earthTrueAnomaly);

        EclipticCoordinates eclCoord;
        //axe condition for inner planets
        eclCoord = axe < 1d ? innerPlanetsEclGeocentricCoord(radius, lon, phi, R, L)
                : outerPlanetsEclGeocentricCoord(radius, lon, phi, R, L);

        double distanceToEarth = computeDistanceToEarth(radius, lon, R, L, phi);
        float angularSize = (float) (this.angularSize1UA / distanceToEarth);
        float magnitude = (float) computeMagnitude(radius, lon, eclCoord.lon(), distanceToEarth, phi);

        EquatorialCoordinates equatorialPos = eclipticToEquatorialConversion.apply(eclCoord);

        return new Planet(this.name, equatorialPos, angularSize, magnitude);

    }

    /**
     * Computes the distance of a planet to Earth.
     *
     * @param r
     * @param l
     * @param R
     * @param L
     * @param phi
     * @return
     */

    private static double computeDistanceToEarth(double r, double l, double R, double L, double phi) {
        return Math.sqrt(R * R + r * r - 2 * R * r * Math.cos(l - L) * Math.cos(phi));
    }

    private double computeMagnitude(double r, double l, double lambda, double distanceToEarth, double phi) {
        double F = (1 + Math.cos(lambda - l)) / 2.0;
        return magnitude + 5 * Math.log10((r * distanceToEarth) / Math.sqrt(F));
    }

    /**
     * Computes true anomaly of a Planet.
     *
     * @param daysSinceJ2010
     * @return True anomaly in the interval [0,TAU]
     */
    private double computeTrueAnomaly(double daysSinceJ2010) {
        double meanAnomaly = Angle.normalizePositive((SunModel.SPEED) * (daysSinceJ2010 / period)) + lonJ2010 - lonPerigee;
        double trueAnomaly = (meanAnomaly) + 2 * this.eccentricity * Math.sin(meanAnomaly);
        return Angle.normalizePositive(trueAnomaly);
    }

    /**
     * Computes the orbit radius of a planet.
     *
     * @param trueAnomaly
     * @return
     */
    private double computeRadius(double trueAnomaly) {
        return (axe * (1 - eccentricity * eccentricity)) / (1 + eccentricity * Math.cos(trueAnomaly));
    }

    /**
     * Computes the longitude of a planet in its orbit plan.
     * @param trueAnomaly
     * @return
     */
    private double computeLongitude(double trueAnomaly) {
        //Pas besoin de la normaliser.
        return (trueAnomaly + this.lonPerigee);
    }

    /**
     * Computes the ecliptic heliocentric latitude.
     * @param longitude
     * @return
     */
    private double computeLatitude(double longitude) {
        return Math.asin(Math.sin(longitude - this.lon_nod) * Math.sin(this.obliquity));
    }


    /**
     * Computes the Ecliptic Geocentric Coordinates of an inner planet.
     *
     * @param planetRadius
     * @param lon
     * @param phi
     * @param R
     * @param L
     * @return
     */
    private EclipticCoordinates innerPlanetsEclGeocentricCoord(double planetRadius, double lon, double phi, double R, double L) {
        double lambda = Math.PI + L + Math.atan2(planetRadius * Math.sin(L - lon), R - planetRadius * Math.cos(L - lon));
        double beta = Math.atan((planetRadius * Math.tan(phi) * Math.sin(lambda - lon)) / (R * Math.sin(lon - L)));
        return EclipticCoordinates.of(Angle.normalizePositive(lambda), beta);
    }

    /**
     * Computes the Ecliptic Geocentric coordianters of an outer planet.
     *
     * @param planetRadius
     * @param lon
     * @param phi
     * @param R
     * @param L
     * @return
     */
    private static EclipticCoordinates outerPlanetsEclGeocentricCoord(double planetRadius, double lon, double phi, double R, double L) {
        double lambda = lon + Math.atan2(R * Math.sin(lon - L), planetRadius - R * Math.cos(lon - L));
        double beta = Math.atan((planetRadius * Math.tan(phi) * Math.sin(lambda - lon)) / (R * Math.sin(lon - L)));
        return EclipticCoordinates.of(Angle.normalizePositive(lambda), beta);
    }


}
