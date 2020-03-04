package ch.epfl.astronomy;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.coordinates.*;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class EquatorialToHorizontalConversionTest {

    /**
     */
    @Test
    void applyWorksOnKnownValues(){

        /**
         * Data used to find an hour angle as used in the book example.
         * Desired Hour Angle: 5.862221
         * Input data to obtain the hour angle:
         *  -> RightAsc = 4h 45m18.56
         *  -> Longitude = 0
         *  -> Zoned Date = 01/03/2020 0h0min0s UTC
         *
         *  Website used to get this input data: https://tinyurl.com/vuwlzpy
         */
        var time = ZonedDateTime.of(LocalDate.of(2020, Month.MARCH, 01),
                LocalTime.of(0,0,0), ZoneOffset.UTC);

        var obsPosition = GeographicCoordinates.ofDeg(0,52);
        var eqCoordinates = EquatorialCoordinates.of( Angle.ofHr(4 + 45.0/60 + 18.56 / 3600), Angle.ofDMS(23,13,10));

        EquatorialToHorizontalConversion eqToHorConv = new EquatorialToHorizontalConversion(time, obsPosition);

        var result = eqToHorConv.apply(eqCoordinates);


        assertEquals(Angle.ofDeg(283.271027), result.az(), 1e-7);
        assertEquals(Angle.ofDeg(19.334345), result.alt(), 1e-7);

    }


    @Test
    void hashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var obsPosition = GeographicCoordinates.ofDeg(0,52);
            var time = ZonedDateTime.of(LocalDate.of(2020, Month.MARCH, 01),
                    LocalTime.of(0,0,0), ZoneOffset.UTC);
            EquatorialToHorizontalConversion eqToHorConv = new EquatorialToHorizontalConversion(time, obsPosition);
            eqToHorConv.hashCode();
        });
    }

    @Test
    void equalsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var obsPosition = GeographicCoordinates.ofDeg(0,52);
            var time = ZonedDateTime.of(LocalDate.of(2020, Month.MARCH, 01),
                    LocalTime.of(0,0,0), ZoneOffset.UTC);
            EquatorialToHorizontalConversion eqToHorConv = new EquatorialToHorizontalConversion(time, obsPosition);
            eqToHorConv.equals(eqToHorConv);
        });
    }

}
