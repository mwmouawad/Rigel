package ch.epfl.rigel.gui;


import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * Time Accelerator mother class consisting of continuous and discrete accelerations.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
@FunctionalInterface
public interface TimeAccelerator {

    /**
     * Adjust the TimeAccelerator by some given time in nano seconds.
     * @param initialTime initialTime of the accelerator.
     * @param timeNano added nano time to it.
     * @return adjusted TimeAccelerator.
     */
    ZonedDateTime adjust(ZonedDateTime initialTime, long timeNano);

    /**
     * Returns a TimeAccerelator with adjusted acceleration by a alpha.
     * @param alpha
     * @return adjusted TimeAccelerator.
     */
    static TimeAccelerator continuous(long alpha){
        return (initialTime, timeNanoSec) ->
                initialTime.plus(alpha * timeNanoSec, ChronoUnit.NANOS);
    }

    /**
     * Returns a new TimeAccelerator adjusted by a number of given steps.
     * @param step discrete simulated time.
     * @param frequency number of elapsed steps by real time unity.
     * @return adjusted TimeAccelerator by number of steps.
     */
    static TimeAccelerator discrete(Duration step, long frequency) {
        return (initialTime, timeNanoSec) -> {
            long steps = (long) Math.floor(frequency * 1e-9 * timeNanoSec) * step.toNanos();
            return initialTime.plus(steps, ChronoUnit.NANOS);
        };
    }
}