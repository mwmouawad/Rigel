package ch.epfl.rigel.astronomy;

import java.time.*;
import java.time.temporal.ChronoUnit;


public enum Epoch {
    J2010(ZonedDateTime.of(
            LocalDate.of(2010, Month.JANUARY, 1).minusDays(1),
            LocalTime.of(0, 0),
            ZoneOffset.UTC)),
    J2000(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1),
            LocalTime.of(12, 0), ZoneOffset.UTC));

    private ZonedDateTime time;
    private final double MILLISTODAYS = 1000*60*60*24;

    Epoch(ZonedDateTime time) {
        this.time = time;
    }

    public double daysUntil(ZonedDateTime when){
        return (time.until(when, ChronoUnit.MILLIS))/MILLISTODAYS;
    }

    public double julianCenturiesUntil(ZonedDateTime when){
        //TODO change this method so there is no dependence between them.
        return (daysUntil(when)/36525); }
}
