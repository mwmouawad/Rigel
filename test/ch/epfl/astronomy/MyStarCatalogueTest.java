package ch.epfl.astronomy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.Moon;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.*;

public class MyStarCatalogueTest {

    private static Star star1 = new Star(1, "Star", EquatorialCoordinates.of(0,0), 0.32f,0.01f);
    private static Star star2= new Star(2, "Star2", EquatorialCoordinates.of(0,0), 0.2f,0.0321f);
    private static Star star3 = new Star(3, "Star3", EquatorialCoordinates.of(0,0.5), 0.43f,0.0312f);
    private static Star star4 = new Star(4, "Star4", EquatorialCoordinates.of(0.5,0.2), 0.432f,0.03122f);
    private static Star star5 =  new Star(5, "Star5", EquatorialCoordinates.of(0.7,0.8), 2f,3.5f);


    private static List<Star> createStarCollection(){

        var starCatalogue = new ArrayList<Star>();
        starCatalogue.add(star1);
        starCatalogue.add(star2);
        starCatalogue.add(star3);
        starCatalogue.add(star4);

        return starCatalogue;

    }

    @Test
    void constructorThrowsIllegalArg(){
        var stars = createStarCollection();

        var starList1 = Arrays.asList(star1, star2);
        var starList2 = Arrays.asList(star3, star4);
        var starList3 = Arrays.asList(star2, star4, star5);

        Asterism ast1 = new Asterism(starList1, "Dummy");
        Asterism ast2 =  new Asterism(starList2, "Dummy");
        Asterism ast3 =  new Asterism(starList3, "dummy");
        List<Asterism> astList = Arrays.asList( ast1,ast2,ast3 );

        assertThrows(IllegalArgumentException.class, () -> {
            new StarCatalogue(stars, astList);
        });


    }

    @Test
    void starListWorks(){

        var starCollection = createStarCollection();
        List<Asterism> astList = new ArrayList<Asterism>();
        var starCatalogue = new StarCatalogue(starCollection, astList);

        assertEquals(starCollection, starCatalogue.stars());


    }


    @Test
    void asterismsWork(){
        var stars = createStarCollection();

        var starList1 = Arrays.asList(star1, star2);
        var starList2 = Arrays.asList(star3, star4);
        var starList3 = Arrays.asList(star2, star4);

        Asterism ast1 = new Asterism(starList1, "Dummy");
        Asterism ast2 =  new Asterism(starList2, "Dummy");
        Asterism ast3 =  new Asterism(starList3, "dummy");
        List<Asterism> astList = Arrays.asList( ast1,ast2,ast3 );

        var starCatalogue = new StarCatalogue(stars, astList);

        Set<Asterism>  astSet = Collections.unmodifiableSet(new HashSet<Asterism>(astList));

        var asterism = starCatalogue.asterisms();


        for(var i =0; i < 3; i++){
            var contains = false;
            for(Asterism ast: starCatalogue.asterisms()){
                if(ast.stars().equals( astList.get(i).stars())){
                    contains = true;
                }
            }
            assertEquals(true, contains);
        }



    }


    @Test
    void asterimsIndicesWorks(){
        var stars = createStarCollection();

        var starList1 = Arrays.asList(star1, star2);
        var starList2 = Arrays.asList(star3, star4);
        var starList3 = Arrays.asList(star2, star4);

        Asterism ast1 = new Asterism(starList1, "Dummy");
        Asterism ast2 =  new Asterism(starList2, "Dummy");
        Asterism ast3 =  new Asterism(starList3, "dummy");
        List<Asterism> astList = Arrays.asList( ast1,ast2,ast3 );

        var starCatalogue = new StarCatalogue(stars, astList);

        var indices1 = starCatalogue.asterismIndices(ast1);
        assertEquals(0,  indices1.get(0));
        assertEquals(1,  indices1.get(1));

        var indices2 = starCatalogue.asterismIndices(ast2);
        assertEquals(2,  indices2.get(0));
        assertEquals(3,  indices2.get(1));

        var indices3 = starCatalogue.asterismIndices(ast3);
        assertEquals(1,  indices3.get(0));
        assertEquals(3,  indices3.get(1));



    }

}
