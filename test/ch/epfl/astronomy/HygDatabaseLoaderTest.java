package ch.epfl.astronomy;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.astronomy.StarCatalogue;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class HygDatabaseLoaderTest {

    private static final String HYG_CATALOGUE_NAME = "/hygdata_v3.csv";


    //Not really a test for now needs more.
    @Test
    void testLoadWorks() throws IOException{

        try(InputStream i = new FileInputStream("resources/hygdata_v3.csv")){
            StarCatalogue.Builder builder = new StarCatalogue.Builder();
            HygDatabaseLoader.INSTANCE.load(i, builder);

        }


    }


    @Test
    void hygDatabaseIsCorrectlyInstalled() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            assertNotNull(hygStream);
        }
    }

    @Test
    void hygDatabaseContainsRigel() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .build();
            Star rigel = null;
            for (Star s : catalogue.stars()) {
                if (s.name().equalsIgnoreCase("rigel"))
                    rigel = s;

            }
            assertNotNull(rigel);
            assertEquals(24436, rigel.hipparcosId());
            assertEquals(0.180, rigel.magnitude(), 1e-6);
            assertEquals(1.3724303693276385, rigel.equatorialPos().ra());
            assertEquals(-0.143145630755865, rigel.equatorialPos().dec());

        }
    }

}
