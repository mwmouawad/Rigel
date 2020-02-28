package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class EquatorialCoordinatesTest {


    @Test
    void toStringWorksOnKnownCoordinates() {
        var coord1 = EquatorialCoordinates.of(Angle.ofHr(1.5), Angle.ofDeg(45) );
        assertEquals("(ra=1.5000h, dec=45.0000°)", coord1.toString());
    }

    @Test
    void failsOnInvalidValues(){

        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(Angle.ofHr(-1.4), 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(Angle.ofHr(24), 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(0, -91);
        });

    }


}
