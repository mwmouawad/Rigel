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

    private final double phi;
    private final double localTime;
    private final double cosPhi;
    private final double sinPhi;
    private final static RightOpenInterval ALT_INTERVAL = RightOpenInterval.symmetric(Math.PI);


    /**
     * Constructs the instance for the conversion fore a given position and date.
     *
     * @param when
     * @param where
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
        this.phi = where.lat();
        this.localTime = SiderealTime.local(when, where);
        this.cosPhi = Math.cos(phi);
        this.sinPhi = Math.sin(phi);
    }

    /**
     * Computes the horizontal coordinates for the equatorial coordinates input.
     *
     * @param equ equatorial coordinates input.
     * @return Horizontal Coordinates representation of the input.
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ) {
        double delta = equ.dec();
        //As seen from the book page 24 to find the hour angle.
        double hourAngle = Angle.normalizePositive(this.localTime - equ.ra());

        double cosAngleHour = Math.cos(hourAngle);
        double sinAngleHour = Math.sin(hourAngle);

        double sinDelta = Math.sin(delta);
        double cosDelta = Math.cos(delta);
        double alt = Math.asin(
                sinDelta * this.sinPhi + cosDelta * this.cosPhi * cosAngleHour
        );
        double az = Math.atan2(
                - cosDelta * this.cosPhi * sinAngleHour,
                sinDelta - this.sinPhi * Math.sin(alt)
        );

        return HorizontalCoordinates.of(Angle.normalizePositive(az), ALT_INTERVAL.reduce(alt));
    }

    /**
     * Not intended to be used. Will throw an exception.
     *
     * @param o object to be compared with.
     * @throws UnsupportedOperationException
     */
    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not intended to be used. Will throw an exception.
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}
