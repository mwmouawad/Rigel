package ch.epfl.rigel.astronomy;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
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

    private ZonedDateTime time;
    private static final double MILLIS_TO_DAYS = Duration.ofDays(1).toMillis();
    private static final double MILLIS_TO_CENTURIES = Duration.ofDays(36525).toMillis();

    Epoch(ZonedDateTime time) {
        this.time = time;
    }

    public double daysUntil(ZonedDateTime when){
        return time.until(when, ChronoUnit.MILLIS)/MILLIS_TO_DAYS;
    }

    public double julianCenturiesUntil(ZonedDateTime when){
        return time.until(when, ChronoUnit.MILLIS)/MILLIS_TO_CENTURIES; }
}
