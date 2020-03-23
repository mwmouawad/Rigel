package ch.epfl.astronomy;

import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.io.CharArrayReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class MyStereoGraphicProjectionTest {


    @Test
    void applyWorksOnKnwonValues(){

        StereographicProjection stereoProj = new StereographicProjection(HorizontalCoordinates.ofDeg(15.32, -27.42));

        var projection = stereoProj.apply(HorizontalCoordinates.ofDeg(55.49, 0));

        assertEquals(0.38435480109888986, projection.x());
        assertEquals(0.20967273883925536, projection.y());


    }


    @Test
    void circleCenterForParallelWorksOnKnownValue(){
        StereographicProjection stereoProj = new StereographicProjection(HorizontalCoordinates.ofDeg(15.42, 15.42));

        var projection = stereoProj.circleCenterForParallel(HorizontalCoordinates.ofDeg(32,-10.2));

        assertEquals(0, projection.x());
        assertEquals(10.854920751644674, projection.y());

    }

    @Test
    void circleCenterForParallelWorksWithInfinity() {

        StereographicProjection stereoProj2 = new StereographicProjection(HorizontalCoordinates.ofDeg(0, 45));
        var projection2 = stereoProj2.circleCenterForParallel(HorizontalCoordinates.ofDeg(0,-45));

        assertEquals(0, projection2.x());
        assertEquals(Double.POSITIVE_INFINITY, projection2.y() );


        StereographicProjection stereoProj3 = new StereographicProjection(HorizontalCoordinates.ofDeg(0, -45));
        var projection3 = stereoProj3.circleCenterForParallel(HorizontalCoordinates.ofDeg(0,45));

        assertEquals(0, projection2.x());
        assertEquals(Double.POSITIVE_INFINITY, projection2.y() );


    }


        @Test
    void circleRadiusForParallelWorksWithKnownValues(){
        StereographicProjection stereoProj = new StereographicProjection(HorizontalCoordinates.ofDeg(15.42, 15.42));

        var radius = stereoProj.circleRadiusForParallel(HorizontalCoordinates.ofDeg(32,-10.2));

        assertEquals(11.082298726568336, radius);

    }

    @Test
    void circleRadiusForParallelWorksWithInfinity() {

        StereographicProjection stereoProj2 = new StereographicProjection(HorizontalCoordinates.ofDeg(0, 0));

        var radius2 = stereoProj2.circleRadiusForParallel(HorizontalCoordinates.ofDeg(0,0));

        assertEquals(Double.POSITIVE_INFINITY, radius2 );

    }

    @Test
    void applyToAngle(){
    }

    @Test
    void inverseApplyWorksOnKnownValues(){

        StereographicProjection stereoProj = new StereographicProjection(HorizontalCoordinates.ofDeg(15.32, -27.42));

        var projection = stereoProj.apply(HorizontalCoordinates.ofDeg(55.49, 0));

        var inverProjection = stereoProj.inverseApply(CartesianCoordinates.of(0.38435480109888986,0.20967273883925536));

        assertEquals(Angle.ofDeg(55.49), inverProjection.az(), 1e-15);
        assertEquals(Angle.ofDeg(0), inverProjection.alt(), 1e-15);


    }


        @Test
    void hashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            CartesianCoordinates.of(1,-1).hashCode();
        });
    }

    @Test
    void equalsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {

            var cart = CartesianCoordinates.of(1,-1);
            cart.equals(cart);
        });
    }

}
