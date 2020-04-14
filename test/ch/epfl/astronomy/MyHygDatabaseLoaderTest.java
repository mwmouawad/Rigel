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

public class MyHygDatabaseLoaderTest {

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



    @Test
    void HygDatabaseLoadTest() throws IOException{
        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
            StarCatalogue catalogue = new StarCatalogue.Builder().loadFrom(hygStream, HygDatabaseLoader.INSTANCE).build();
            Star rigel = catalogue.stars().get(1019);
            Star bellatrix = catalogue.stars().get(1068);
            Star betelgeuse = catalogue.stars().get(1213);
            Star nameEmpty = catalogue.stars().get(1212);
            Star nameEmpty2 = catalogue.stars().get(1208);
            Star ciEmpty = catalogue.stars().get(5041);
            //hipparcosId
            assertEquals(24436, rigel.hipparcosId());
            assertEquals(25336, bellatrix.hipparcosId());
            //colorTemperature
            assertEquals(10500, rigel.colorTemperature(), 1e2);
            assertEquals(3800, betelgeuse.colorTemperature(), 1e2);
            //names from PROPER
            assertEquals("Rigel", rigel.name());
            assertEquals("Bellatrix", bellatrix.name());
            //name without PROPER and without BAYER
            assertEquals("? Aur", nameEmpty.name());
            //name without PROPER but with BAYER
            assertEquals("Xi Aur", nameEmpty2.name());
            //check of colorTemperature without CI
            assertEquals((int)(4600*(1/1.7 + 1/0.62)), ciEmpty.colorTemperature());
            //magnitudes (couldn't find any empty magnitude)
            assertEquals(0.18, rigel.magnitude(), 1e-6);
            assertEquals(0.45, betelgeuse.magnitude(), 1e-6);
            //equatorial coordinates (in radians)
            assertEquals(1.3724303693276385, rigel.equatorialPos().ra());
            assertEquals(-0.143145630755865, rigel.equatorialPos().dec());
            //hipparcosId without HIP
            assertEquals(0, ciEmpty.hipparcosId());
        }
    }

}
