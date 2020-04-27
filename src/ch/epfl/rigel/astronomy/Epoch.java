package ch.epfl.rigel.astronomy;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Enum intended to be used with the date standards for astronomy: 2010 and 2000.
 * Offers tools to computed time differences in day and julian centuries.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public enum Epoch {
    J2010(ZonedDateTime.of(
            LocalDate.of(2010, Month.JANUARY, 1).minusDays(1),
            LocalTime.of(0, 0),
            ZoneOffset.UTC)),
    J2000(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
            LocalTime.of(12, 0), ZoneOffset.UTC));

    private final ZonedDateTime time;
    private static final double MILLIS_TO_DAYS = Duration.ofDays(1).toMillis();
    private static final double MILLIS_TO_CENTURIES = Duration.ofDays(36525).toMillis();


    Epoch(ZonedDateTime time) {
        this.time = time;
    }

    /**
     * Computes the time difference between the input and the instance. Returns the result in days.
     * The result can be negative if the input is an earlier date than the instance.
     * @param when the Zoned Date Time to compare the instance to.
     * @return time difference in days. The result can be negative if the input is an earlier date than the instance.
     */
    public double daysUntil(ZonedDateTime when){
        return time.until(when, ChronoUnit.MILLIS)/MILLIS_TO_DAYS;
    }

    /**
     * Computes the time difference between the input and the instance. Returns the result in Julian Centuries.
     * The result can be negative if the input is an earlier date than the instance.
     * @param when the Zoned Date Time to compare the instance to.
     * @return time difference in Julian Centuries. The result can be negative if the input is an earlier date than the instance.
     */
    public double julianCenturiesUntil(ZonedDateTime when){
        return time.until(when, ChronoUnit.MILLIS)/MILLIS_TO_CENTURIES; }
}
