package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

import java.time.ZonedDateTime;
import java.util.function.Function;

import static ch.epfl.rigel.astronomy.Epoch.J2000;

public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    public double phi;
    public double angle;

    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where){
        phi = where.lat();
        angle = SiderealTime.local(when, where);
    }

    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equatorialCoordinates) {
        double delta = equatorialCoordinates.dec();
        double az = Math.atan2(-Math.cos(delta)
                *Math.cos(phi)*Math.sin(angle), Math.sin(delta) - Math.sin(phi)*Math.sin(angle));
        double alt = Math.asin(Math.sin(delta)*Math.sin(phi) + Math.cos(delta)*Math.cos(phi)*Math.cos(angle));

        return HorizontalCoordinates.of(az, alt);
        
    }

    @Override
    public boolean equals(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public int hashCode() { throw new UnsupportedOperationException(); }
}
