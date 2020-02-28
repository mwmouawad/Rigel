package ch.epfl.rigel.coordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public class GeographicCoordinatesTest {

    @Test
    void gettersTest(){
        GeographicCoordinates a = GeographicCoordinates.ofDeg(100,90);
        assertEquals(100, a.lonDeg());
        assertEquals(90, a.latDeg());
        assertEquals(Angle.ofDeg(100), a.lon());
        assertEquals(Angle.ofDeg(90), a.lat());

    }

    @Test
    void ofDegWorksWithValidValues() {
        GeographicCoordinates a = GeographicCoordinates.ofDeg(100,90);
        GeographicCoordinates b = GeographicCoordinates.ofDeg(-180,-90);
    }

    @Test
    void ofDegFailsWithInValidValues() {
        assertThrows(IllegalArgumentException.class, () -> {
            GeographicCoordinates.ofDeg(180, 0 );
        });
    }


    @Test
    void toStringWorksOnKnownCoordinates() {
        var coord1 = GeographicCoordinates.ofDeg(6.57, 46.52);
        assertEquals("(lon=6.5700°, lat=46.5200°)", coord1.toString());
    }


}
