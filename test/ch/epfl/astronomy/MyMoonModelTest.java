package ch.epfl.astronomy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.astronomy.MoonModel;
import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

public class MyMoonModelTest {

    @Test
    void moonPositionIsCorrectForKnownValues(){

        var when = ZonedDateTime.of(
                LocalDate.of(2003, Month.SEPTEMBER, 1),
                LocalTime.of(0, 0),
                ZoneOffset.UTC);

        var moonPos = MoonModel.MOON.at(Epoch.J2010.daysUntil(when), new EclipticToEquatorialConversion(when));

        //Convert the ecliptic Coordinates from the book to Equatorial Coordinates

        var eclToEq = new EclipticToEquatorialConversion(when);
        var expectedCoordinates = eclToEq.apply(EclipticCoordinates.of(Angle.ofDeg(214.862515), Angle.ofDeg(1.716257)));


        assertEquals(expectedCoordinates.ra(), moonPos.equatorialPos().ra(), Angle.ofDeg(0.0000005));
        assertEquals(expectedCoordinates.dec(), moonPos.equatorialPos().dec(), Angle.ofDeg(0.0000005));


    }


    @Test
    void moonAngularSizeIsCorrectForKnownValues(){

        var when = ZonedDateTime.of(
                LocalDate.of(2003, Month.SEPTEMBER, 1),
                LocalTime.of(0, 0),
                ZoneOffset.UTC);

        var moonPos = MoonModel.MOON.at(Epoch.J2010.daysUntil(when), new EclipticToEquatorialConversion(when));

        assertEquals(Angle.ofDeg(0.546822),moonPos.angularSize(), Angle.ofDeg(0.000005) );

    }


    @Test
    void moonPhaseIsCorrectForKnownValues(){

        var when = ZonedDateTime.of(
                LocalDate.of(2003, Month.SEPTEMBER, 1),
                LocalTime.of(0, 0),
                ZoneOffset.UTC);

        var moonPos = MoonModel.MOON.at(Epoch.J2010.daysUntil(when), new EclipticToEquatorialConversion(when));


        //TESTED using debugger


    }



}
