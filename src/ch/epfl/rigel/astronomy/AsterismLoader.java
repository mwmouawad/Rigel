package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.rigel.astronomy.HygDatabaseLoader.COLUMNS;


public enum AsterismLoader implements StarCatalogue.Loader {
    INSTANCE;


    /**
     * Loads asterisms from input file in the input stream.
     * @param inputStream inputstream associated with the asterisms file.
     * @param builder   star catalogue builder with stars loaded.
     * @throws IOException
     */
    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {

        InputStreamReader inStrReader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        BufferedReader buffReader = new BufferedReader(inStrReader);

        String line = buffReader.readLine();
        String[] charTable;
        List<Star> starList = new ArrayList<Star>();
        Asterism asterism;

        while(line != null){
            starList.clear();
            System.out.println("Adding asterism");
            charTable = line.split(",");
            for(String s: charTable){

                Star star = getStarFromCatalogue(Integer.parseInt(s), builder.stars());
                starList.add(star);

            }

            builder.addAsterism( new Asterism(starList));
            System.out.println( "Asterism added " + starList );
            line = buffReader.readLine();


        }


    }


    /**
     *
     * @param id star id
     * @param starCatalogue catalogue of stars
     * @return returns the object star from the catalogue
     */
    public static Star getStarFromCatalogue(int id, List<Star> starCatalogue){

        for(Star s: starCatalogue){
            if(s.hipparcosId() == id){
                return s;
            }
        }
        return null;

    }


}
