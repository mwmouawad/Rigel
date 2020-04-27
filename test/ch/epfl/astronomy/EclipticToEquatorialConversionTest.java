package ch.epfl.astronomy;

import ch.epfl.rigel.coordinates.*;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.function.Function;

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
        
        EclipticCoordinates a = EclipticCoordinates.of(Angle.ofDeg(139.686111), Angle.ofDeg(4.875278) );
        EclipticToEquatorialConversion zab = new EclipticToEquatorialConversion(ZonedDateTime.of

                (2009,7,6, 0,0,0,0, ZoneId.of("UTC")));

        EquatorialCoordinates eqCoord = zab.apply(a);

        assertEquals(143.722173, eqCoord.raDeg(),1e-6);
        assertEquals(19.535003, eqCoord.decDeg(), 1e-6);


    }


    /**
     * Examples taken from this website: https://tinyurl.com/qwl8cde
     */
    @Test
    void eclipticToEquatorialWorksWithValuesFromWebsite(){

        var eclipticCoordinates = EclipticCoordinates.of(
                Angle.ofDMS(15, 41,10.4),
                Angle.ofDMS(89,10,20.00)
        );
        var time = ZonedDateTime.of(LocalDate.of(1977, Month.DECEMBER, 31),
                LocalTime.of(0, 0), ZoneOffset.UTC);

        EclipticToEquatorialConversion eclToEqConversion = new EclipticToEquatorialConversion(time);


        assertEquals(
                Angle.ofDMS(66, 46,7.50),
                eclToEqConversion.apply(eclipticCoordinates).dec(),
                1e-8
        );

        assertEquals(
                Angle.ofHr(18 + 8.0/60 + 4.99 / 3600),
                (eclToEqConversion.apply(eclipticCoordinates).ra()),
                1e-7
        );
    }

    @Test
    void applyChainWorksOnKnownValues(){

        var date = ZonedDateTime.of(LocalDate.of(1988, Month.JANUARY, 15),
                LocalTime.of(0, 0 ), ZoneOffset.UTC);
        EclipticCoordinates ecl = EclipticCoordinates.of(Angle.ofDMS(10,10,11.6),Angle.ofDMS(5,5,5));
        var eclToEq = new EclipticToEquatorialConversion(date);
        var equToHor = new EquatorialToHorizontalConversion(date, GeographicCoordinates.ofDeg(0, 15));

        Function<EclipticCoordinates,HorizontalCoordinates> eclToHor = eclToEq.andThen(equToHor);

        HorizontalCoordinates hor = eclToHor.apply(ecl);


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

