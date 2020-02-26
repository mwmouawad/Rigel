package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public class HorizontalCoordinatesTest {

    @Test
    void gettersTest(){
        HorizontalCoordinates a = HorizontalCoordinates.ofDeg(6.5682, 46.5183);
        assertEquals(6.5682, a.lonDeg());
        assertEquals(46.5183, a.latDeg());
        assertEquals(Angle.ofDeg(6.5682), a.lon());
        assertEquals(Angle.ofDeg(46.5183), a.lat());

    }

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
    void testInvalidHorizontalCoordinates(){
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.ofDeg(-1, 10);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.ofDeg(0, 91);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.of(-5, 0);
        });
    }

    @Test
    void testOctantName(){

        var coord1 = HorizontalCoordinates.ofDeg(335,0);
        assertEquals("NO", coord1.azOctantName("N", "E", "S", "O"));

        var coord2 = HorizontalCoordinates.ofDeg(0,0);
        assertEquals("N", coord2.azOctantName("N", "E", "S", "O"));

        var coord3 = HorizontalCoordinates.ofDeg(155,0);
        assertEquals("SE", coord3.azOctantName("N", "E", "S", "O"));

        var coord5 = HorizontalCoordinates.ofDeg(108, 0);
        assertEquals("E", coord5.azOctantName("N", "E", "S", "O"));

        var coord6 = HorizontalCoordinates.ofDeg(197, 0);
        assertEquals("S", coord6.azOctantName("N", "E", "S", "O"));

        var coord4 = HorizontalCoordinates.ofDeg(254,0);
        assertEquals("O", coord4.azOctantName("N", "E", "S", "O"));

        var coord7 = HorizontalCoordinates.ofDeg(203.45,0);
        assertEquals("SO", coord7.azOctantName("N", "E", "S", "O"));

    }

    @Test
    void equalsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var horCoord = HorizontalCoordinates.ofDeg(350, 7.2);
            horCoord.equals(horCoord);
        });
    }

    @Test
    void hashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            HorizontalCoordinates.ofDeg(350,7.2).hashCode();
        });
    }

}
