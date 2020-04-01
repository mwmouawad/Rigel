package ch.epfl.astronomy;

import ch.epfl.rigel.astronomy.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MyAsterismLoaderTest {

    private static final String HYG_CATALOGUE_NAME = "/hygdata_v3.csv";
    private static final String ASTERISMS_CATALOGUE_NAME = "/asterisms.txt";



    @Test
    void asterismsContainsRigel() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {

            InputStream astStream = getClass().getResourceAsStream(ASTERISMS_CATALOGUE_NAME);
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .loadFrom(astStream, AsterismLoader.INSTANCE)
                    .build();
            Star rigel = null;
            Asterism asterism = null;


            //Get First Rigel Asterism
            for(Asterism ast: catalogue.asterisms()){
                for(Star s: ast.stars()){
                    if(s.name().equalsIgnoreCase("rigel")){
                        rigel = s;
                        asterism = ast;
                        break;
                    }
                    break;
                }

            }

            assertNotNull(rigel);
            assertNotNull(asterism);
            //Check if all stars are stored from of line 95 are stored.
            if(asterism.stars().size() > 5){
                assertEquals(24436,asterism.stars().get(0).hipparcosId());
                assertEquals(27366,asterism.stars().get(1).hipparcosId());
                assertEquals(26727,asterism.stars().get(2).hipparcosId());
                assertEquals(27989,asterism.stars().get(3).hipparcosId());
                assertEquals(28614,asterism.stars().get(4).hipparcosId());
                assertEquals(29426,asterism.stars().get(5).hipparcosId());
                assertEquals(28716,asterism.stars().get(6).hipparcosId());

                //Check if asterism indices are working.
                List<Integer> resultIndices = catalogue.asterismIndices(asterism);

                assertEquals(1019, resultIndices.get(0));
                assertEquals(1173, resultIndices.get(1));

            }

        }
    }

    @Test
    void asterismDatabaseIsCorrectlyInstalled() throws IOException {
        try (InputStream asterismStream = getClass()
                .getResourceAsStream(ASTERISMS_CATALOGUE_NAME)) {
            assertNotNull(asterismStream);
        }
    }

}
