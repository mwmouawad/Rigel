package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ch.epfl.rigel.astronomy.HygDatabaseLoader.COLUMNS;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.gui.CityCatalogue;
import javafx.beans.property.StringProperty;


/**
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public enum CitiesLoader implements CityCatalogue.Loader {
    INSTANCE;

    static private int ID_COLUMN = 0;
    static private int NAME_COLUMN = 1;
    static private int ASCII_NAME_COLUMN = 2;
    static private int COUNTRY_CODE_COLUMN = 8;
    static private int LAT_COLUMN = 4;
    static private int LON_COLUMN = 5;
    static private int ZONE_ID_COLUMN = 17;



    /**
     * Loads asterisms from input file in into a star Catalogue Builder.
     *
     * @param inputStream inputstream associated with the asterisms file.
     * @param builder     star catalogue builder with stars loaded.
     * @throws IOException
     */
    @Override
    public void load(InputStream inputStream, CityCatalogue.Builder builder) throws IOException {

        try(
                InputStreamReader inStrReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader buffReader = new BufferedReader(inStrReader);
        ){
            String line = buffReader.readLine();
            String[] charTable;

            int i = 0;

            while (line != null) {
                charTable = line.split("\t");
                City newCity = new City(
                        Integer.parseInt(charTable[ID_COLUMN]),
                        charTable[NAME_COLUMN],
                        charTable[ASCII_NAME_COLUMN],
                        charTable[COUNTRY_CODE_COLUMN],
                        GeographicCoordinates.ofDeg(Double.parseDouble(charTable[LON_COLUMN]), Double.parseDouble(charTable[LAT_COLUMN])),
                        ZoneId.of(charTable[ZONE_ID_COLUMN])
                );
                builder.addCity(newCity);
                line = buffReader.readLine();
                i++;
            }


            }
    }





    /**
     * Helper method to get Star from the star catalogue.
     *
     * @param id            star id
     * @param starCatalogue catalogue of stars
     * @return returns the object star from the catalogue
     */
    private static Star getStarFromCatalogue(int id, List<Star> starCatalogue) {
        for (Star s : starCatalogue) {
            if (s.hipparcosId() == id) {
                return s;
            }
        }
        return null;
    }


}
