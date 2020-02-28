package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    public final Polynomial epsi =  Polynomial.of(Angle.ofArcsec(0.00181), Angle.ofArcsec(-0.0006),
            Angle.ofArcsec(-46.815), Angle.ofDMS((int) Angle.ofHr(23), 26, 21.45));
    //TODO ASK FOR HOURS CAST
    public final double ra = 0;

    public EclipticToEquatorialConversion(ZonedDateTime when){
    }

    @Override
    public EquatorialCoordinates apply(EclipticCoordinates eclipticCoordinates) {
        return null;
    }
}
