package ch.epfl.astronomy;
import ch.epfl.rigel.astronomy.Epoch;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public class EpochTest {

    @Test
    void daysUntilTest(){

        ZonedDateTime d1 = ZonedDateTime.of(
                LocalDate.of(2000, Month.JANUARY, 3),
                LocalTime.of(18, 0),
                ZoneOffset.UTC);

        assertEquals(2.25, Epoch.J2000.daysUntil(d1));

        var d2 = ZonedDateTime.of(
                LocalDate.of(1977, Month.APRIL, 3),
                LocalTime.of(16, 51, 34),
                ZoneOffset.UTC);


        assertEquals(-717793706.0 / (60 * 60 * 24), Epoch.J2000.daysUntil(d2));


        ZonedDateTime a = ZonedDateTime.of(LocalDate.of(2003, Month.JULY, 30), LocalTime.of(15, 0), ZoneOffset.UTC);
        ZonedDateTime b = ZonedDateTime.of(LocalDate.of(2020, Month.MARCH, 20), LocalTime.of(0, 0), ZoneOffset.UTC);
        ZonedDateTime c = ZonedDateTime.of(LocalDate.of(2006, Month.JUNE, 16), LocalTime.of(18, 13), ZoneOffset.UTC);
        ZonedDateTime d = ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 3), LocalTime.of(18, 0), ZoneOffset.UTC);
        ZonedDateTime e = ZonedDateTime.of(LocalDate.of(1999, Month.DECEMBER, 6), LocalTime.of(23, 3), ZoneOffset.UTC);


        assertEquals(1306.125, Epoch.J2000.daysUntil(a));
        assertEquals(7383.5, Epoch.J2000.daysUntil(b));
        assertEquals(2358.259028, Epoch.J2000.daysUntil(c), 1e-6);
        assertEquals(2.25, Epoch.J2000.daysUntil(d));
        assertEquals(-25.539583, Epoch.J2000.daysUntil(e), 1e-6);
        assertEquals(-2345.375, Epoch.J2010.daysUntil(a), 1e-6);
        assertEquals(3732, Epoch.J2010.daysUntil(b));
        assertEquals(-1293.240972, Epoch.J2010.daysUntil(c), 1e-6);
        assertEquals(-3649.25, Epoch.J2010.daysUntil(d), 1e-6);
        assertEquals(-3677.039583, Epoch.J2010.daysUntil(e), 1e-6);


    }

    @Test
    void julianCenturiesUntilTest(){

        var d2 = ZonedDateTime.of(
                LocalDate.of(1977, Month.APRIL, 3),
                LocalTime.of(16, 51, 34),
                ZoneOffset.UTC);


        assertEquals(-717793706.0 / (60 * 60 * 24 * 365.25 * 100), Epoch.J2000.julianCenturiesUntil(d2));


        ZonedDateTime a = ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1).minusDays(36525), LocalTime.of(12, 0),

                ZoneOffset.UTC);

        assertEquals(-1, Epoch.J2000.julianCenturiesUntil(a));
    }

}
