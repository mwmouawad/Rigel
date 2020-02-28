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
    void gettersWorksOnKnownValues(){

        var coord1 = EquatorialCoordinates.of(Angle.ofHr(4.0), Angle.ofDeg(55.3));
        assertEquals(Angle.ofHr(4),coord1.ra());
        //TODO: Find out why conversion not working past 1e-15.
        assertEquals(4.0,coord1.raHr(), 1e-15);
        assertEquals(55.3,coord1.decDeg());
        assertEquals(Angle.toDeg(Angle.ofHr(4.0)), coord1.raDeg());

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
            EquatorialCoordinates.of(0, -Math.PI);
        });

    }

    @Test
    void equalsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var coord1 = EquatorialCoordinates.of(Angle.ofHr(1.5), Angle.ofDeg(45));
            coord1.equals(coord1);
        });
    }

    @Test
    void hashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            EquatorialCoordinates.of(1,1).hashCode();
        });
    }


}
