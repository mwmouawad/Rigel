package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Represents a pair of equatorial coordinates.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class EquatorialCoordinates extends SphericalCoordinates {

    private final static RightOpenInterval raInterval = RightOpenInterval.of(0,Angle.TAU);
    private final static ClosedInterval decInterval = ClosedInterval.symmetric(Math.PI);

    private EquatorialCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }


    /**
     * Creates an instance of Equatorial Coordinates.
     * @param ra right ascencion input between [0,24[ hours or [0°, 2PI[
     * @param dec declination input between [-PI/2, PI/2]
     * @throws IllegalArgumentException if the inputs are not contained in the bounds specified above.
     * @return
     */
    public static EquatorialCoordinates of(double ra, double dec){
        return new EquatorialCoordinates(
                Preconditions.checkInInterval(raInterval, ra),
                Preconditions.checkInInterval(decInterval, dec)
                );
    }

    public double ra(){ return super.lon(); }

    public double raDeg(){ return super.lonDeg(); }

    public double raHr(){ return Angle.toHr(this.ra()); }

    public double dec(){ return super.lat(); }

    public double decDeg(){ return super.latDeg(); }

    public String toString() {
        return String.format(Locale.ROOT, "(ra=%.4fh, dec=%.4f°)", Angle.toHr(this.ra()),
                this.decDeg()
        );
    }

}
