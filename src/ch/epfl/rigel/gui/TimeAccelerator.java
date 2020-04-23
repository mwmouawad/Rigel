package ch.epfl.rigel.gui;


import java.time.Duration;
import java.time.ZonedDateTime;

@FunctionalInterface
public interface TimeAccelerator {

    // une valeur de type long exprimée en nanosecondes?
    ZonedDateTime adjust(ZonedDateTime initialTime, long time);

    static TimeAccelerator continuous(double alpha){
        return null;
    }

    static TimeAccelerator discrete(Duration step, Duration frequence){
        return null;
    }
}
