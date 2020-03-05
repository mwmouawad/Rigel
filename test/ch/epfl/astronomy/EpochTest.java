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

        ZonedDateTime d = ZonedDateTime.of(
                LocalDate.of(2000, Month.JANUARY, 3),
                LocalTime.of(18, 0),
                ZoneOffset.UTC);

        assertEquals(2.25, Epoch.J2000.daysUntil(d));

        var d2 = ZonedDateTime.of(
                LocalDate.of(1977, Month.APRIL, 3),
                LocalTime.of(16, 51, 34),
                ZoneOffset.UTC);


        assertEquals(-717793706.0 / (60 * 60 * 24), Epoch.J2000.daysUntil(d2));


    }

    @Test
    void julianCenturiesUntilTest(){

        var d2 = ZonedDateTime.of(
                LocalDate.of(1977, Month.APRIL, 3),
                LocalTime.of(16, 51, 34),
                ZoneOffset.UTC);


        assertEquals(-717793706.0 / (60 * 60 * 24 * 365.25 * 100), Epoch.J2000.julianCenturiesUntil(d2));


    }

}
