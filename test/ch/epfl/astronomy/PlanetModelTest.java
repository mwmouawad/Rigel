package ch.epfl.astronomy;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.astronomy.Planet;
import ch.epfl.rigel.astronomy.PlanetModel;
import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class PlanetModelTest {


    @Test
    /**
     * Example taken from the book page 126.
     * There is a difference in conversion from ecliptic to equatorial coordinates used in the book.
     */
    void atPositionWorksOnKnownValues() {

        var dateTime = ZonedDateTime.of(
                LocalDate.of(2003, Month.NOVEMBER, 22),
                LocalTime.of(0, 0),
                ZoneOffset.UTC);

        //Outer Planet
        Planet jupiterAt = PlanetModel.JUPITER.at(Epoch.J2010.daysUntil(dateTime), new EclipticToEquatorialConversion(dateTime));

        //Converting the book coordinates to ours.
        var eclToEq = new EclipticToEquatorialConversion(dateTime);
        var expectedEq = eclToEq.apply(EclipticCoordinates.of(Angle.ofDeg(166.310510), Angle.ofDeg(1.036466)));

        assertEquals(expectedEq.ra(), jupiterAt.equatorialPos().ra(), Angle.ofDeg(0.0000005));
        assertEquals(expectedEq.dec(), jupiterAt.equatorialPos().dec(), Angle.ofDeg(0.0000005));


        //Inner Planet
        Planet mercuryAt = PlanetModel.MERCURY.at(Epoch.J2010.daysUntil(dateTime), new EclipticToEquatorialConversion(dateTime));

        //Converting the book coordinates to ours.
        eclToEq = new EclipticToEquatorialConversion(dateTime);
        expectedEq = eclToEq.apply(EclipticCoordinates.of(Angle.ofDeg(253.929758), Angle.ofDeg(-2.044057)));

        assertEquals(expectedEq.ra(), mercuryAt.equatorialPos().ra(), Angle.ofDeg(0.0000005));
        assertEquals(expectedEq.dec(), mercuryAt.equatorialPos().dec(), Angle.ofDeg(0.0000005));

    }

    @Test
    void angularSizeWorksOnKnownValues() {

        var dateTime = ZonedDateTime.of(
                LocalDate.of(2003, Month.NOVEMBER, 22),
                LocalTime.of(0, 0),
                ZoneOffset.UTC);

        Planet planetAt = PlanetModel.JUPITER.at(Epoch.J2010.daysUntil(dateTime), new EclipticToEquatorialConversion(dateTime));

        assertEquals(Angle.ofArcsec(35.1), planetAt.angularSize(), Angle.ofArcsec(0.05));

    }

    @Test
    void allWorks() {
        var expectedList = new ArrayList<PlanetModel>();
        expectedList.add(PlanetModel.MERCURY);
        expectedList.add(PlanetModel.VENUS);
        expectedList.add(PlanetModel.EARTH);
        expectedList.add(PlanetModel.MARS);
        expectedList.add(PlanetModel.JUPITER);
        expectedList.add(PlanetModel.SATURN);
        expectedList.add(PlanetModel.URANUS);
        expectedList.add(PlanetModel.NEPTUNE);

        assertEquals(expectedList, PlanetModel.ALL);
    }

}
