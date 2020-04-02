package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;

import java.time.ZonedDateTime;
import java.util.*;

public class ObservedSky {

    Sun sun;
    List<Planet> planets;
    List<Star> stars;
    Moon moon;
    GeographicCoordinates position;
    StereographicProjection projection;
    StarCatalogue catalogue;

    public ObservedSky(ZonedDateTime time, GeographicCoordinates position, StereographicProjection projection, StarCatalogue catalogue) {
        Preconditions.checkArgument(!(time == null));
        Preconditions.checkArgument(!(position == null));
        Preconditions.checkArgument(!(projection == null));
        Preconditions.checkArgument(!(catalogue == null));
        this.projection = projection;
        this.position = position;
        double daysUntil = Epoch.J2010.daysUntil(time);

        EclipticToEquatorialConversion conversion = new EclipticToEquatorialConversion(time);

        sun = SunModel.SUN.at(daysUntil, conversion);

        planets = new ArrayList<>();
        for (PlanetModel p : PlanetModel.values()) {
            planets.add(p.at(daysUntil, conversion));
        }
        planets.remove(PlanetModel.EARTH);
        planets = List.copyOf(planets);

        stars = List.copyOf(catalogue.stars());

        this.catalogue = catalogue;

        moon = MoonModel.MOON.at(daysUntil, conversion);
    }

    public Sun sun(){ return sun; }

    //TODO : getter pour  stars et planets ?
    public  List<Planet> planets() { return planets; }

    public List<Star> stars() { return stars; }

    public Moon moon() { return moon; }

    //TODO : looks weird.
    public CartesianCoordinates sunPosition() {
        return CartesianCoordinates.of(sun.eclipticPos().lon(), sun.eclipticPos().lat());
    }

    public CartesianCoordinates moonPosition() {
        return CartesianCoordinates.of(moon.equatorialPos().ra(),moon.equatorialPos().dec());
    }

    public double[] planetPositions() {
        double[] positions = new double[planets.size()*2];
        int j = 0;
        for(int i = 0; i < planets.size(); i++){
            positions[j] = planets.get(i).equatorialPos().ra();
            positions[j+1] = planets.get(i).equatorialPos().dec();
            j+=2;
        }
        return positions;
    }

    public double[] starPositions() {
        double[] positions = new double[stars.size()*2];
        int j = 0;
        for(int i = 0; i < stars.size(); i++){
            positions[j] = stars.get(i).equatorialPos().ra();
            positions[j+1] = stars.get(i).equatorialPos().dec();
            j+=2;
        }
        return positions;
    }

    //TODO : this method or the one from the builder ?
    public Set<Asterism> asterisms() {
        return catalogue.asterisms();
    }

    public List<Integer> asterismIndices(Asterism asterism) { return catalogue.asterismIndices(asterism); }

    //TODO: objectClosestTo
    //   - Quel type de coord?

    public CelestialObject objectClosestTo(GeographicCoordinates coordinates, double distance) {


        return null;
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
