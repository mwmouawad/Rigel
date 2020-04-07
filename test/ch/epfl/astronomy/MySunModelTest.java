package ch.epfl.astronomy;

import ch.epfl.rigel.astronomy.Sun;
import ch.epfl.rigel.astronomy.SunModel;
import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static ch.epfl.rigel.astronomy.Epoch.J2010;
import static org.junit.jupiter.api.Assertions.*;

public class MySunModelTest {

    @Test
    /**
     * Example taken from the book page 105.
     */
    void atWorksOnKnownValues(){

        var dateTime = ZonedDateTime.of(
                LocalDate.of(2003, Month.JULY, 27),
                LocalTime.of(0, 0),
                ZoneOffset.UTC);

        Sun sunPo = SunModel.SUN.at(-2349.0, new EclipticToEquatorialConversion(dateTime));

        assertEquals(Angle.ofDeg(201.159131), sunPo.meanAnomaly(), 10e-7);
        assertEquals(Angle.ofDMS(19,21,10), sunPo.equatorialPos().dec(), Angle.ofDMS(0,0,0.5));
        assertEquals(Angle.ofHr(8 + 23.0 / 60.0 + 34.0/3600.0), sunPo.equatorialPos().ra(), (Angle.ofHr(0.5/3600.0)));
        assertEquals(Angle.ofDeg(123.580601), sunPo.eclipticPos().lon(), Angle.ofDeg(0.0000005));

        //Testing conversion
        var eclToEq = new EclipticToEquatorialConversion(dateTime);
        var expectedEq = eclToEq.apply(EclipticCoordinates.of(Angle.ofDeg(123.580601), 0));

        assertEquals(expectedEq.ra(), sunPo.equatorialPos().ra(), Angle.ofDeg(0.0000005));
        assertEquals(expectedEq.dec(), sunPo.equatorialPos().dec(), Angle.ofDeg(0.0000005));

    }



    @Test
    void sunAngularSizeWorksOnKnownValues(){

        var dateTime = ZonedDateTime.of(
                LocalDate.of(2003, Month.JULY, 27),
                LocalTime.of(0, 0),
                ZoneOffset.UTC);

        Sun sunPo = SunModel.SUN.at(-2349.0, new EclipticToEquatorialConversion(dateTime));

        assertEquals(Angle.ofDeg(201.159131), sunPo.meanAnomaly(), 10e-7);
        assertEquals(Angle.ofDMS(0,31,30), sunPo.angularSize(), Angle.ofDMS(0,0,0.5));

    }


}
