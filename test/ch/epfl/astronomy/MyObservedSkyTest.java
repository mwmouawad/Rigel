package ch.epfl.astronomy;
import ch.epfl.rigel.astronomy.ObservedSky;
import org.junit.jupiter.api.Test;

public class MyObservedSkyTest {

    @Test
    void constructorWorksAsExpected(){

        var obsSky = new ObservedSky(null, null, null, null);
    }
}
