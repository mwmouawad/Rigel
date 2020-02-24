package ch.epfl.rigel.coordinates;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HorizontalCoordinatesTest {

    @Test
    void testAngularDistanceTo(){
        HorizontalCoordinates EPFLCoord = HorizontalCoordinates.ofDeg(6.5682, 46.5183);
        HorizontalCoordinates EPFZCoord = HorizontalCoordinates.ofDeg(8.5476, 47.3763);
        assertEquals(0.0279, EPFLCoord.angularDistanceTo(EPFZCoord), 1e-4);
    }

    @Test
    void toStringWorksOnKnownCoordinates() {
        var coord1 = HorizontalCoordinates.ofDeg(350, 7.2);
        assertEquals("(az=350.0000°, alt=7.2000°)", coord1.toString());
    }

    @Test
    void testOctantName(){

        var coord1 = HorizontalCoordinates.ofDeg(335,0);
        assertEquals("NO", coord1.azOctantName("N", "E", "S", "O"));

    }

}
