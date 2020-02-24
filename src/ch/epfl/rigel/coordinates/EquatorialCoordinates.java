package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

public final class EquatorialCoordinates extends SphericalCoordinates {

    private static RightOpenInterval raInterval = RightOpenInterval.of(0,360);
    private static ClosedInterval decInterval = ClosedInterval.of(-90,90);



    private EquatorialCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }


    public static EquatorialCoordinates of(double ra, double dec){

        Preconditions.checkInInterval(raInterval, Angle.toDeg(ra));
        Preconditions.checkInInterval(decInterval, Angle.toDeg(dec));


        return new EquatorialCoordinates(ra, dec);

    }


    public double ra(){ return super.lon(); }

    public double raDeg(){ return super.lonDeg(); }

    public double raHr(){ return Angle.toHr(this.ra()); }

    public double dec(){ return super.lat(); }

    public double decDeg(){ return super.latDeg(); }

    public String toString() {
        return String.format(Locale.ROOT, "(ra=%.4fhr,dec=%.4f°)", Angle.toHr(this.ra()),
                this.decDeg()
        );
    }





}
