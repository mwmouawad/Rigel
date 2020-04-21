package ch.epfl.rigel.gui;


import java.time.ZonedDateTime;

@FunctionalInterface
public interface TimeAccelerator {

    // une valeur de type long exprimée en nanosecondes?
    ZonedDateTime adjust(ZonedDateTime initialTime, long time);
}
