package ch.epfl.astronomy;
import ch.epfl.rigel.astronomy.Moon;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class MyMoonTest {

    @Test
    void moonConstructorFailsWithInvalidPhase(){
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var phase = (float)rng.nextDouble(-5,5);
            if(phase >= 0 && phase <=1){
                continue;
            }
            assertThrows(IllegalArgumentException.class, () -> {
                new Moon(EquatorialCoordinates.of(0,0), 10, 0, phase);
            });
        }
    }


    @Test
    void nameReturnsLune(){
        var moon = new Moon(EquatorialCoordinates.of(0,0), 10, 0, 0.5f);
        assertEquals("Lune", moon.name());
    }

    @Test
    void infoReturnsExpectedString(){
        var moon = new Moon(EquatorialCoordinates.of(0,0), 10, 0, 0.3752f);
        assertEquals("Lune (37.5%)", moon.info());
    }


}
