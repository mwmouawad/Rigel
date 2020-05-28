package ch.epfl.rigel.gui;

import java.time.Duration;

/**
 * Utility class containing enum for some continuous and discrete Time Accelerators that will be used
 * in this project.
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public enum NamedTimeAccelerator {
    TIMES_1("1x", TimeAccelerator.continuous(1)),
    TIMES_30("30x", TimeAccelerator.continuous(30)),
    TIMES_300("300x", TimeAccelerator.continuous(300)),
    TIMES_3000("3000x", TimeAccelerator.continuous(3000)),
    DAY("jour", TimeAccelerator.discrete(Duration.ofHours(24), 60)),
    SIDEREAL_DAY("jour sidéral", TimeAccelerator.discrete(Duration.ofHours(23)
            .plus(Duration.ofMinutes(56)).plus(Duration.ofSeconds(4)), 60));

    final private String name;
    final private TimeAccelerator accelerator;

    /**
     * Constructs a instance of TimeAccelerator.
     *
     * @param name
     * @param accelerator
     */
    NamedTimeAccelerator(String name, TimeAccelerator accelerator) {
        this.name = name;
        this.accelerator = accelerator;
    }

    /**
     * Gets the name for the time accelerator.
     * e.g. "30x" for a 30 times continuous acceleration.
     * e.g. "jour" for a one day acceleration.
     *
     * @return the name of the accelerator.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the associated TimeAccelerator.
     *
     * @return
     */
    public TimeAccelerator getAccelerator() {
        return accelerator;
    }

    /**
     * Outputs the accelerator's name from method getName();
     *
     * @return the accelerator's name from getName();
     */
    @Override
    public String toString() {
        return getName();
    }

}
