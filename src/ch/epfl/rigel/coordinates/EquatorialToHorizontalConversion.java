package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;

import java.time.ZonedDateTime;
import java.util.function.Function;

public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    public double phi;
    public double angle;
    private double cosPhi;
    private double sinPhi;
    private double cosAngle;
    private double sinAngle;

    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where){
        phi = where.lat();
        angle = SiderealTime.local(when, where);

        cosPhi = Math.cos(phi);
        sinPhi = Math.sin(phi);
        cosAngle = Math.cos(angle);
        sinAngle = Math.sin(angle);

    }

    /**
     *
     * @param equ equatorial coordinates input.
     * @return Horizontal Coordinates representation of the input.
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ) {
        double delta = equ.dec();

        double alt = Math.asin(
                Math.sin(delta) * sinPhi + Math.cos(delta) * cosPhi * cosAngle
        );

        double az = Math.atan2(
                -Math.cos(delta) * cosPhi * sinAngle
                ,Math.sin(delta) - sinPhi * Math.sin(alt)
        );


        return HorizontalCoordinates.of(az, alt);
    }

    @Override
    public boolean equals(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public int hashCode() { throw new UnsupportedOperationException(); }
}
