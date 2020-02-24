package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

public final class EclipticCoordinates extends SphericalCoordinates {


    private EclipticCoordinates(double lon, double lat ){
        super(lon, lat);
    }

    public static EclipticCoordinates of(double lon, double lat){

        //TODO: Check conditions

        return new EclipticCoordinates(lon,lat);

    }

    public double lon(){
        return super.lon();
    }

    public double lat(){
        return super.lat();
    }

    public double lonDeg(){
        return super.lonDeg();
    }

    public double latDeg(){
        return super.latDeg();
    }


    public String toString() {
        return String.format(Locale.ROOT, "(λ=%.4f°,β=%.4f°)", this.lonDeg(), this.latDeg());
    }



}
