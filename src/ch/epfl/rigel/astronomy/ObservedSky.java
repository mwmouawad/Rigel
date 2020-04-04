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

        stars = List.copyOf(catalogue.stars());

        this.catalogue = catalogue;

    }

    public Sun sun(){ return sun; }

    public  List<Planet> planets() { return Collections.unmodifiableList(planets); }

    public List<Star> stars() { return Collections.unmodifiableList(stars); }

    public Moon moon() { return moon; }

    public CartesianCoordinates sunPosition() {
        return this.project(this.sun);
    }

    //
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

    public Set<Asterism> asterisms() {
        return catalogue.asterisms();
    }

    public List<Integer> asterismIndices(Asterism asterism) { return catalogue.asterismIndices(asterism); }



    public Optional objectClosestTo(CartesianCoordinates coordinates, double distance) {


        return Optional.empty();
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
