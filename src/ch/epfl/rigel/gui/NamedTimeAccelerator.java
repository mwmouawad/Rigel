package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;

public enum NamedTimeAccelerator{
    TIMES_1("1x", TimeAccelerator.continuous(1)),
    TIMES_30("30x", TimeAccelerator.continuous(30)),
    TIMES_300("300x", TimeAccelerator.continuous(300)),
    TIMES_3000("3000x", TimeAccelerator.continuous(3000)),
    DAY("jour", TimeAccelerator.discrete(Duration.ofHours(24), 60)),
    SIDEREAL_DAY("jour sidéral", TimeAccelerator.discrete(Duration.ofHours(23)
            .plus(Duration.ofMinutes(56)).plus(Duration.ofSeconds(4)), 60));

    private String name;
    private TimeAccelerator accelerator;

    NamedTimeAccelerator(String name, TimeAccelerator accelerator){
        this.name = name;
        this.accelerator = accelerator;
    }

    public String getName() { return name; }

    public TimeAccelerator getAccelerator() {
        return accelerator;
    }

    @Override
    public String toString() { return getName(); }

}
