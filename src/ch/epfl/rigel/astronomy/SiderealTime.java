package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.*;
import java.time.temporal.ChronoUnit;

import static ch.epfl.rigel.astronomy.Epoch.J2000;

/**
 * Static class offering tools for Sidereal Time related computations.
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class SiderealTime {

    private final static double TO_HOURS_IN_WHEN = (1e9 * 3600);
    private final static double NB_SEC_HOUR = 3600;
    private final static double TIME_JUL_DIFF_1 = 2400.051336;
    private final static double NB_SEC_MINUTE = 60;
    private final static double CT_S1 = 1.002737909;
    private final static Polynomial polynomial = Polynomial.of(0.000025862, TIME_JUL_DIFF_1, 6.697374558);



    //Non instanceable class.
    private SiderealTime() {
    }

    /**
     * Returns the greenwich sideral of the Zoned Date Time input date.
     *
     * @param when ZonedDateTime to compute the greenwich sidereal time.
     * @return the greenwich  sidereal time normalized to [0,TAU]
     */
    public static double greenwich(ZonedDateTime when) {
        ZonedDateTime whenInGreenwichUTC = when.withZoneSameInstant(ZoneOffset.UTC);

        //Computes the number of centuries between when and J2000 truncated to days. (Truncating to 0 hours)
        double timeJulianDifference = J2000.julianCenturiesUntil(whenInGreenwichUTC.truncatedTo(ChronoUnit.DAYS));

        //Computes the time in hours between the 0h and the when hour.
        double hoursInWhen = whenInGreenwichUTC.getHour() + ((double) whenInGreenwichUTC.getMinute() / NB_SEC_MINUTE) +
                (double) whenInGreenwichUTC.getSecond() / NB_SEC_HOUR +
                (double) whenInGreenwichUTC.getNano() / (TO_HOURS_IN_WHEN);

        //Equation Variables

        double S0 = polynomial.at(timeJulianDifference);
        double S1 = CT_S1 * hoursInWhen;

        double greenwichSiderealTimeInHours = S1 + S0;

        //Normalize angle to the interval
        return Angle.normalizePositive(Angle.ofHr(greenwichSiderealTimeInHours));
    }


    /**
     * Returns the local sidereal time for the Zoned Date Time Input in a given latitude.
     *
     * @param when  ZonedDateTime to compute the local sidereal time.
     * @param where latitude to be used for the computations.
     * @return local sidereal time in rad in the interval [0,TAU[
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where) {
        return Angle.normalizePositive(greenwich(when) + where.lon());
    }


}
