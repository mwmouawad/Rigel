package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.structure.TrieableObject;

import java.time.ZoneId;

/**
 * Represents a City object based on the geonames data model.
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class City extends TrieableObject {

    private final int id;
    private final String name;
    private final String nameASCII;
    private final String country;
    private final GeographicCoordinates geoCoordinates;
    private final ZoneId zoneId;


    public City(int id, String name,String nameASCII, String country, GeographicCoordinates geoCoordinates, ZoneId zoneId){
        this.id = id;
        this.name = name;
        this.nameASCII = nameASCII;
        this.country = country;
        this.geoCoordinates = geoCoordinates;
        this.zoneId = zoneId;
    }

    public int getId() {
        return id;
    }

    public String getNameASCII() {
        return nameASCII;
    }

    public ZoneId getZoneId() {
        return zoneId;
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public GeographicCoordinates getGeoCoordinates() {
        return geoCoordinates;
    }

    @Override
    public String toString() {
        return this.getName() + ", " + this.country;
    }

    @Override
    public String getNameTrieable() {
        return this.toString();
    }
}

