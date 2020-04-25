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
        return (initialTime, timeNanoSec) ->
                initialTime.plus(alpha * timeNanoSec, ChronoUnit.NANOS);
    }

    static TimeAccelerator discrete(Duration step, long frequence) {
        return (initialTime, timeNanoSec) -> {
            long steps = (long) Math.floor(frequence * timeNanoSec) * step.toNanos();
            return initialTime.plus(steps, ChronoUnit.NANOS);
        };
    }
}