package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

import java.util.List;

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
    private double angularSize;
    private double magnitude;

    //TODO check if public
    PlanetModel(String name, double period, double lonJ2010, double lonPerigee, double eccentricity, double axe,
                double obliquity, double lon_nod, double size, double magnitude){


    }


    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        return null;
    }


    //Attention : ces paramètres doivent être acceptés dans les mêmes unités que celles utilisées dans ces tables,
    // mais les angles doivent tous être convertis en radians avant d'être stockés ou utilisés,
    // conformément à la convention utilisée dans ce projet.
}
