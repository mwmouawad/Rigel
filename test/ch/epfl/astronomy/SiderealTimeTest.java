package ch.epfl.astronomy;

import ch.epfl.rigel.astronomy.SiderealTime;
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
    void greenwichTest(){
        //TODO: FIND A TEST FOR IT. In the book they are not usin J2000 as a reference time.

        var dateTime = ZonedDateTime.of(LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36,51,670000000), ZoneOffset.UTC);

        assertEquals(Angle.ofHr(4.668120), SiderealTime.greenwich(dateTime),1e-6);
    }

    /**
     * As taken from the book Practical Astronomy Spreadsheet
     * page 27. Example: Local Sidereal Time.
     */
    @Test
    void localSiderealTimeTest(){


    }


}
