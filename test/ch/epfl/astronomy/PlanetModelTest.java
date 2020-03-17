package ch.epfl.astronomy;
import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.astronomy.Planet;
import ch.epfl.rigel.astronomy.PlanetModel;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;


public class PlanetModelTest {


    @Test
    /**
     * Example taken from the book page 126.
     * There is a difference in conversion from ecliptic to equatorial coordinates used in the book.
     */
    void atPositionWorksOnKnownValues(){

        var dateTime = ZonedDateTime.of(
                LocalDate.of(2003, Month.NOVEMBER, 22),
                LocalTime.of(0, 0),
                ZoneOffset.UTC);

        //Outer Planet
        Planet jupiterAt = PlanetModel.JUPITER.at(Epoch.J2010.daysUntil(dateTime), new EclipticToEquatorialConversion(dateTime));

        //TODO: WORKING. For now it is working seeing the ecliptic values using debugger. How to test with their conversions?

        //Inner Planet
        Planet mercuryAt = PlanetModel.MERCURY.at(Epoch.J2010.daysUntil(dateTime), new EclipticToEquatorialConversion(dateTime));

    }

    @Test
    void angularSizeWorksOnKnownValues(){

        var dateTime = ZonedDateTime.of(
                LocalDate.of(2003, Month.NOVEMBER, 22),
                LocalTime.of(0, 0),
                ZoneOffset.UTC);

        Planet planetAt = PlanetModel.JUPITER.at(Epoch.J2010.daysUntil(dateTime), new EclipticToEquatorialConversion(dateTime));

        assertEquals(Angle.ofArcsec(35.1), planetAt.angularSize(), Angle.ofArcsec(0.05));

    }

}
