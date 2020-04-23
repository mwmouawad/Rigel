package ch.epfl.rigel.gui;

import java.time.Duration;

public enum NamedTimeAccelerator {
    TIMES_1("1x", TimeAccelerator.continuous(1)),
    TIMES_30("30x", TimeAccelerator.continuous(30)),
    TIMES_300("300x", TimeAccelerator.continuous(300)),
    TIMES_3000("3000x", TimeAccelerator.continuous(3000)),
    DAY("jour", TimeAccelerator.discrete(Duration.ofHours(24), Duration.ofSeconds(1/10)));
    //SIDEREAL_DAY("jour sidéral", TimeAccelerator.discrete(Duration.o, Duration.ofSeconds(1/10))),

    String name;
    TimeAccelerator accelerator;

    NamedTimeAccelerator(String name, TimeAccelerator accelerator){
        this.name = name;
        this.accelerator = accelerator;
    }
}
