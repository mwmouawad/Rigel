package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

import static ch.epfl.rigel.astronomy.Epoch.J2000;

/**
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    private final Polynomial epsi =  Polynomial.of(Angle.ofArcsec(0.00181), -Angle.ofArcsec(0.0006),
            -Angle.ofArcsec(46.815), Angle.ofDMS(23, 26, 21.45));
    private ZonedDateTime when;
    private double cosepsi;
    private double sinepsi;


    public EclipticToEquatorialConversion(ZonedDateTime when){
        this.when = when;
        double time = J2000.julianCenturiesUntil(when);
        this.cosepsi = Math.cos(epsi.at(time));
        this.sinepsi = Math.sin(epsi.at(time));
    }

    @Override
    public EquatorialCoordinates apply(EclipticCoordinates eclipticCoordinates) {
        double lambda = eclipticCoordinates.lon();
        double beta = eclipticCoordinates.lat();
        double time = J2000.julianCenturiesUntil(when);
        double alpha = Math.atan2(Math.sin(lambda)*cosepsi - Math.tan(beta)*sinepsi,(Math.cos(lambda)));
        return null;
    }
}
