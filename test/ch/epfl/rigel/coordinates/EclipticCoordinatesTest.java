package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EclipticCoordinatesTest {

    @Test
    void gettersWorksOnKnownValues(){

        var coord1 = EclipticCoordinates.of(2.4, -0.9);
        assertEquals(coord1.latDeg(), Angle.toDeg(-0.9));
        assertEquals(coord1.lat(), -0.9);
        assertEquals(coord1.lon(), 2.4);
        assertEquals(coord1.lonDeg(), Angle.toDeg(2.4));

    }


    @Test
    void toStringWorksOnKnownCoordinates() {
        var coord1 = EclipticCoordinates.of(Angle.ofDeg(22.5), Angle.ofDeg(18.0) );
        assertEquals("(λ=22.5000°, β=18.0000°)", coord1.toString());
    }

    @Test
    void failsOnInvalidValues(){

        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(-5, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(Angle.TAU + 5, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(0, Angle.ofDeg(-91));
        });

    }

    @Test
    void equalsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var coord1 = EquatorialCoordinates.of(2.4, -0.9);
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
