package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.time.*;
import java.time.temporal.ChronoUnit;

import static ch.epfl.rigel.astronomy.Epoch.J2000;

public final  class SiderealTime {

    //Non instanceable class.
    private SiderealTime(){}

    /**
     * Returns the greenwich sideral time in rad.
     * @param when
     * @return
     */
    public static double greenwich(ZonedDateTime when){
        ZonedDateTime whenInGreenwichUTC = when.withZoneSameInstant(ZoneOffset.UTC);

        //Computes the number of centuries between when and J2000 truncated to days. (Truncating to 0 hours)
        double timeJulianDifference = J2000.julianCenturiesUntil(whenInGreenwichUTC.truncatedTo(ChronoUnit.DAYS));

        //Computes the time in hours between the 0h and the when hour.
        double hoursInWhen = whenInGreenwichUTC.getHour() + ((double)whenInGreenwichUTC.getMinute() / 60) + (double)whenInGreenwichUTC.getSecond() / 3600 +
                (double)whenInGreenwichUTC.getNano() / (1e9 * 3600);

        //Equation Variables
        double S0 = ( 0.000025862 * timeJulianDifference * timeJulianDifference )
                    + (2400.051336 * timeJulianDifference)
                    + 6.697374558;
        double S1 = 1.002737909 * hoursInWhen;

        double greenwichSiderealTimeInHours = S1 + S0;

        System.out.println("Greenwhich hours: " + RightOpenInterval.of(0,24).reduce(greenwichSiderealTimeInHours));

        //Normalize angle to the interval
        return Angle.normalizePositive(Angle.ofHr(greenwichSiderealTimeInHours));
    }


    /**
     * Returns the local sidereal time from the Greenwich one.
     * @param when
     * @param where
     * @return local sidereal time in rad in the interval [0,TAU[
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where){
        return Angle.normalizePositive(greenwich(when) + where.lon());
    }



}
