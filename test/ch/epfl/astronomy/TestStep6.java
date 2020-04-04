package ch.epfl.astronomy;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestStep6 {

    private static final String HYG_CATALOGUE_NAME =
            "/hygdata_v3.csv";
    @Test
    void hygDatabaseIsCorrectlyInstalled() throws IOException {
        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
            assertNotNull(hygStream);
        }
    }

    @Test
    void hygDatabaseContainsRigel() throws IOException {
        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
            StarCatalogue catalogue = new StarCatalogue.Builder().loadFrom(hygStream, HygDatabaseLoader.INSTANCE).build();
            Star rigel = null;
            for (Star s : catalogue.stars()) {
                if (s.name().equalsIgnoreCase("rigel")) rigel = s;
            }
            assertNotNull(rigel);
        }
    }

    @Test
    void loadCorrectly() throws IOException {
        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
            StarCatalogue.Builder builder = new StarCatalogue.Builder();
            HygDatabaseLoader.INSTANCE.load(hygStream, builder);

            int i = 0;
            for(Star star : builder.stars()) {
                if (star.name().charAt(0) == '?') {
                    i = 1;
                    assertEquals(' ', star.name().charAt(1));}
            }
            assertEquals(1,i);
        }
    }

    private static final String ASTERISM_CATALOGUE_NAME =
            "/asterisms.txt";

    @Test
    void variousTestsAndReadablePrintfOnCompletelyFinishedStarCatalogue() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            InputStream asterismStream = getClass()
                    .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE).loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();
            Star rigel = null;
            for (Star s : catalogue.stars()) {
                if (s.name().equalsIgnoreCase("rigel"))
                    rigel = s;
            }
            assertNotNull(rigel);

            List<Star> allStar = new ArrayList<Star>();
            allStar.addAll(catalogue.stars());

            System.out.println("LIST OF STARS :");
            for (Star s : allStar) {
                System.out.printf("%6d ", s.hipparcosId());
            } //should print out the same star IDS as in the fichier (check visually)
            System.out.println();
            System.out.println();

            System.out.println("ASTERISMS : ");
            int i;

            //vérifier visuellement en utilisant CTRL-F que les astérismes contenu dans ASTERISMS sont bien les memes
            //flemme de coder une méthode qui vérifie automatiquement
            for (Asterism asterism : catalogue.asterisms()) {
                List<Integer> cAstInd = catalogue.asterismIndices(asterism);
                i = 0;
                for (Star star : asterism.stars()) {
                    System.out.print("Hip : ");
                    System.out.printf("%6d", star.hipparcosId());
                    System.out.print("  foundHipparcos : ");
                    System.out.printf("%6d", allStar.get(cAstInd.get(i)).hipparcosId());

                /*TEST : l'index stoqué dans asterismIndices renvoie le meme hipparcosId que
                l'index stoqué dans l'astérisme voulu : */
                    assertEquals(allStar.get(cAstInd.get(i)).hipparcosId(), star.hipparcosId());
                    System.out.print(" ||| ");
                    i++;
                }
                System.out.println();
            }
        }
    }

    @Test
    void correctNbOfIndex() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            InputStream asterismStream = getClass()
                    .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE).loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();
            for (Asterism asterism : catalogue.asterisms()) {
                int nbIndices = catalogue.asterismIndices(asterism).size();
                int nbStars = asterism.stars().size();
                assertEquals(nbIndices, nbStars);
            }
        }
    }

    private static final String ASTERISM_FILE_NAME = "/asterisms.txt";

    @Test
    void loadCorrectly2() throws IOException {

        StarCatalogue.Builder catalogueBuilder;
        StarCatalogue catalogue;
        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
            catalogueBuilder = new StarCatalogue.Builder().loadFrom(hygStream, HygDatabaseLoader.INSTANCE);

            try (InputStream asterismStream = getClass().getResourceAsStream(ASTERISM_FILE_NAME)) {
                catalogue = catalogueBuilder.loadFrom(asterismStream, AsterismLoader.INSTANCE).build();
            }

            Queue<Asterism> a = new ArrayDeque<>();
            Star beltegeuse = null;
            for (Asterism ast : catalogue.asterisms()) {
                for (Star s : ast.stars()) {
                    if (s.name().equalsIgnoreCase("Rigel")) {
                        a.add(ast);
                    }
                }
            }
            int astCount = 0;
            for (Asterism ast : a) {
                ++astCount;
                for (Star s : ast.stars()) {
                    if (s.name().equalsIgnoreCase("Betelgeuse")) {
                        beltegeuse = s;
                    }
                }
            }
            assertNotNull(beltegeuse);
            assertEquals(2, astCount);
        }
    }

    @Test
    public void coordinatesAreCorrect() {
        assertEquals(14.211456457836, MoonModel.MOON.at(-2313, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003,  Month.SEPTEMBER, 1),LocalTime.of(0,0), ZoneOffset.UTC))).equatorialPos().raHr(),1e-8);
        assertEquals(-0.20114171346019355, MoonModel.MOON.at(-2313, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003,  Month.SEPTEMBER, 1),LocalTime.of(0,0), ZoneOffset.UTC))).equatorialPos().dec(), 1e-8);
    }

    @Test
    public void angularSizeIsCorrect() {
        assertEquals(0.009225908666849136 , MoonModel.MOON.at(Epoch.J2010.daysUntil(ZonedDateTime.of(LocalDate.of(1979, 9, 1),LocalTime.of(0, 0),
                ZoneOffset.UTC)), new EclipticToEquatorialConversion(ZonedDateTime.of(
                LocalDate.of(1979, 9, 1), LocalTime.of(0, 0), ZoneOffset.UTC))).
                angularSize(), 1e-8);
    }

    @Test
    public void phaseIsCorrect() {
        assertEquals("Lune (22.5%)" , MoonModel.MOON.at(Epoch.J2010.daysUntil(ZonedDateTime.of(LocalDate.of(2003, 9, 1),LocalTime.of(0, 0),
                ZoneOffset.UTC)), new EclipticToEquatorialConversion(ZonedDateTime.of( LocalDate.of(2003, 9, 1),
                LocalTime.of(0, 0),ZoneOffset.UTC))).info());
    }


}


