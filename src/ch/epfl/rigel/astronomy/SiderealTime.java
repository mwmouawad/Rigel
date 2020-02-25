package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.time.*;
import java.time.temporal.ChronoUnit;

import static ch.epfl.rigel.astronomy.Epoch.J2000;

public final  class SiderealTime {

    private static RightOpenInterval interval = RightOpenInterval.of(0, Angle.TAU);


    //Non instanceable class.
    private SiderealTime(){}

    /**
     * Returns the greenwhich sideral time in rad.
     * @param when
     * @return
     */
    public static double greenwich(ZonedDateTime when){

        ZonedDateTime whenInGreenwhichUTC = when.withZoneSameInstant(ZoneOffset.UTC);
        System.out.println(whenInGreenwhichUTC.truncatedTo(ChronoUnit.DAYS));

        System.out.println("J200: " + ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
                LocalTime.of(12, 0), ZoneOffset.UTC));

        //Calculate the number of centuries between when and J2000 truncated to days. (Truncating to 0 hours)
        double timeJulianDifference = J2000.julianCenturiesUntil(whenInGreenwhichUTC.truncatedTo(ChronoUnit.DAYS));
        System.out.println("Julian Difference: " + timeJulianDifference);

        //Calculate the number of hours between the 0h and the when hour.
        double hoursInWhen = when.getHour();
        System.out.println("Number of hours in the date: " + hoursInWhen);

        //Equation Variables
        double S0 = ( 0.000025862 * timeJulianDifference * timeJulianDifference )
                    + (2400.051336 * timeJulianDifference)
                    + 6.697374558;
        double S1 = 1.002737909 * hoursInWhen;


        double greenWhichSiderealTimeInHours = S0 + S1;

        System.out.println(greenWhichSiderealTimeInHours);

        //TODO: How to convert from hours to rad? Using simple conversion?
        double greenWhichSiderealTimeInRad = Angle.ofHr(greenWhichSiderealTimeInHours);

        //Normalize angle to the interval

        return interval.reduce(greenWhichSiderealTimeInRad);
    }

    /**
     * Returns the local sideral time.
     * @param when
     * @param where
     * @return local sideral time in rad in the interval [0,TAU[
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where){

        return 0;

    }

}
