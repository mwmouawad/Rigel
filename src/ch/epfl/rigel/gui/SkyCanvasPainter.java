package ch.epfl.rigel.gui;


import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import javafx.scene.shape.Circle;

import java.util.List;
import java.util.Set;

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

    //TODO: Add feature to not draw star path when they are outside of canvas!
    private void drawAsterisms(ObservedSky sky, double[] transformedStarPos){

        Set<Asterism> asterismSet = sky.asterisms();
        //Draw Asterisms

        for(Asterism ast: asterismSet){
            List<Integer> indexList = sky.asterismIndices(ast);
            this.graphicContext.setStroke(Color.BLUE);
            this.graphicContext.beginPath();
            for(int i = 0; i < indexList.size(); i++){
                int index = indexList.get(i);
                Star s = sky.stars().get(index);
                double x = transformedStarPos[2*index];
                double y = transformedStarPos[2*index+1];
                if(i==0) this.graphicContext.moveTo(x,y);
                else this.graphicContext.lineTo(x,y);
            }
            this.graphicContext.stroke();
            this.graphicContext.closePath();

        }

    }

    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        double[] transformedStarPos = new double[sky.starPositions().length];
        planeToCanvas.transform2DPoints(sky.starPositions(), 0, transformedStarPos,0,sky.stars().size() );
        this.drawAsterisms(sky, transformedStarPos);
        //Draw Stars
        int i = 0;
        this.graphicContext.setFill(Color.RED);
        this.graphicContext.beginPath();

        for(Star s: sky.stars()){
            Color starColor = BlackBodyColor.colorForTemperature(s.colorTemperature());

            //Draw the element
            double diameter = planeToCanvas.deltaTransform(magnitudeSize(s), 0).getX();
            double x = transformedStarPos[i];
            double y = transformedStarPos[i+1];
            this.fillCircle(x,y,diameter,starColor);
            i+=2;
        }
    }

    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        /*
        double[] transformedStarPos = new double[sky.starPositions().length];
        planeToCanvas.transform2DPoints(sky.starPositions(), 0, transformedStarPos, 0, sky.stars().size());

        int i = 0;
        for (Star s : sky.stars()) {
            Color starColor = BlackBodyColor.colorForTemperature(s.colorTemperature());

            //Draw the element
            double diameter = SCALE_FACTOR * magnitudeSize(s);
            double x = transformedStarPos[i];
            double y = transformedStarPos[i + 1];
            this.fillCircle(x, y, diameter, starColor);

<<<<<<< HEAD
=======
            i += 2;
        }

         */
>>>>>>> 58162b54ce74184a92ad192298b66eeee8ad9231
    }


    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
    }

    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

        Point2D transformedPos = planeToCanvas.transform(sky.moonPosition().x(), sky.moonPosition().y());
        Color moonColor = Color.WHITE;

        double diameter = planeToCanvas.deltaTransform(
                projection.applyToAngle(sky.moon().angularSize()), 0
        ).getX();

        this.fillCircle(transformedPos.getX(), transformedPos.getY(), diameter, moonColor);

    }

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
