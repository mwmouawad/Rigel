package ch.epfl.astronomy;
import ch.epfl.rigel.astronomy.Planet;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class PlanetTest {

    @Test
    void planetConstructorFailsWithInvalidAngularSize(){
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var angularsize = rng.nextInt(Integer.MIN_VALUE,0);
            assertThrows(IllegalArgumentException.class, () -> {
                new Planet("Name", EquatorialCoordinates.of(0,0), angularsize, 0);
            });
        }
    }

    @Test
    void gettersReturnExpectedValues(){
        var planet = new Planet("ko2813091", EquatorialCoordinates.of(3,-1.23), 25, -16);
        assertEquals("ko2813091",planet.name());
        assertEquals(3,planet.equatorialPos().ra());
        assertEquals(-1.23,planet.equatorialPos().dec());
        assertEquals(25,planet.angularSize());
        assertEquals(-16,planet.magnitude());

        var planet2 = new Planet("1231231", EquatorialCoordinates.of(0,1.5), 1, 15);
        assertEquals("1231231",planet2.name());
        assertEquals(0,planet2.equatorialPos().ra());
        assertEquals(1.5,planet2.equatorialPos().dec());
        assertEquals(1,planet2.angularSize());
        assertEquals(15,planet2.magnitude());
    }

    @Test
    void toStringReturnsSameAsInfo(){
        var planet = new Planet("ko2813091", EquatorialCoordinates.of(3,-1.23), 25, -16);
        assertEquals(planet.info(), planet.toString());
    }

    @Test
    void planetConstructorFailsWithNullName(){
        assertThrows(NullPointerException.class, () -> {
            new Planet(null, EquatorialCoordinates.of(0,0), 0, 10);
        });
    }

    @Test
    void planetConstructorFailsWithNullCoordinates(){
        assertThrows(NullPointerException.class, () -> {
            new Planet("Planet", null, 0, 10);
        });
    }


}
