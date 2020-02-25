package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.time.ZonedDateTime;

import static ch.epfl.rigel.astronomy.Epoch.J2000;

//TODO STATIC FINAL ??
public final class SiderealTime {
//TODO
    private SiderealTime(){}

    public static double greenwich(ZonedDateTime when){
        Interval interval = RightOpenInterval.of(0, Angle.TAU);
        return 0;
    }
}
