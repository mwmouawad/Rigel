package ch.epfl.astronomy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

public class CartesianCoordinatesTest {


    @Test
    void cartesianReturnExpectedValues(){

        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var  coord1 = rng.nextDouble(-1000000000.0,1000000000.0 );
            var  coord2 = rng.nextDouble(-1000000000.0,1000000000.0 );
            var coordinates = CartesianCoordinates.of(coord1, coord2);

            assertEquals(coord1, coordinates.x());
            assertEquals(coord2, coordinates.y());

        }

    }


    @Test
    void hashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            CartesianCoordinates.of(1,-1).hashCode();
        });
    }

    @Test
    void equalsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {

            var cart = CartesianCoordinates.of(1,-1);
            cart.equals(cart);
        });
    }

}
