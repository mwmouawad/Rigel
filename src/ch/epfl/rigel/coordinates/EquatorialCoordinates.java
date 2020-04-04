package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Represents a pair of equatorial coordinates.
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class EquatorialCoordinates extends SphericalCoordinates {

    private final static RightOpenInterval RA_INTERVAL = RightOpenInterval.of(0, Angle.TAU);
    private final static ClosedInterval DEC_INTERVAL = ClosedInterval.symmetric(Math.PI);

    private EquatorialCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }


    /**
     * Creates an instance of Equatorial Coordinates.
     *
     * @param ra  right ascencion input between [0,24[ hours or [0, 2PI[
     * @param dec declination input between [-PI/2, PI/2]
     * @return
     * @throws IllegalArgumentException if the inputs are not contained in the bounds specified above.
     */
    public static EquatorialCoordinates of(double ra, double dec) {
        return new EquatorialCoordinates(
                Preconditions.checkInInterval(RA_INTERVAL, ra),
                Preconditions.checkInInterval(DEC_INTERVAL, dec)
        );
    }

    /**
     * Returns the right ascension (ra) in radian degrees.
     *
     * @return the right ascension (ra) in radian degrees.
     */
    public double ra() {
        return super.lon();
    }

    /**
     * Returns the right ascension (Ra) in celsius degrees.
     *
     * @return the right ascension (Ra) in celsius degrees.
     */
    public double raDeg() {
        return super.lonDeg();
    }

    /**
     * Returns the right ascension (ra) in hours.
     *
     * @return the right ascension (ra) in hours.
     */
    public double raHr() {
        return Angle.toHr(this.ra());
    }

    /**
     * Returns the declination (dec) in radian degrees .
     *
     * @return the declination (dec) in radian degrees.
     */
    public double dec() {
        return super.lat();
    }

    /**
     * Returns the declination (dec) in celsius degrees .
     *
     * @return the declination (dec) in celsius degrees.
     */
    public double decDeg() {
        return super.latDeg();
    }

    /**
     * @see Object#toString()
     */
    public String toString() {
        return String.format(Locale.ROOT, "(ra=%.4fh, dec=%.4f°)", Angle.toHr(this.ra()),
                this.decDeg()
        );
    }

}
