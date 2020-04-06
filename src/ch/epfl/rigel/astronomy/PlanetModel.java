package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import java.util.List;
import java.util.Objects;

/**
 * Models and constructs all the planets of the solar system at a given date.
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
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


    /**
     * Constructs the planet Model. Private method as it is an enum.
     *
     * @param name
     * @param period
     * @param lonJ2010
     * @param lonPerigee
     * @param eccentricity
     * @param axe
     * @param obliquity
     * @param lon_nod
     * @param size
     * @param magnitude
     */
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
     * It returns a Planet instance at the input date.
     *
     * @param daysSinceJ2010                 time difference for the given date. ( can be negative )
     * @param eclipticToEquatorialConversion conversion to be used with.
     * @return Planet instance with computed position, angular size and magnitude for the input.
     */
    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        double trueAnomaly = computeTrueAnomaly(daysSinceJ2010);
        double radius = computeOrbitRadius(trueAnomaly);
        double lon = computeLongitude(trueAnomaly);
        double lon2 = lon;
        double phi = computeLatitude(lon);
        double radius2 = radius * Math.cos(phi);
        //No need to normalize because of the atan2 ans the trigonometric functions
        lon = Math.atan2(Math.sin(lon - lon_nod) * Math.cos(obliquity), Math.cos(lon - lon_nod)) + lon_nod;

        //Earth's Coordinates
        double earthTrueAnomaly = EARTH.computeTrueAnomaly(daysSinceJ2010);
        double R = EARTH.computeOrbitRadius(earthTrueAnomaly);
        double L = EARTH.computeLongitude(earthTrueAnomaly);

        EclipticCoordinates eclCoord;
        //TODO  : compute beta externaly, or not necessary because we are going to compute it once ?
        //axe condition for inner planets
        eclCoord = axe < 1d ? innerPlanetsEclGeocentricCoord(radius2, lon, phi, R, L)
                : outerPlanetsEclGeocentricCoord(radius2, lon, phi, R, L);

        double distanceToEarth = computeDistanceToEarth(radius, lon2, R, L, phi);

        float angularSize = (float) (this.angularSize1UA / distanceToEarth);
        float magnitude = (float) computeMagnitude(radius, lon2, eclCoord.lon(), distanceToEarth, phi);

        EquatorialCoordinates equatorialPos = eclipticToEquatorialConversion.apply(eclCoord);

        return new Planet(this.name, equatorialPos, angularSize, magnitude);

    }

    /**
     * Computes the distance of a planet to Earth.
     * Helper method to assist computing and cleanliness.
     *
     * @param r
     * @param l
     * @param R
     * @param L
     * @param phi
     * @return
     */

    private double computeDistanceToEarth(double r, double l, double R, double L, double phi) {
        return Math.sqrt(R * R + r * r - 2 * R * r * Math.cos(l - L) * Math.cos(phi));
    }

    /**
     * Computes the magnitude of the planet.
     * Helper method to assist computing and cleanliness.
     *
     * @param r
     * @param l
     * @param lambda
     * @param distanceToEarth
     * @param phi
     * @return
     */
    private double computeMagnitude(double r, double l, double lambda, double distanceToEarth, double phi) {
        double F = (1 + Math.cos(lambda - l)) / 2.0;
        return magnitude + 5d * Math.log10((r * distanceToEarth) / Math.sqrt(F));
    }

    /**
     * Computes the true Anomaly of the planet.
     * Helper method to assist computing and cleanliness.
     * *
     *
     * @param daysSinceJ2010
     * @return True anomaly in the interval [0,TAU].
     */
    private double computeTrueAnomaly(double daysSinceJ2010) {
        double meanAnomaly = Angle.normalizePositive((SunModel.SPEED) * (daysSinceJ2010 / period)) + lonJ2010 - lonPerigee;
        double trueAnomaly = (meanAnomaly) + 2d * eccentricity * Math.sin(meanAnomaly);
        return Angle.normalizePositive(trueAnomaly);
    }

    /**
     * Computes the Orbit Radius of the planet.
     * Helper method to assist computing and cleanliness.
     *
     * @param trueAnomaly
     * @return the computed orbit radius.
     */
    private double computeOrbitRadius(double trueAnomaly) {
        return (axe * (1d - eccentricity * eccentricity)) / (1d + eccentricity * Math.cos(trueAnomaly));
    }

    /**
     * Computes the longitude of the Planet in its plan.
     * Helper method to assist computing and cleanliness.
     *
     * @return the computed longitude.
     */
    private double computeLongitude(double trueAnomaly) {
        return (trueAnomaly + lonPerigee);
    }

    /**
     * Computes the latitude of the Planet.
     * Helper method to assist computing and cleanliness.
     *
     * @param longitude
     * @return
     */
    private double computeLatitude(double longitude) {
        return Math.asin(Math.sin(longitude - lon_nod) * Math.sin(obliquity));
    }


    /**
     * Computes the Ecliptic Geocentric Coordinates of an inner planet.
     * Helper method to assist computing and cleanliness.
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
     * Helper method to assist computing and cleanliness.
     *
     * @param planetRadius
     * @param lon
     * @param phi
     * @param R
     * @param L
     * @return
     */
    private EclipticCoordinates outerPlanetsEclGeocentricCoord(double planetRadius, double lon, double phi, double R, double L) {
        double lambda = lon + Math.atan2(R * Math.sin(lon - L), planetRadius - R * Math.cos(lon - L));
        double beta = Math.atan((planetRadius * Math.tan(phi) * Math.sin(lambda - lon)) / (R * Math.sin(lon - L)));
        return EclipticCoordinates.of(Angle.normalizePositive(lambda), beta);
    }


}
