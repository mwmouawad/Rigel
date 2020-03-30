package ch.epfl.astronomy;

import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class MyStarTest {

    @Test
    void starConstructorFailsNegativeHipparcos() {

        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var hipparcosId = rng.nextInt(-30, 0);
            assertThrows(IllegalArgumentException.class, () -> {
                new Star(hipparcosId, "Star", EquatorialCoordinates.of(0, 0), 0f, 0f);
            });
        }

    }

    @Test
    void starConstructorFailsOutOfBoundsColor() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var colorIndex = rng.nextDouble(-10, 10);
            if (ClosedInterval.of(-0.5, 5.5).contains(colorIndex)) {
                continue;
            }
            assertThrows(IllegalArgumentException.class, () -> {
                new Star(1, "Star", EquatorialCoordinates.of(0, 0), 0f, (float) colorIndex);
            });
        }

    }

    @Test
    void angularSizeIsZero() {
        var star = new Star(1, "Star", EquatorialCoordinates.of(0, 0), 0f, (float) 0);
        assertEquals(0, star.angularSize());
    }

    @Test
    void getHipparcosIdWorks() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var hipparcosId = rng.nextInt(0, 500);
            var star = new Star(hipparcosId, "Star", EquatorialCoordinates.of(0, 0), 0f, 0f);
            assertEquals(hipparcosId, star.hipparcosId());
        }
    }

    @Test
    void colorTemperatureWorksForKnownValues() {

        //Betelgeuse Example
        var betelgeuseStar = new Star(0, "Betelgeuse", EquatorialCoordinates.of(0, 0), 0f, 1.50f);
        assertEquals(3800, betelgeuseStar.colorTemperature(), 10);

        //Rigel Example
        var rigelStar = new Star(0, "Rigel", EquatorialCoordinates.of(0, 0), 0f, -0.03f);
        assertEquals(10500, rigelStar.colorTemperature(), 15);

    }


}
