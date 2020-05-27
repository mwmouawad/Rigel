package ch.epfl.rigel.gui;


import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//TODO: Constants!!! MAGIC NUMBERS!!
/**
 * Class encapsulating methods to draw the stars, sky, planets,
 * asterisms and horizon. Uses JavaFX Graphics Context.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public class SkyCanvasPainter {

    Canvas canvas;
    GraphicsContext graphicContext;
    private Color BACKGROUND_COLOR = Color.BLACK;
    private double SCALE_FACTOR = 1300;

    /**
     * Builds a SkyCanvasPainter instance.
     * Expects a canvas to draw onto.
     * @param canvas the canvas to draw to.
     */
    public SkyCanvasPainter(Canvas canvas) {
        this.canvas = canvas;
        this.graphicContext = canvas.getGraphicsContext2D();
        this.graphicContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Clears the canvas.
     */
    public void clear() {
        this.graphicContext.setFill(BACKGROUND_COLOR);
        this.graphicContext.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
    }


    /**
     * Draws all stars from ObservedSky to the Canvas. Uses
     * a given transform matrix to convert to the screen coordinate system.
     * @param sky the ObservedSky instance containing the given sky to draw.
     * @param planeToCanvas the transform matrix for the conversion.
     */
    public void drawStars(ObservedSky sky, Transform planeToCanvas, StereographicProjection projection) {
        double[] transformedStarPos = new double[sky.starPositions().length];
        planeToCanvas.transform2DPoints(sky.starPositions(), 0, transformedStarPos, 0, sky.stars().size());
        this.drawAsterisms(sky, transformedStarPos);
        //Draw Stars
        int i = 0;
        this.graphicContext.setFill(Color.RED);
        this.graphicContext.beginPath();

        for (Star s : sky.stars()) {
            Color starColor = BlackBodyColor.colorForTemperature(s.colorTemperature());


            //Draw the element
            double diameter = planeToCanvas.deltaTransform(magnitudeSize(s, projection), 0).getX();
            double x = transformedStarPos[i];
            double y = transformedStarPos[i + 1];
            this.fillCircle(x, y, diameter, starColor);
            i += 2;
        }
    }

    //TODO : check if right

    /**
     * Draws all planets from ObservedSky to the Canvas. Uses
     * a given Transform matrix to convert to the screen coordinate system.
     * @param sky the ObservedSky instance containing the given sky to draw.
     * @param planeToCanvas the transform matrix for the conversion.
     */
    public void drawPlanets(ObservedSky sky, Transform planeToCanvas, StereographicProjection projection) {

        double[] transformedPlanetPos = new double[sky.planetPositions().length];
        planeToCanvas.transform2DPoints(sky.planetPositions(), 0, transformedPlanetPos, 0, sky.planets().size());

        int i = 0;
        for (Planet p : sky.planets()) {
            Color color = Color.LIGHTGRAY;
            double diameter = planeToCanvas.deltaTransform(magnitudeSize(p, projection), 0).getX();
            double x = transformedPlanetPos[i];
            double y = transformedPlanetPos[i + 1];
            this.fillCircle(x, y, diameter, color);
            i += 2;
        }


    }

    /**
     * Draws the Sun from ObservedSky to the Canvas. Uses
     * a given Transform matrix to convert to the screen coordinate system.
     * @param sky the ObservedSky instance containing the given sky to draw.
     * @param planeToCanvas the transform matrix for the conversion.
     * @param projection the stereographic projection used to apply to the Sun diameter.
     */
    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

        Point2D transformedSunPos = planeToCanvas.transform(
                sky.sunPosition().x(),
                sky.sunPosition().y()
        );
        double diameter = planeToCanvas.deltaTransform(
                projection.applyToAngle(sky.sun().angularSize()), 0
        ).getX();


        //Third Circle
            //Color.YELLOW with opacity 25%
        Color lightYellow = Color.YELLOW.deriveColor(0,1.0f,1.0f,0.25f);
        this.fillCircle(transformedSunPos.getX(), transformedSunPos.getY(), 2.2 * diameter, lightYellow);

        //Second circle
        this.fillCircle(transformedSunPos.getX(), transformedSunPos.getY(), diameter + 2, Color.YELLOW);

        //First circle
        this.fillCircle(transformedSunPos.getX(), transformedSunPos.getY(), diameter, Color.WHITE);

    }

    /**
     * Draws the Moon from ObservedSky to the Canvas. Uses
     * a given Transform matrix to convert to the screen coordinate system.
     * @param sky the ObservedSky instance containing the given sky to draw.
     * @param planeToCanvas the transform matrix for the conversion.
     * @param projection the stereographic projection used to apply to the Moon diameter.
     */
    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

        Point2D transformedPos = planeToCanvas.transform(sky.moonPosition().x(), sky.moonPosition().y());
        Color moonColor = Color.WHITE;

        double diameter = planeToCanvas.deltaTransform(
                projection.applyToAngle(sky.moon().angularSize()), 0
        ).getX();

        this.fillCircle(transformedPos.getX(), transformedPos.getY(), diameter, moonColor);
    }

    /**
     * Draws the Horizon line to the Canvas. Uses
     * a given Transform matrix to convert to the screen coordinate system.
     * @param planeToCanvas the transform matrix for the conversion.
     * @param projection the stereographic projection used to apply to the Moon diameter.
     */
    public void drawHorizon(StereographicProjection projection, Transform planeToCanvas) {
        HorizontalCoordinates parallel = HorizontalCoordinates.of(0,0);
        double diameter = 2 * projection.circleRadiusForParallel(parallel);
        double transformedDiameter = planeToCanvas.deltaTransform(diameter, 0).getX();
        double radius = transformedDiameter/2;

        CartesianCoordinates coordinates  = projection.circleCenterForParallel(parallel);
        Point2D transformedPos = planeToCanvas.transform(coordinates.x(), coordinates.y());

        graphicContext.setStroke(Color.RED);
        graphicContext.setLineWidth(2);
        graphicContext.strokeOval(transformedPos.getX() - radius,
                transformedPos.getY() - radius, transformedDiameter, transformedDiameter);

        for(int i = 0; i < 8; i++){
            HorizontalCoordinates horiz = HorizontalCoordinates.ofDeg(i*45, 0);
            CartesianCoordinates coord = projection.apply(horiz);
            Point2D point = planeToCanvas.transform(coord.x(), coord.y());
            graphicContext.setFill(Color.RED);
            graphicContext.fillText(horiz.azOctantName("N", "E", "S", "O"), point.getX(), point.getY());
            graphicContext.setTextBaseline(VPos.TOP);
        }
    }


    /**
     * Method used along draw stars. Draw the asterisms.
     * @param sky
     * @param transformedStarPos
     */
    private void drawAsterisms(ObservedSky sky, double[] transformedStarPos) {

        Set<Asterism> asterismSet = sky.asterisms();
        //Draw Asterisms

        //TODO: Check if done correctly
        for (Asterism ast : asterismSet) {
            List<Integer> indexList = sky.asterismIndices(ast);
            this.graphicContext.setStroke(Color.BLUE);
            this.graphicContext.beginPath();
            boolean isPreviousStarOnScreen;
            boolean isCurrentStarOnScreen;
            int oldIndex = 0;

            for (int i = 0; i < indexList.size(); i++) {
                int index = indexList.get(i);
                Star s = sky.stars().get(index);
                double x = transformedStarPos[2 * index];
                double y = transformedStarPos[2 * index + 1];

                if(i == 0){
                    this.graphicContext.moveTo(x, y);
                }
                else{
                    isCurrentStarOnScreen = this.isInScreen(x ,y);
                    isPreviousStarOnScreen = this.isInScreen(transformedStarPos[2 * (oldIndex)],transformedStarPos[2 * (oldIndex) + 1] );
                    if(isCurrentStarOnScreen || isPreviousStarOnScreen) this.graphicContext.lineTo(x, y);
                    else  this.graphicContext.moveTo(x,y);
                }
                oldIndex = index;
            }
            this.graphicContext.stroke();
            this.graphicContext.closePath();

        }

    }

    /**
     * Helper method for drawing a circle.
     * @param x
     * @param y
     * @param diameter
     * @param fillColor
     */
    private void fillCircle(double x, double y, double diameter, Color fillColor) {
        double radius = diameter / 2.0d;
        this.graphicContext.setFill(fillColor);
        this.graphicContext.fillOval(x-radius, y-radius, diameter, diameter);
    }

    /**
     * Computes the magnitude size technique for the stars and planets diameter.
     * @param celestialObject
     * @param projection
     * @return
     */
    static private double magnitudeSize(CelestialObject celestialObject, StereographicProjection projection) {
        double clippedMag = ClosedInterval.of(-2, 5).clip(celestialObject.magnitude());
        double sizeFactor = (99.0 - 17.0 * clippedMag) / 144.0;
        return sizeFactor * projection.applyToAngle(Angle.ofDeg(0.5));
    }

    /**
     * Check if  position is  within the screen boundaries.
     * Helper method
     * @return true if current position is within the screen boundaries. Otherwise false.
     */
    private boolean isInScreen(double x, double y){

        return ( x >= 0 && x <= canvas.widthProperty().get() && y >= 0 && y <= canvas.heightProperty().get());
    }

}
