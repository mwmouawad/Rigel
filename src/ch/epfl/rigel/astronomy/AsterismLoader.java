package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.rigel.astronomy.HygDatabaseLoader.COLUMNS;


/**
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public enum AsterismLoader implements StarCatalogue.Loader {
    INSTANCE;


    /**
     * Loads asterisms from input file in into a star Catalogue Builder.
     *
     * @param inputStream inputstream associated with the asterisms file.
     * @param builder     star catalogue builder with stars loaded.
     * @throws IOException
     */
    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {

        InputStreamReader inStrReader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        BufferedReader buffReader = new BufferedReader(inStrReader);

        String line = buffReader.readLine();
        String[] charTable;
        List<Star> starList = new ArrayList<Star>();

        while (line != null) {
            starList.clear();
            charTable = line.split(",");
            for (String s : charTable) {
                Star star = getStarFromCatalogue(Integer.parseInt(s), builder.stars());
                starList.add(star);
            }
            builder.addAsterism(new Asterism(starList));
            line = buffReader.readLine();

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
