package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;
import ch.epfl.rigel.math.RightOpenInterval;

import java.time.ZonedDateTime;
import java.util.function.Function;

import static ch.epfl.rigel.astronomy.Epoch.J2000;

/**
 * Tool to represent ecliptic coordinates to equatorial coordinates.
 * The class is implements a function to enable chaining the conversions.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    private static final Polynomial EPSI = Polynomial.of(
            Angle.ofArcsec(0.00181),
            -Angle.ofArcsec(0.0006),
            -Angle.ofArcsec(46.815),
            Angle.ofDMS(23, 26, 21.45)
    );
    private final double cosEpsi;
    private final double sinEpsi;
    private final static  RightOpenInterval DEC_INTERVAL =  RightOpenInterval.symmetric(Math.PI);

    /**
     * @param when time zone time used to be converted.
     */
    public EclipticToEquatorialConversion(ZonedDateTime when) {
        double time = J2000.julianCenturiesUntil(when);
        cosEpsi = Math.cos(EPSI.at(time));
        sinEpsi = Math.sin(EPSI.at(time));
    }

    /**
     * Returns the input in equatorial coordinates
     *
     * @param eclipticCoordinates input in ecliptic coordinates.
     * @return equatorial coordinates representation of the input.
     */
    @Override
    public EquatorialCoordinates apply(EclipticCoordinates eclipticCoordinates) {
        double lambda = eclipticCoordinates.lon();
        double beta = eclipticCoordinates.lat();

        double alpha = Math.atan2(
                Math.sin(lambda) * cosEpsi - Math.tan(beta) * sinEpsi
                ,Math.cos(lambda)
        );

        double delta = Math.asin(Math.sin(beta) * cosEpsi + Math.cos(beta) * sinEpsi * Math.sin(lambda));

        return EquatorialCoordinates.of(Angle.normalizePositive(alpha), DEC_INTERVAL.reduce(delta));
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
