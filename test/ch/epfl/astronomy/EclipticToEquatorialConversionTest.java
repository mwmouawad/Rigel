package ch.epfl.astronomy;

import ch.epfl.rigel.coordinates.*;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EclipticToEquatorialConversionTest {

    /**
     * Test example used in the book page
     * 53.
     */
    @Test
    void conversionWorksOnKnownValues(){


        var eclipticCoordinates = EclipticCoordinates.of(
                Angle.ofDMS(139, 41,10),
                Angle.ofDMS(4,52,31)
                );
        var time = ZonedDateTime.of(LocalDate.of(2009, Month.JULY, 6),
                LocalTime.of(0, 0), ZoneOffset.UTC);

        EclipticToEquatorialConversion eclToEqConversion = new EclipticToEquatorialConversion(time);

        //Assert Dec angle
        assertEquals(
                Angle.ofDMS(19, 32,6.01),
                eclToEqConversion.apply(eclipticCoordinates).dec(),
                1e-8
        );

        assertEquals(
                Angle.ofHr(9.581478),
                (eclToEqConversion.apply(eclipticCoordinates).ra()),
                1e-7
        );
    }


    @Test
    void hashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var time = ZonedDateTime.of(LocalDate.of(2009, Month.JULY, 6),
                    LocalTime.of(0, 0), ZoneOffset.UTC);

            EclipticToEquatorialConversion eclToEqConversion = new EclipticToEquatorialConversion(time);

            eclToEqConversion.hashCode();
        });
    }

    @Test
    void equalsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var time = ZonedDateTime.of(LocalDate.of(2009, Month.JULY, 6),
                    LocalTime.of(0, 0), ZoneOffset.UTC);

            EclipticToEquatorialConversion eclToEqConversion = new EclipticToEquatorialConversion(time);

            eclToEqConversion.equals(eclToEqConversion);
        });
    }

}

