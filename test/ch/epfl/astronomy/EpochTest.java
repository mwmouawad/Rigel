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

    }

    @Test
    void julianCenturiesUntilTest(){



    }

}
