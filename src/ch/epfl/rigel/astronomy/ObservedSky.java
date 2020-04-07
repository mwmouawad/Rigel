package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.*;

import javax.xml.catalog.Catalog;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Represents a view of the sky in a given moment at a given place.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class ObservedSky {
    //TODO  : idée :
    // + avoir une Map qui associe a chaque objet de l enum sa position dans le ciel de la meme forme que pour planets et stars,
    // map créée dans le constructeur de observedsky, chaque getter renvoie la position.
    private enum SkyObjects {
        MOON,
        SUN,
        PLANETS,
        STARS;
    }
    private final Sun sun;
    private final List<Planet> planets;
    private final Moon moon;
    private final GeographicCoordinates position;
    private final StereographicProjection projection;
    private final StarCatalogue catalogue;
    private final ZonedDateTime when;
    private final Map<SkyObjects, double[]> celObjPositions;

    /**
     * Constructs the observed skyline by computing the position of all planets in the solar system, the moon and
     * the sun.
     * @param time a Zoned Date time when it is observed.
     * @param position the position of the observation.
     * @param projection the StereoGraphic projection meant to be used to draw the sky view.
     * @param catalogue the stars catalogue used.
     */
    public ObservedSky(ZonedDateTime time, GeographicCoordinates position, StereographicProjection projection, StarCatalogue catalogue) {
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

        this.catalogue = catalogue;

        this.celObjPositions = new HashMap<SkyObjects, double[]>();



        //TODO: It this good?
        double[] moonPos = {this.computeMoonPosition().x(), this.computeMoonPosition().y()};
        double[] sunPos = {this.computeSunPosition().x(), this.computeSunPosition().y()};

        this.celObjPositions.put(SkyObjects.MOON, moonPos);
        this.celObjPositions.put(SkyObjects.SUN, sunPos);
        this.celObjPositions.put(SkyObjects.PLANETS, this.computePlanetPositions());
        this.celObjPositions.put(SkyObjects.STARS, this.computeStarPositions());

    }


    /**
     * Returns the sky's sun instance.
     * @return Returns the sky's sun instance.
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
    public List<Star> stars() { return catalogue.stars(); }

    /**
     * Returns the sky's moon instance.
     * @return Returns the sky's moon instance
     */
    public Moon moon() { return moon; }

    /**
     * Returns the Observed sky's sun coordinates.
     * @return Returns the Observed sky's sun coordinates.
     */
    public CartesianCoordinates sunPosition(){
        return CartesianCoordinates.of(this.celObjPositions.get(SkyObjects.SUN)[0], this.celObjPositions.get(SkyObjects.SUN)[1]);
    }

    private CartesianCoordinates computeSunPosition() {
        return this.project(this.sun);
    }

    /**
     * Returns the Observed sky's moon coordinates.
     * @return Returns the Observed sky's moon coordinates.
     */
    public CartesianCoordinates moonPosition() {
        return CartesianCoordinates.of(this.celObjPositions.get(SkyObjects.MOON)[0], this.celObjPositions.get(SkyObjects.MOON)[1]);
    }

    private CartesianCoordinates computeMoonPosition() {
        return this.project(this.moon);
    }


    /**
     * Returns an array with the planets' positions.
     * For each planet of the sky, the coordinates are represented in a array of double,
     * the x value is followed by the y value of the corresponding planet in  the array.
     * This is used to make the manipulation of the coordinates easier.
     * @return Returns an array with the decomposed planets' positions.
     */
    public double[] planetPositions(){
        return this.celObjPositions.get(SkyObjects.PLANETS);
    }


    /**
     * Intended to be used only during init.
     * @return
     */
    private double[] computePlanetPositions() {
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
    public double[] starPositions(){
        return this.celObjPositions.get(SkyObjects.STARS);
    }


    private double[] computeStarPositions() {
        double[] positions = new double[stars().size()*2];
        int j = 0;
        for(int i = 0; i < stars().size(); i++){
            CartesianCoordinates starCoord = this.project(stars().get(i));
            positions[j] = starCoord.x();
            positions[j+1] = starCoord.y();
            j+=2;
        }
        return positions;
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


    /**
     *
     * @param coordinates
     * @param distance
     * @return
     */
    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates coordinates, double distance) {

        //TODO: Je ne sais pas comment utiliser sky objects et repérér l'instance de Celestial Object correspondant de manière performante.
        CelestialObject closestObject = null;
        double objDistance;
        double lowestDistance = distance;

        for(SkyObjects obj: SkyObjects.values()){


        }

        return Optional.empty();
    }

    private double distance(CartesianCoordinates obj1, CartesianCoordinates obj2){
        return Math.sqrt((obj1.x() - obj2.x()) * (obj1.x() - obj2.x() ) + (obj1.y() - obj2.y()) * (obj1.y() - obj2.y()));
    }

    //TODO: Comment faire pour pouvoir l'utiliser pour Planet et Stars ?

    private static double[] computePositions(List<CelestialObject> celestial) {
        double[] positions = new double[celestial.size()*2];
        int j = 0;
        for(int i = 0; i < celestial.size(); i++){
            positions[j] = celestial.get(i).equatorialPos().ra();
            positions[j+1] = celestial.get(i).equatorialPos().dec();
            j+=2;
        }
        return positions;
    }






}
