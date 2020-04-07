package ch.epfl.rigel.gui;


import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import javafx.scene.shape.Circle;

public class SkyCanvasPainter {

    Canvas canvas;
    GraphicsContext graphicContext;
    private Color BACKGROUND_COLOR = Color.BLACK;
    private double SCALE_FACTOR = 1300;

    public  SkyCanvasPainter(Canvas canvas) {
        this.canvas = canvas;
        this.graphicContext = canvas.getGraphicsContext2D();
        this.graphicContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void clear() {
        this.graphicContext.setFill(BACKGROUND_COLOR);
        this.graphicContext.fillRect(0,0, this.canvas.getWidth(), this.canvas.getHeight());
    }

    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

        double[] transformedStarPos = new double[sky.starPositions().length];
        planeToCanvas.transform2DPoints(sky.starPositions(), 0, transformedStarPos,0,sky.stars().size() );

        int i = 0;
        for(Star s: sky.stars()){
            Color starColor = BlackBodyColor.colorForTemperature(s.colorTemperature());

            //Draw the element
            double diameter = SCALE_FACTOR * magnitudeSize(s);
            double x = transformedStarPos[i];
            double y = transformedStarPos[i+1];
            this.fillCircle(x,y,diameter, starColor);

            i+=2;
        }


    }

    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {}

    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
    }

    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) { }

    public void drawHorizon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {}


    private void fillCircle(double x, double y, double diameter, Color fillColor){
        this.graphicContext.setFill(fillColor);
        this.graphicContext.fillOval(x,y, diameter, diameter);
    }

    static private double magnitudeSize(CelestialObject celestialObject){
        double clippedMag = ClosedInterval.of(-2,5).clip(celestialObject.magnitude());
        double sizeFactor = (99.0 - 17.0 * clippedMag) / 144.0;
        double diameter = sizeFactor * StereographicProjection.applyToAngle(Angle.ofDeg(0.5));
        return diameter;
    }

}
