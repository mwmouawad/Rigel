package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.structure.TrieableObject;

import java.time.ZoneId;

/**
 * Represents a City object based on the geonames data model.
 * Extends TrieableObject to be used with a Trie.
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


    /**
     * Constructs a city instance.
     * @param id the geoname id of the city.
     * @param name the unicode name of the city.
     * @param nameASCII the ascii name of the city.
     * @param country the country code of the city.
     * @param geoCoordinates the geographic coordinates of the city.
     * @param zoneId the zone id of the city.
     */
    public City(int id, String name,String nameASCII, String country, GeographicCoordinates geoCoordinates, ZoneId zoneId){
        this.id = id;
        this.name = name;
        this.nameASCII = nameASCII;
        this.country = country;
        this.geoCoordinates = geoCoordinates;
        this.zoneId = zoneId;
    }

    /**
     * Gets the geoname id of the city
     * @return the geoname id of the city.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name ASCII of the city.
     * @return ASCII name of the city.
     */
    public String getNameASCII() {
        return nameASCII;
    }

    /**
     * Gets Zone ID of the city.
     * @return the zone id of the city.
     */
    public ZoneId getZoneId() {
        return zoneId;
    }

    /**
     * Gets the country code.
     * @return the country code.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Gets the name of the city.
     * @return the name of the city.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the GeographicCoordinates of the city.
     * @return the GeographicCoordinates of the city.
     */
    public GeographicCoordinates getGeoCoordinates() {
        return geoCoordinates;
    }

    /**
     * The name of the city following the country code.
     * @return name of the city following the country code.
     */
    @Override
    public String toString() {
        return this.getName() + ", " + this.country;
    }

    /**
     * The name of the city following the country code.
     * @return name of the city following the country code.
     */
    @Override
    public String getNameTrieable() {
        return this.toString();
    }
}

