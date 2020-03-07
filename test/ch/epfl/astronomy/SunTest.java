package ch.epfl.astronomy;
import ch.epfl.rigel.astronomy.Sun;
import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class SunTest{

    @Test
    void sunConstructorFailsWithNullPosition(){
            assertThrows(NullPointerException.class, () -> {
                new Sun(null, EquatorialCoordinates.of(0,0), 10, 0);
            });
    }


    @Test
    void nameReturnsSoleil(){
        var sun = new Sun(EclipticCoordinates.of(0,0), EquatorialCoordinates.of(0,0), 10, 0);
        assertEquals("Soleil", sun.name());
    }

    @Test
    void magnitudeReturnsExpectedValue(){
        var sun = new Sun(EclipticCoordinates.of(0,0), EquatorialCoordinates.of(0,0), 10, 0);
        //TODO: Getter is casting the value to double. Is it okay like this?
        assertEquals((double)-26.7f, sun.magnitude());
    }

    @Test
    void gettersReturnExpectedValues(){

        var sun = new Sun(EclipticCoordinates.of(1.25,-1.235), EquatorialCoordinates.of(0,0), 10, 0);
        assertEquals(1.25,sun.eclipticPos().lon());
        assertEquals(-1.235,sun.eclipticPos().lat());
        assertEquals(0, sun.meanAnomaly());

        var sun2 = new Sun(EclipticCoordinates.of(0.8989,-0.8989), EquatorialCoordinates.of(0,0), 10, 11.123f);
        assertEquals(0.8989,sun2.eclipticPos().lon());
        assertEquals(-0.8989,sun2.eclipticPos().lat());
        assertEquals((double)(11.123f),sun2.meanAnomaly());


    }






}
