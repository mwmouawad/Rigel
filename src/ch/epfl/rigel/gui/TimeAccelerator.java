package ch.epfl.rigel.gui;


import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

//TODO: annoter etape 7,8,9
@FunctionalInterface
public interface TimeAccelerator {

    //TODO: Trouver annotation pour nano secondes.
    ZonedDateTime adjust(ZonedDateTime initialTime, long timeNanoSec);

    static TimeAccelerator continuous(long alpha){
        return (initialTime, time) ->
                initialTime.plus(alpha * time, ChronoUnit.NANOS);
    }

    static TimeAccelerator discrete(Duration step, long frequence) {
        return (initialTime, time) -> {
            long steps = (long) Math.floor(frequence * time) * step.toNanos();
            return initialTime.plus(steps, ChronoUnit.NANOS);
        };
    }
}