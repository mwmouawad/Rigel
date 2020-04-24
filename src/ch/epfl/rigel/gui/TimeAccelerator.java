package ch.epfl.rigel.gui;


import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

//TODO: annoter etape 7,8,9
@FunctionalInterface
public interface TimeAccelerator {


    // une valeur de type long exprimée en nanosecondes?
    ZonedDateTime adjust(ZonedDateTime initialTime, long time);

    //TODO : instance of ZonedDateTime
    static TimeAccelerator continuous(long alpha){
        return (initialTime, time) ->
                initialTime.plus(alpha * time, ChronoUnit.NANOS);
    }

    static TimeAccelerator discrete(Duration step, long frequence) {
        //TODO: step
        return (initialTime, time) -> {
            long steps = (long) Math.floor(frequence * time) * step.toNanos();
            return initialTime.plus(steps, ChronoUnit.NANOS);
        };
    }
}