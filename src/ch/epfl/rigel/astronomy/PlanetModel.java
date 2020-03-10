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
    private String name;
    private double period;
    private double lonJ2010;
    private double lonPerigee;
    private double eccentricity;
    private double axe;
    private double obliquity;
    private double lon_nod;
    private double angularSize1UA;
    private double magnitude;
    private static PlanetModel[] innerPlanets = {MERCURY, EARTH, VENUS};


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
        this.angularSize1UA = size; //unite ?
        this.magnitude = magnitude;
    }


    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        double trueAnomaly = computeTrueAnomaly(this, daysSinceJ2010);
        double radius = computeRadius(this, trueAnomaly);
        double lon = computeLongitude(this, trueAnomaly);
        double phi = computeLatitude(this, lon);
        radius *= Math.cos(phi);
        lon = Math.atan2(Math.sin(lon - lon_nod) * Math.cos(obliquity), Math.cos(lon - lon_nod)) + lon_nod;

        //Earth's Coordinates
        double earthTrueAnomaly = computeTrueAnomaly(EARTH, daysSinceJ2010);
        double R = computeRadius(EARTH, earthTrueAnomaly);
        double L = computeLongitude(EARTH, earthTrueAnomaly);

        //Planet Ecliptic Geocentric Coordinates
        EclipticCoordinates eclCoord;
        if (isAnInnerPlanet()) {
            eclCoord = innerPlanetsEclGeocentricCoord(this, daysSinceJ2010, radius, lon, phi, R, L);
        } else {
            eclCoord = outerPlanetsEclGeocentricCoord(this, daysSinceJ2010, radius, lon, phi, R, L);
        }

        double distanceToEarth = computeDistanceToEarth(this, radius, lon, R, L, phi);

        //TODO: Find a bette name
        float angularSize = (float) (this.angularSize1UA / distanceToEarth) ;

        float magnitude = (float) computeMagnitude(this, radius, lon, eclCoord.lon(), distanceToEarth, phi);

        //TODO: We need to convert to equatorial Coordinates?
        EquatorialCoordinates equatorialPos = eclipticToEquatorialConversion.apply(eclCoord);

        return new Planet(this.name,equatorialPos, angularSize, magnitude);

    }

    /**
     * Computes of a planet  to the Earth.
     * @param planetModel
     * @param r
     * @param l
     * @param R
     * @param L
     * @param phi
     * @return
     */
    static double computeDistanceToEarth(PlanetModel planetModel, double r, double l, double R, double L, double phi) {

        double distance = Math.sqrt(
                R * R + r * r - 2 * R * r * Math.cos(l - L) * Math.cos(phi)
        );

        return distance;

    }

    static double computeMagnitude(PlanetModel planetModel, double r, double l, double lambda, double distanceToEarth, double phi) {

        double F = (1 + Math.cos(lambda - l)) / 2.0;

        double magnitude = planetModel.magnitude + 5 * Math.log10( (r * distanceToEarth) / Math.sqrt(F) );

        return magnitude;

    }

    /**
     * Computes true anomaly of a Planet.
     * @param planetModel
     * @param daysSinceJ2010
     * @return
     */
    static double computeTrueAnomaly(PlanetModel planetModel, double daysSinceJ2010) {
        double meanAnomaly = (Angle.TAU / 365.242191) * (daysSinceJ2010 / planetModel.period) + planetModel.lonJ2010 - planetModel.lonPerigee;
        double trueAnomaly = meanAnomaly + 2 * planetModel.lonJ2010 * Math.sin(meanAnomaly);
        return trueAnomaly;

    }

    /**
     * Computes the orbit radius of a planet.
     * @param planetModel
     * @param trueAnomaly
     * @return
     */
    static double computeRadius(PlanetModel planetModel, double trueAnomaly) {

        double radius = (planetModel.axe * (1 - planetModel.eccentricity * planetModel.eccentricity))
                / (1 + planetModel.eccentricity * Math.cos(trueAnomaly));

        return radius;

    }

    /**
     * Computes the longitude of a planet in its orbit plan.
     * @param planetModel
     * @param trueAnomaly
     * @return
     */
    static double computeLongitude(PlanetModel planetModel, double trueAnomaly) {
        return trueAnomaly + planetModel.lonPerigee;
    }

    /**
     * Computes the ecliptic heliocentric latitude.
     * @param planetModel
     * @param longitude
     * @return
     */
    static double computeLatitude(PlanetModel planetModel, double longitude) {
        return Math.asin(Math.sin(longitude - planetModel.lon_nod) * Math.sin(planetModel.obliquity));
    }


    /**
     * Computes the Ecliptic Geocentric Coordinates of an inner planet.
     * @param planetModel
     * @param daysSinceJ2010
     * @param planetRadius
     * @param lon
     * @param phi
     * @param R
     * @param L
     * @return
     */
    private static EclipticCoordinates innerPlanetsEclGeocentricCoord(PlanetModel planetModel, double daysSinceJ2010, double planetRadius, double lon, double phi, double R, double L) {


        double lamdba = Math.PI + L + R + Math.atan2(planetRadius * Math.sin(L - lon), R - planetRadius * Math.cos(L - lon));

        //TODO: Same computation than for outerPlanets! Both should be changed. Or maybe create another method?
        double beta = Math.atan2(planetRadius * Math.tan(phi) * Math.sin(phi - lon), R * Math.sin(lon - L));

        return EclipticCoordinates.of(lamdba, beta);
    }

    /**
     * Computes the Ecliptic Geocentric coordianters of an outer planet.
     * @param planetModel
     * @param daysSinceJ2010
     * @param planetRadius
     * @param lon
     * @param phi
     * @param R
     * @param L
     * @return
     */
    private static EclipticCoordinates outerPlanetsEclGeocentricCoord(PlanetModel planetModel, double daysSinceJ2010, double planetRadius, double lon, double phi, double R, double L) {


        double lambda = lon + Math.atan2(R * Math.sin(lon - L), planetRadius - R * Math.cos(lon - L));

        //TODO: Same computation than for innerPlanets! Both should be changed. Or maybe create another method?
        double beta = Math.atan2(planetRadius * Math.tan(phi) * Math.sin(phi - lon), R * Math.sin(lon - L));

        return EclipticCoordinates.of(lambda, beta);

    }

    /**
     * Checks if the instance belongs to the group of inner planets.
     * @return
     */
    private boolean isAnInnerPlanet() {
        for (PlanetModel planet : innerPlanets) {
            if (this.equals(planet)) {
                return true;
            }
        }
        return false;
    }


}
