package ch.epfl.astronomy;
import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.Moon;
import ch.epfl.rigel.astronomy.Sun;
import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
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

    @Test
    void moonTest(){
        Moon moon = new Moon(EquatorialCoordinates.of(Angle.ofDeg(55.8),
                Angle.ofDeg(19.7)), 37.5f, -1, 0.3752f);
        assertEquals("Lune", moon.name());
        assertEquals("Lune (37.5%)", moon.info());
        assertEquals(EquatorialCoordinates.of(Angle.ofDeg(55.8),
                Angle.ofDeg(19.7)).dec(), moon.equatorialPos().dec());
        assertEquals(EquatorialCoordinates.of(Angle.ofDeg(55.8),
                Angle.ofDeg(19.7)).ra(), moon.equatorialPos().ra()); //checking equatorial position
        assertThrows(IllegalArgumentException.class, () -> {new Moon(EquatorialCoordinates.of(Angle.ofDeg(55.8),
                Angle.ofDeg(19.7)), 37.5f, -1, -0.1f); });
    }

    @Test
    void sunTest(){

        //tests variés pour sun et moon
        Sun sun = new Sun(EclipticCoordinates.of(Angle.ofDeg(53), Angle.ofDeg(38)),
                EquatorialCoordinates.of(Angle.ofDeg(55.8),Angle.ofDeg(24)),
                0.4f, 5.f);
        assertEquals("Soleil", sun.info());
        assertEquals(EquatorialCoordinates.of(Angle.ofDeg(55.8),
                Angle.ofDeg(24)).dec(), sun.equatorialPos().dec());
        assertEquals(EquatorialCoordinates.of(Angle.ofDeg(55.8),
                Angle.ofDeg(19.7)).ra(), sun.equatorialPos().ra()); //checking equatorial position
        assertEquals(5.f, sun.meanAnomaly());
        assertEquals(-26.7f, sun.magnitude());

        //test pour eclipticPos throws un null
        assertThrows(NullPointerException.class, () -> { new Sun(null,
                EquatorialCoordinates.of(Angle.ofDeg(55.8),Angle.ofDeg(24)),
                0.4f, 5.f); });

    }







}
