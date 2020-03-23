package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.Locale;
import java.util.function.Function;

/**
 * Represents a Stereographic method of projection from any Spherical Coordinates
 * system to cartesian coordinates centered on coordinates (x,y).
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {

    private final double centerX;
    private final double centerY;
    private final double sinCenterY;
    private final double cosCenterY;

    /**
     * Constructor returning a StereoGraphic projection centered in the horizontal coordinates.
     * @param center coordinates where the projection is centered.
     */
    public StereographicProjection(HorizontalCoordinates center) {
        centerX = center.az();
        centerY = center.alt();
        sinCenterY = Math.sin(centerY);
        cosCenterY = Math.cos(centerY);
    }

    /**
     * Computes the projection of the given horizontal coordinates to  cartesian coordinates
     * centered on the instance coordinates.
     * @param azAlt input horizontal coordinates to project.
     * @return the corresponding projection cartesian coordinates.
     */
    @Override
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {

        double coordX = azAlt.az();
        double coordY = azAlt.alt();
        double varX = coordX - centerX;


        double d = 1 / (1 + Math.sin(coordY) * sinCenterY + Math.cos(coordY) * cosCenterY * Math.cos(varX));

        //computes the new coordinates
        double x = d * Math.cos(coordY) * Math.sin(varX);
        double y = d * (Math.sin(coordY) * cosCenterY - Math.cos(coordY) * sinCenterY * Math.cos(varX));

        return CartesianCoordinates.of(x, y);
    }

    /**
     * Computes the center coordinates of the result circle from the projection
     * of the parallel passing by the given horizontal coordinates.
     * @param hor horizontal coordinates of the parallel that will be projected.
     * @return center coordinates of the resulting circle of the projection.
     * The y coordinates can be of type Infinity.
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor) {
        double sin2 = Math.sin(hor.alt());
        double coordY = cosCenterY / (sin2 + sinCenterY);
        return CartesianCoordinates.of(0, coordY);
    }

    /**
     * Computes the radius coordinates of the result circle from the projection
     * of the parallel passing by the given horizontal coordinates.
     * @param parallel horizontal coordinates of the parallel that will be projected.
     * @return center coordinates of the resulting circle of the projection. This radius can be of type
     * Infinity.
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel) {
        return (Math.cos(parallel.alt()) / (Math.sin(parallel.alt()) + sinCenterY));
    }

    /**
     * Computes the projected diameter of a sphere of given angular size
     * centered in the instance's StereoGraphicProjection center.
     * We suppose the sphere is at the horizon.
     * @param rad angular size of the sphere to be projected.
     * @return diameter of the projected sphere.
     */
    public double applyToAngle(double rad) {
        return 2 * Math.tan(rad / 4);
    }

    /**
     * Computes the horizontal coordinates of the input of a stereographic projection
     * with given output as parameter.
     * @param xy projected cartesian coordinates output.
     * @return the initial horizontal coordinates before projection .
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {
        double x = xy.x();
        double y = xy.y();
        double rho = Math.sqrt(x * x + y * y);
        double sinc = 2 * rho / (rho * rho + 1);
        double cosc = (1 - rho * rho) / (rho * rho + 1);

        //computes inverse coordinates with the above constants
        double lambda = Angle.normalizePositive(Math.atan2(x * sinc, rho * cosCenterY * cosc - y * sinCenterY * sinc) + centerX);
        double phi = Math.asin(cosc * sinCenterY + (y * sinc * cosCenterY) / rho);
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

    public String toString() {
        return String.format(Locale.ROOT, "Stereographic Projection center : (x= %.4f, y= %.4f)", centerX,
                centerY);
    }


}
