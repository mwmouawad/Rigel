package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * Represents a view of the sky in a given moment at a given place.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class ObservedSky {

    private final Sun sun;
    private final List<Planet> planets;
    private final List<Star> stars;
    private final Moon moon;
    private final GeographicCoordinates position;
    private final StereographicProjection projection;
    private final StarCatalogue catalogue;
    private final ZonedDateTime when;

    /**
     * Constructs the observed skyline by computing the position of all planets in the solar system, the moon and
     * the sun.
     * @param time a Zoned Date time when it is observed.
     * @param position the position of the observation.
     * @param projection the StereoGraphic projection meant to be used to draw the sky view.
     * @param catalogue the stars catalogue used.
     */
    public ObservedSky(ZonedDateTime time, GeographicCoordinates position, StereographicProjection projection, StarCatalogue catalogue) {
        //TODO: Are theses checks necessary?
        Preconditions.checkArgument(!(time == null));
        Preconditions.checkArgument(!(position == null));
        Preconditions.checkArgument(!(projection == null));
        Preconditions.checkArgument(!(catalogue == null));
        this.projection = projection;
        this.position = position;
        this.when = time;
        double daysUntil = Epoch.J2010.daysUntil(time);

        EclipticToEquatorialConversion conversion = new EclipticToEquatorialConversion(time);

        sun = SunModel.SUN.at(daysUntil, conversion);
        moon = MoonModel.MOON.at(daysUntil, conversion);

        planets = new ArrayList<>();
        for (PlanetModel p : PlanetModel.ALL) {
            if(!p.equals(PlanetModel.EARTH)){
                planets.add(p.at(daysUntil, conversion));
            }
        }

        stars = Collections.unmodifiableList(catalogue.stars());

        this.catalogue = catalogue;

    }

    /**
     * Returns the sky's sun instance.
     * @return Returns the sky's sun instance
     */
    public Sun sun(){ return sun; }

    /**
     * Returns an array containing all of the  sky's planets.
     *
     * @return Returns an array containing all of the  sky's planets.
     */
    public  List<Planet> planets() { return Collections.unmodifiableList(planets); }

    /**
     * Returns an array containing all of the  sky's stars.
     *
     * @return Returns an array containing all of the  sky's stars.
     */
    //TODO : when  did  we call it  ?
    public List<Star> stars() { return Collections.unmodifiableList(stars); }

    /**
     * Returns the sky's moon instance.
     * @return Returns the sky's moon instance
     */
    public Moon moon() { return moon; }

    /**
     * Returns the Observed sky's sun coordinates.
     * @return Returns the Observed sky's sun coordinates.
     */
    public CartesianCoordinates sunPosition() {
        return this.project(this.sun);
    }

    /**
     * Returns the Observed sky's moon coordinates.
     * @return Returns the Observed sky's moon coordinates.
     */
    public CartesianCoordinates moonPosition() {
        return this.project(this.moon);
    }

    /**
     * Helper method for projecting from Celestial Objects with Equatorial Coordinates.
     * @param celestialObject the celestial object position to project with.
     * @return steographic projection cartesian coordinates.
     */
    private CartesianCoordinates project(CelestialObject celestialObject){
        HorizontalCoordinates horCoordinates = new EquatorialToHorizontalConversion(this.when, this.position).apply(celestialObject.equatorialPos());
        return this.projection.apply(horCoordinates);
    }

    /**
     * Returns an array with the planets' positions.
     * For each planet of the sky, the coordinates are represented in a array of double,
     * the x value is followed by the y value of the corresponding planet in  the array.
     * This is used to make the manipulation of the coordinates easier.
     * @return Returns an array with the decomposed planets' positions
     */
    public double[] planetPositions() {
        double[] positions = new double[planets.size()*2];
        int j = 0;
        for(int i = 0; i<planets.size(); i++){
                CartesianCoordinates projCoord = this.project(planets.get(i));
                positions[j] = projCoord.x();
                positions[j+1] = projCoord.y();
                j+=2;
        }
        return  positions;
    }

    /**
     * Returns an array with the Observed sky stars' positions.
     * For each star of the sky, the coordinates are represented in a array of double,
     * the x value is followed by the y value of the corresponding star in  the array.
     * This is used to make the manipulation of the coordinates easier.
     * @return Returns an array with the decomposed planets' positions
     */
    public double[] starPositions() {
        double[] positions = new double[stars.size()*2];
        int j = 0;
        for(int i = 0; i < stars.size(); i++){
            CartesianCoordinates starCoord = this.project(stars.get(i));
            positions[j] = starCoord.x();
            positions[j+1] = starCoord.y();
            j+=2;
        }
        return positions;
    }

    /**
     * Returns a set with  all of the Observed sky's asterisms.
     * @return Returns the Observed sky's asterisms.
     */
    public Set<Asterism> asterisms() {
        return catalogue.asterisms();
    }

    /**
     * Returns a list with the index of the stars for the input astermism.
     *
     * @param asterism
     * @return list with the index of the stars in the stars list.
     * @throws IllegalArgumentException if the asterism is not stored in the StarCatalogue
     */
    public List<Integer> asterismIndices(Asterism asterism) { return catalogue.asterismIndices(asterism); }

    public Optional objectClosestTo(CartesianCoordinates coordinates, double distance) {

        return Optional.empty();
    }

    private double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt((x1 - x2) * (x1 - x2 ) + (y1 - y2) * (y1 - y2 ));
    }

    //TODO: Comment faire pour pouvoir l'utiliser pour Planet et Stars ?
    /*
    private static double[] computePositions(List<Planet> celestial) {
        double[] positions = new double[celestial.size()*2];
        int j = 0;
        for(int i = 0; i < celestial.size(); i++){
            positions[j] = celestial.get(i).equatorialPos().ra();
            positions[j+1] = celestial.get(i).equatorialPos().dec();
            j+=2;
        }
        return positions;
    }

     */




}
