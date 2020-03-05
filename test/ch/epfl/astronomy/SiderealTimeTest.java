package ch.epfl.astronomy;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.*;

/**
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public class SiderealTimeTest {


    @Test
    /**
     * As taken from the book Practical Astronomy Spreadsheet
     * page 23. Example: Sidereal Time.
     */
    void greenwichWorksWithKnownExample(){

        var dateTime = ZonedDateTime.of(LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36,51,670000000), ZoneOffset.UTC);

        assertEquals(Angle.ofHr(4.668119), SiderealTime.greenwich(dateTime),1e-6);

    }

    @Test
    void greenwichWorksWithOtherTimeZones(){

        //Date time at GMT-3:00 (Vai Brasil).
        var dateTime = ZonedDateTime.of(LocalDate.of(2010, Month.DECEMBER, 10),
                LocalTime.of(20, 15,30,0), ZoneId.of("Brazil/East"));

        assertEquals( Angle.ofHr(3.0 + 33.0/60 + 36.91/3600), SiderealTime.greenwich(dateTime), 1e-7);



    }




    /**
     * As taken from the book Practical Astronomy Spreadsheet
     * page 27. Example: Local Sidereal Time.
     */
    @Test
    void localSiderealTimeTest(){
        var dateTime = ZonedDateTime.of(LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36,51,670000000), ZoneOffset.UTC);

        var when = GeographicCoordinates.ofDeg(-64, 0);

        assertEquals(Angle.ofHr(0.401453), SiderealTime.local(dateTime, when),1e-6);

        //Second Test
        var dateTime2 = ZonedDateTime.of(LocalDate.of(2020, Month.MARCH, 3),
                LocalTime.of(5, 35,35,0), ZoneOffset.UTC);

        var when2 = GeographicCoordinates.ofDeg(-80, 0);

        assertEquals(Angle.ofHr(11.0 + 1.0/60 + 25.80/3600), SiderealTime.local(dateTime2, when2),1e-6);

    }


}
