package ch.epfl.rigel.coordinates;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public class GeographicCoordinatesTest {

    @Test
    void ofDegWorksWithValidValues() {
        GeographicCoordinates a = GeographicCoordinates.ofDeg(100,90);
        GeographicCoordinates b = GeographicCoordinates.ofDeg(-180,-90);
    }

    @Test
    void ofDegFailsWithValidValues() {
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
