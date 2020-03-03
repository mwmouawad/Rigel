package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

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
    }

    /**
     *
     * @param equ equatorial coordinates input.
     * @return Horizontal Coordinates representation of the input.
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ) {
        double delta = equ.dec();
        //As seen from the book page 24 to find the right ascension.
        double hourAngle = Angle.normalizePositive(angle - equ.ra());

        System.out.println("Hour Angle: " + Angle.toHr(hourAngle));

        cosAngle = Math.cos(hourAngle);
        sinAngle = Math.sin(hourAngle);

        double alt = Math.asin(
                Math.sin(delta) * sinPhi + Math.cos(delta) * cosPhi * cosAngle
        );
        double az = Math.atan2(
                -Math.cos(delta) * cosPhi * sinAngle
                ,Math.sin(delta) - sinPhi * Math.sin(alt)
        );

        return HorizontalCoordinates.of( Angle.normalizePositive(az), alt);
    }

    /**
     * Created for better testing purposes of the method apply.
     * @param hourAngle
     * @param dec
     * @return
     */
    public HorizontalCoordinates equatorialToHorizontalCoord(double hourAngle, double dec){

        double sinAngle  = Math.sin(hourAngle);
        double cosAngle  = Math.cos(hourAngle);


        double alt = Math.asin(
                Math.sin(dec) * sinPhi + Math.cos(dec) * cosPhi * cosAngle
        );
        double az = Math.atan2(
                -Math.cos(dec) * cosPhi * sinAngle
                ,Math.sin(dec) - sinPhi * Math.sin(alt)
        );
        return HorizontalCoordinates.of(az, alt);

    }

    /**
     *
     * @return
     */
    public double rightAscToHourAngle(double rightAsc){

        return this.angle - rightAsc;

    }

    @Override
    public boolean equals(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public int hashCode() { throw new UnsupportedOperationException(); }
}
