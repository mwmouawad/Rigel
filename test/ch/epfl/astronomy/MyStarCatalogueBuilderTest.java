package ch.epfl.astronomy;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MyStarCatalogueBuilderTest {

    private static Star star1 = new Star(1, "Star", EquatorialCoordinates.of(0,0), 0.32f,0.01f);
    private static Star star2= new Star(2, "Star2", EquatorialCoordinates.of(0,0), 0.2f,0.0321f);
    private static Star star3 = new Star(3, "Star3", EquatorialCoordinates.of(0,0.5), 0.43f,0.0312f);
    private static Star star4 = new Star(4, "Star4", EquatorialCoordinates.of(0.5,0.2), 0.432f,0.03122f);
    private static Star star5 =  new Star(5, "Star5", EquatorialCoordinates.of(0.7,0.8), 2f,3.5f);


    @Test
    void starsListViewWorks(){

        var starBuilder = new StarCatalogue.Builder();
        starBuilder.addStar(star1).addStar(star2).addStar(star3);
        List<Star> starList = Arrays.asList(star1,star2,star3);

        assertEquals(starList, starBuilder.stars());

        assertThrows(UnsupportedOperationException.class, () -> {
            starBuilder.stars().add(star3);
        });


    }

    @Test
    void asterismViewWorks(){

        var starBuilder = new StarCatalogue.Builder();
        var starList1 = Arrays.asList(star1, star2);
        var starList2 = Arrays.asList(star3, star4);
        var starList3 = Arrays.asList(star2, star4, star5);

        Asterism ast1 = new Asterism(starList1);
        Asterism ast2 =  new Asterism(starList2);
        Asterism ast3 =  new Asterism(starList3);

        starBuilder.addAsterism(ast1).addAsterism(ast2).addAsterism(ast3);

        List<Asterism> astView = Arrays.asList(ast1,ast2,ast3);

        assertEquals(astView,starBuilder.asterisms());

        assertThrows(UnsupportedOperationException.class, () -> {
            starBuilder.asterisms().add(ast1);
        });
    }

    @Test
    void builderWorks(){
        var starBuilder = new StarCatalogue.Builder();
        var starList1 = Arrays.asList(star1, star2);
        var starList2 = Arrays.asList(star3, star4);
        var starList3 = Arrays.asList(star2, star4, star5);
        var stars = Arrays.asList(star1,star2,star3,star4,star5);

        Asterism ast1 = new Asterism(starList1);
        Asterism ast2 =  new Asterism(starList2);
        Asterism ast3 =  new Asterism(starList3);

        starBuilder.addAsterism(ast1).addAsterism(ast2).addAsterism(ast3);
        starBuilder.addStar(star1).addStar(star2).addStar(star3).addStar(star4).addStar(star5);
        List<Asterism> astView = Arrays.asList(ast1,ast2,ast3);

        var builtStarCatalogue = starBuilder.build();
        var expectedStarCatalogue = new StarCatalogue(stars, astView);

        assertEquals(expectedStarCatalogue.stars(), builtStarCatalogue.stars());
        assertEquals(expectedStarCatalogue.asterisms(), builtStarCatalogue.asterisms());

    }

    @Test
    void loaderWorks(){
    }
}
