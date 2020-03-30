package ch.epfl.astronomy;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MyAsterismTest {

    @Test
    void failsOnEmptyStarLis() {
        var starList = new ArrayList<Star>();
        assertThrows(IllegalArgumentException.class, () -> {
                    new Asterism(starList);
                }
        );
    }

    @Test
    void getterWorks() {
        var starList = new ArrayList<Star>();
        starList.add(new Star(1, "Star", EquatorialCoordinates.of(0, 0), 0f, 0f));
        starList.add(new Star(2, "Sta2r", EquatorialCoordinates.of(1, 0), 1f, 0f));
        starList.add(new Star(2, "Sta2r", EquatorialCoordinates.of(1, 0), 1f, 0f));
        var asterism = new Asterism(starList);

        assertEquals(starList, asterism.stars());
    }


}
