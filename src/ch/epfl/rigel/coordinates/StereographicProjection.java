package ch.epfl.rigel.coordinates;

import java.util.Locale;
import java.util.function.Function;

public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {

    private final double centerX;
    private final double centerY;
    private final double sinCenterY;
    private final double cosCenterY;

    public StereographicProjection(HorizontalCoordinates center) {
        centerX = center.az();
        centerY = center.alt();
        sinCenterY = Math.sin(centerY);
        cosCenterY = Math.cos(centerY);
    }

    @Override
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {
        double coordX = azAlt.az();
        double coordY = azAlt.alt();
        double varX = coordX - centerX;

        double d = 1/(1 + Math.sin(coordY)* sinCenterY + Math.cos(coordY)*cosCenterY*Math.cos(varX));
        //computes the new coordinates
        double x = d*Math.cos(coordY)*Math.sin(varX);
        double y = d*(Math.sin(coordY)*cosCenterY - Math.cos(coordY)*sinCenterY*Math.cos(varX));
        return CartesianCoordinates.of(x,y);
    }

    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor){
        double coordY = cosCenterY/(Math.sin(hor.alt()) + sinCenterY);
        return CartesianCoordinates.of(0, coordY);
    }

    public double circleRadiusForParallel(HorizontalCoordinates parallel){
        return (Math.cos(parallel.alt())/(Math.sin(parallel.alt()) + sinCenterY));
    }


    public double applyToAngle(double rad){
        return 2*Math.tan(rad/4);
    }

    public HorizontalCoordinates inverseApply(CartesianCoordinates xy){
        double x = xy.x();
        double y = xy.y();
        double rho = Math.sqrt(x*x + y*y);
        double sinc = 2*rho/(rho*rho + 1);
        double cosc= (1-rho*rho)/(rho*rho + 1);

        //computes inverse coordinates with the above constants
        double lambda = Math.atan2(x*sinc, rho*cosCenterY*cosc - y*sinCenterY*sinc) + centerX;
        double phi = Math.asin(cosc*sinCenterY + (y*sinc*cosCenterY)/rho);
        return HorizontalCoordinates.of(lambda, phi);
    }

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    //TODO check if correct, only want center ?
    public String toString() {
        return String.format(Locale.ROOT, "StereographicProjection center : (x= %.4f, y= %.4f)", centerX,
                centerY);
    }


}
