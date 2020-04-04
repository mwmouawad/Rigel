package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.RightOpenInterval;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * Tool to represent equatorial coordinates to horizontal coordinates.
 * The class is implements a Function to enable chaining the conversions.
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    public final double phi;
    public final double localTime;
    private final double cosPhi;
    private final double sinPhi;
    private final static RightOpenInterval ALT_INTERVAL = RightOpenInterval.symmetric(Math.PI);


    /**
     * Constructs the instance for the conversion in a given position and date.
     *
     * @param when
     * @param where
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
        phi = where.lat();
        localTime = SiderealTime.local(when, where);
        cosPhi = Math.cos(phi);
        sinPhi = Math.sin(phi);
    }

    /**
     * Computes the horizontal coordinates of the input equatorial coordinates.
     *
     * @param equ equatorial coordinates input.
     * @return Horizontal Coordinates representation of the input.
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ) {
        double delta = equ.dec();
        //As seen from the book page 24 to find the hour angle.
        double hourAngle = Angle.normalizePositive(localTime - equ.ra());

        System.out.println("Hour Angle: " + Angle.toHr(hourAngle));

        double cosAngleHour = Math.cos(hourAngle);
        double sinAngleHour = Math.sin(hourAngle);

        double alt = Math.asin(
                Math.sin(delta) * sinPhi + Math.cos(delta) * cosPhi * cosAngleHour
        );
        double az = Math.atan2(
                -Math.cos(delta) * cosPhi * sinAngleHour
                , Math.sin(delta) - sinPhi * Math.sin(alt)
        );

        //second conversion not necessary because of the arcsin.

        return HorizontalCoordinates.of(Angle.normalizePositive(az), ALT_INTERVAL.reduce(alt));
    }


    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}
