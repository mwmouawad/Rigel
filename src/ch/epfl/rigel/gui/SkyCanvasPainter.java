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

import java.util.List;
import java.util.Set;

/**
 * Class encapsulating methods to draw the stars, sky, planets,
 * asterisms and horizon. Uses JavaFX Graphics Context to draw.
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
final public class SkyCanvasPainter {

    final Canvas canvas;
    final GraphicsContext graphicContext;
    private Color BACKGROUND_COLOR_EARLY_NIGHT = Color.MIDNIGHTBLUE;
    private Color BACKGROUND_COLOR_DARK_NIGHT = Color.BLACK;
    private ClosedInterval DARK_NIGHT_INTERVAL = ClosedInterval.of(0, 3);
    private ClosedInterval EARLY_NIGHT_INTERVAL = ClosedInterval.of(4, 5);
    private Color BACKGROUND_COLOR_MID_MORNING = Color.SKYBLUE;
    private ClosedInterval MID_MORNING_INTERVAL = ClosedInterval.of(8, 9);
    private Color BACKGROUND_COLOR_EARLY_MORNING = Color.SKYBLUE;
    private ClosedInterval EARLY_MORNING_INTERVAL = ClosedInterval.of(6, 7);
    private Color BACKGROUND_COLOR_EVENING = Color.NAVY;
    private ClosedInterval EVENING_INTERVAL = ClosedInterval.of(18, 20);
    private Color BACKGROUND_COLOR_DAY = Color.STEELBLUE;
    private ClosedInterval DAY_INTERVAL = ClosedInterval.of(10, 17);
    static final private ClosedInterval CLIP_INTERVAL_MAG = ClosedInterval.of(-2, 5);
    static final private double CLIP_MAG_FACTOR = 17.0;
    static final private double CLIP_MAG_ALPHA = 99.0;
    static final private double CLIP_MAG_BETA = 140;
    static final private double CLIP_MAG_APPARENT_SIZE = Angle.ofDeg(0.5);
    static final private Color SUN_INNER_CENTER_COLOR = Color.WHITE;
    static final private Color SUN_OUTER_CENTER_COLOR = Color.YELLOW;
    static final private Color SUN_OUTER_COLOR = Color.YELLOW.deriveColor(0, 1.0f, 1.0f, 0.25f);
    static final private Color ASTERISM_COLOR = Color.BLUE;
    static final private Color PLANET_COLOR = Color.LIGHTGRAY;
    static final private Color MOON_COLOR = Color.WHITE;
    static final private Color HORIZON_COLOR = Color.RED;
    static final private HorizontalCoordinates PARALLEL_COORDINATES = HorizontalCoordinates.of(0, 0);
    static final private double CIRCLE_EIGHTH_DEGREE = 45;


    /**
     * Builds a SkyCanvasPainter instance.
     * Expects a canvas to draw onto.
     *
     * @param canvas the canvas to draw to.
     */
    public SkyCanvasPainter(Canvas canvas, int hour) {
        this.canvas = canvas;
        this.graphicContext = canvas.getGraphicsContext2D();
        this.clear(hour);
    }

    /**
     * Clears the canvas.
     */
    public void clear(int hour) {
        this.graphicContext.setFill(skyColor(hour));
        this.graphicContext.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
    }

    public Color skyColor(int hour) {

        if (MID_MORNING_INTERVAL.contains(hour)) {
            return BACKGROUND_COLOR_MID_MORNING.brighter();
        } else if(EARLY_MORNING_INTERVAL.contains(hour)) {
            return BACKGROUND_COLOR_EARLY_MORNING;
        } else if(EVENING_INTERVAL.contains(hour)) {
            return BACKGROUND_COLOR_EVENING.brighter();
        } else if(DAY_INTERVAL.contains(hour)) {
            return BACKGROUND_COLOR_DAY;
        } else if(DARK_NIGHT_INTERVAL.contains(hour)) {
            return BACKGROUND_COLOR_DARK_NIGHT.brighter();
        } else if(EARLY_NIGHT_INTERVAL.contains(hour)) {
            return BACKGROUND_COLOR_EARLY_NIGHT.brighter();
        } else return BACKGROUND_COLOR_EARLY_NIGHT.darker();

    }



    /**
     * Draws all stars from ObservedSky to the Canvas. Uses
     * a given transform matrix to convert to the screen coordinate system.
     * Asterisms are draw before the stars.
     *
     * @param sky           the ObservedSky instance containing the given sky to draw.
     * @param planeToCanvas the transform matrix for the conversion.
     */
    public void drawStars(ObservedSky sky, Transform planeToCanvas, StereographicProjection projection) {
        double[] transformedStarPos = new double[sky.starPositions().length];
        planeToCanvas.transform2DPoints(sky.starPositions(), 0, transformedStarPos, 0, sky.stars().size());
        //Draw Asterisms.
        this.drawAsterisms(sky, transformedStarPos);
        //Draw Stars
        int i = 0;

        for (Star s : sky.stars()) {
            Color starColor = BlackBodyColor.colorForTemperature(s.colorTemperature());
            //Draw the element
            double diameter = planeToCanvas.deltaTransform(magnitudeSize(s, projection), 0).getX();
            double x = transformedStarPos[i];
            double y = transformedStarPos[i + 1];
            this.drawCircle(x, y, diameter, starColor);
            i += 2;
        }
    }

    /**
     * Draws all planets from ObservedSky to the Canvas. Uses
     * a given Transform matrix to convert to the screen coordinate system.
     *
     * @param sky           the ObservedSky instance containing the given sky to draw.
     * @param planeToCanvas the transform matrix for the conversion.
     */
    public void drawPlanets(ObservedSky sky, Transform planeToCanvas, StereographicProjection projection) {

        double[] transformedPlanetPos = new double[sky.planetPositions().length];
        planeToCanvas.transform2DPoints(sky.planetPositions(), 0, transformedPlanetPos, 0, sky.planets().size());

        int i = 0;
        for (Planet p : sky.planets()) {
            double diameter = planeToCanvas.deltaTransform(magnitudeSize(p, projection), 0).getX();
            double x = transformedPlanetPos[i];
            double y = transformedPlanetPos[i + 1];
            this.drawCircle(x, y, diameter, PLANET_COLOR);
            i += 2;
        }

    }

    /**
     * Draws the Sun from ObservedSky to the Canvas. Uses
     * a given Transform matrix to convert to the screen coordinate system.
     *
     * @param sky           the ObservedSky instance containing the given sky to draw.
     * @param planeToCanvas the transform matrix for the conversion.
     * @param projection    the stereographic projection used to apply to the Sun diameter.
     */
    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

        Point2D transformedSunPos = planeToCanvas.transform(
                sky.sunPosition().x(),
                sky.sunPosition().y()
        );
        double diameter = planeToCanvas.deltaTransform(
                projection.applyToAngle(sky.sun().angularSize()), 0
        ).getX();


        double outerDiameter = 2.2 * diameter;
        double outerCenterDiameter = diameter + 2.0;
        double sunX = transformedSunPos.getX();
        double sunY = transformedSunPos.getY();

        this.drawCircle(sunX, sunY, outerDiameter, SUN_OUTER_COLOR);
        this.drawCircle(sunX, sunY, outerCenterDiameter, SUN_OUTER_CENTER_COLOR);
        this.drawCircle(sunX, sunY, diameter, SUN_INNER_CENTER_COLOR);

    }

    /**
     * Draws the Moon from ObservedSky to the Canvas. Uses
     * a given Transform matrix to convert to the screen coordinate system.
     *
     * @param sky           the ObservedSky instance containing the given sky to draw.
     * @param planeToCanvas the transform matrix for the conversion.
     * @param projection    the stereographic projection used to apply to the Moon diameter.
     */
    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

        Point2D transformedPos = planeToCanvas.transform(sky.moonPosition().x(), sky.moonPosition().y());

        double diameter = planeToCanvas.deltaTransform(
                projection.applyToAngle(sky.moon().angularSize()), 0
        ).getX();

        this.drawCircle(transformedPos.getX(), transformedPos.getY(), diameter, MOON_COLOR);
    }

    /**
     * Draws the Horizon line to the Canvas. Uses
     * a given Transform matrix to convert to the screen coordinate system.
     *
     * @param planeToCanvas the transform matrix for the conversion.
     * @param projection    the stereographic projection used to apply to the Moon diameter.
     */
    public void drawHorizon(StereographicProjection projection, Transform planeToCanvas) {
        double diameter = 2 * projection.circleRadiusForParallel(PARALLEL_COORDINATES);
        double transformedDiameter = planeToCanvas.deltaTransform(diameter, 0).getX();
        double radius = transformedDiameter / 2;

        CartesianCoordinates coordinates = projection.circleCenterForParallel(PARALLEL_COORDINATES);
        Point2D transformedPos = planeToCanvas.transform(coordinates.x(), coordinates.y());

        graphicContext.setStroke(HORIZON_COLOR);
        graphicContext.setLineWidth(2);
        graphicContext.strokeOval(transformedPos.getX() - radius,
                transformedPos.getY() - radius, transformedDiameter, transformedDiameter);

        for (int i = 0; i < 8; i++) {
            HorizontalCoordinates horiz = HorizontalCoordinates.ofDeg(i * CIRCLE_EIGHTH_DEGREE, 0);
            CartesianCoordinates coord = projection.apply(horiz);
            Point2D point = planeToCanvas.transform(coord.x(), coord.y());
            this.drawText(
                    horiz.azOctantName("N", "E", "S", "O"),
                    point.getX(),
                    point.getY(),
                    HORIZON_COLOR,
                    VPos.TOP
            );
        }
    }


    /**
     * Method used along draw stars. Draw the asterisms.
     *
     * @param sky
     * @param transformedStarPos
     */
    private void drawAsterisms(ObservedSky sky, double[] transformedStarPos) {

        Set<Asterism> asterismSet = sky.asterisms();
        //Draw Asterisms

        for (Asterism ast : asterismSet) {
            List<Integer> indexList = sky.asterismIndices(ast);
            this.graphicContext.setStroke(ASTERISM_COLOR);
            this.graphicContext.beginPath();
            boolean isPreviousStarOnScreen = true;
            boolean isCurrentStarOnScreen = true;
            double nameX = 0.0;
            double nameY = 0.0;

            for (int i = 0; i < indexList.size(); i++) {
                int index = indexList.get(i);
                double x = transformedStarPos[2 * index];
                double y = transformedStarPos[2 * index + 1];

                if (i == 0) {
                    this.graphicContext.moveTo(x, y);
                    nameX = x ;
                    nameY = y ;
                } else {
                    isCurrentStarOnScreen = this.isInScreen(x, y);
                    if (isCurrentStarOnScreen || isPreviousStarOnScreen) this.graphicContext.lineTo(x, y);
                    else this.graphicContext.moveTo(x, y);

                }
                isPreviousStarOnScreen = isCurrentStarOnScreen;
            }
            this.graphicContext.stroke();
            this.graphicContext.closePath();

            if(this.isInScreen(nameX + 10, nameY + 5))
                this.drawText(ast.getName(),nameX + 10,nameY + 5, Color.WHITE,VPos.CENTER);
        }

    }

    /**
     * Helper method for drawing a circle.
     *
     * @param x
     * @param y
     * @param diameter
     * @param fillColor
     */
    private void drawCircle(double x, double y, double diameter, Color fillColor) {
        double radius = diameter / 2.0d;
        this.graphicContext.setFill(fillColor);
        this.graphicContext.fillOval(x - radius, y - radius, diameter, diameter);
    }

    /**
     * Helper method for drawing text.
     *
     * @param text
     * @param x
     * @param y
     * @param color
     * @param vPos
     */
    private void drawText(String text, double x, double y, Color color, VPos vPos) {
        graphicContext.setFill(color);
        graphicContext.fillText(text, x, y);
        graphicContext.setTextBaseline(vPos);
    }

    /**
     * Computes the magnitude size technique for the stars and planets diameter.
     *
     * @param celestialObject
     * @param projection
     * @return
     */
    static private double magnitudeSize(CelestialObject celestialObject, StereographicProjection projection) {
        double clippedMag = CLIP_INTERVAL_MAG.clip(celestialObject.magnitude());
        double sizeFactor = (CLIP_MAG_ALPHA - CLIP_MAG_FACTOR * clippedMag) / CLIP_MAG_BETA;
        return sizeFactor * projection.applyToAngle(CLIP_MAG_APPARENT_SIZE);
    }

    /**
     * Check if  position is  within the screen boundaries.
     * Helper method
     *
     * @return true if current position is within the screen boundaries. Otherwise false.
     */
    private boolean isInScreen(double x, double y) {
        return canvas.getBoundsInLocal().contains(x, y);
    }

}
