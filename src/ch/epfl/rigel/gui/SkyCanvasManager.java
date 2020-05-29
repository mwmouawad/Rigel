package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;
import java.util.Optional;


/**
 * Manages the Java fx canvas for the sky. The Manager ensures the good functioning by listening
 * to events, updating and drawing the appropriate objects. Its main functions are:
 * Listens to changes/input from the user such as mouse position or keys pressed down.
 * Sets the appropriate bindings.
 * Uses SkyCanvasPainter to draw the objects into the sky upon updates.
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
final public class SkyCanvasManager {
    //Constants
    private final static double OBJECT_MOUSE_DISTANCE = 10;
    private final static double STEP_HORIZONTAL_PROJECTION_DEG = 10;
    private final static double STEP_VERTICAL_PROJECTION_DEG = 5;
    //Intervals
    private final static ClosedInterval FOV_INTERVAL = ClosedInterval.of(30, 150);
    private final static RightOpenInterval CENTER_AZDEG_INTERVAL = RightOpenInterval.of(0, 360);
    private final static ClosedInterval CENTER_ALTDEG_INTERVAL = ClosedInterval.of(5, 90);
    private final ViewingParametersBean viewingParameters;
    private final DateTimeBean dateTimeBean;
    //Private bindings
    private ObjectBinding<Transform> planeToCanvas;
    private ObjectBinding<ObservedSky> observedSky;
    private ObjectBinding<StereographicProjection> projection;
    private SimpleObjectProperty<Point2D> mousePosition;
    private ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition;
    private ObjectBinding<CelestialObject> objectUnderMouse;
    private final Canvas canvas;
    private final SkyCanvasPainter skyCanvasPainter;
    //Public bindings
    public DoubleBinding mouseAzDeg;
    public DoubleBinding mouseAltDeg;

    /**
     * Creates an instance of SkyCanvasManager. Creates an empty Canvas that needs be
     * used initialized.
     *
     * @param catalogue         the star catalogue.
     * @param dateTime          a DateTimeBean object for the current time.
     * @param observerLocation  a ObserverLocationBean with the current observer location coordinate properties.
     * @param viewingParameters bean contaning the parameters for the view:  FOV and the center of projection.
     */
    public SkyCanvasManager(StarCatalogue catalogue, DateTimeBean dateTime, ObserverLocationBean observerLocation, ViewingParametersBean viewingParameters) {
        this.canvas = new Canvas();
        this.viewingParameters = viewingParameters;
        this.dateTimeBean = dateTime;
        this.skyCanvasPainter = new SkyCanvasPainter(this.canvas);
        this.mousePosition = new SimpleObjectProperty<Point2D>(Point2D.ZERO);
        this.initBindings(catalogue, dateTime, observerLocation);
        this.initListeners();
    }

    /**
     * Draws the sky objects from the observedSky with the skyCanvasPainter.
     */
    public void drawSky() {
        this.skyCanvasPainter.clear();
        Transform planeToCanvas = this.planeToCanvas.get();
        this.skyCanvasPainter.drawStars(this.observedSky.get(), planeToCanvas, projection.get());
        this.skyCanvasPainter.drawPlanets(this.observedSky.get(), planeToCanvas, projection.get());
        this.skyCanvasPainter.drawSun(this.observedSky.get(), this.projection.get(), planeToCanvas);
        this.skyCanvasPainter.drawMoon(this.observedSky.get(), this.projection.get(), planeToCanvas);
        this.skyCanvasPainter.drawHorizon(this.projection.get(), planeToCanvas);
    }


    /**
     * Initiates all listeners for the manager.
     */
    private void initListeners() {
        //Listens to mouse movement and stores on mousePosition property
        this.canvas.setOnMouseMoved(event -> {
            this.mousePosition.set(
                    new Point2D(event.getX(), event.getY())
            );
        });

        //Listens for the arrows keys.
        this.canvas.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN) {
                this.translateVerticalProjectCenter(-STEP_VERTICAL_PROJECTION_DEG);
                event.consume();
            } else if (event.getCode() == KeyCode.UP) {
                this.translateVerticalProjectCenter(STEP_VERTICAL_PROJECTION_DEG);
                event.consume();
            } else if (event.getCode() == KeyCode.RIGHT) {
                this.translateHorizontalProjectCenter(STEP_HORIZONTAL_PROJECTION_DEG);
                event.consume();
            } else if (event.getCode() == KeyCode.LEFT) {
                this.translateHorizontalProjectCenter(-STEP_HORIZONTAL_PROJECTION_DEG);
                event.consume();
            }
        });

        //Listens to mouse press.
        this.canvas.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                this.canvas().requestFocus();
            }
        });

        //Listens for mouse scroll.
        this.canvas.setOnScroll(event -> {
            if (Math.abs(event.getDeltaX()) > Math.abs(event.getDeltaY())) {
                this.addFOV(event.getDeltaX());
            } else if (Math.abs(event.getDeltaX()) <= Math.abs(event.getDeltaY())) {
                this.addFOV(event.getDeltaY());
            }
        });

        //Whenever a change occurs, draw sky is called to update the view.
        this.observedSky.addListener((e) -> this.drawSky());
        this.planeToCanvas.addListener((e) -> this.drawSky());

    }

    /**
     * Init the bindings.
     * @param catalogue
     * @param dateTime
     * @param observerLocation
     */
    private void initBindings(StarCatalogue catalogue, DateTimeBean dateTime, ObserverLocationBean observerLocation) {

        this.projection = Bindings.createObjectBinding(
                () -> new StereographicProjection(this.viewingParameters.getCenter()),
                viewingParameters.centerProperty()
        );

        this.observedSky = Bindings.createObjectBinding(
                () -> new ObservedSky(dateTime.getZonedDateTime(), observerLocation.getCoordinates(), this.projection.getValue(), catalogue)
                , dateTime.dateProperty(), dateTime.zoneProperty(), dateTime.timeProperty(), observerLocation.coordinatesProperty(), this.projection);


        this.planeToCanvas = Bindings.createObjectBinding(
                () -> computePlaneToCanvas(this.viewingParameters)
                , canvas.widthProperty(), canvas.heightProperty(), this.viewingParameters.fieldOfViewDegProperty(), this.projection
        );

        this.mouseHorizontalPosition = Bindings.createObjectBinding(
                () -> this.computeMouseHorizontalPosition() == null ? HorizontalCoordinates.ofDeg(0, 0) : this.computeMouseHorizontalPosition()
                , this.mousePosition, this.projection, this.planeToCanvas
        );

        this.mouseAzDeg = Bindings.createDoubleBinding(
                () -> this.mouseHorizontalPosition.get().azDeg(),
                this.mouseHorizontalPosition
        );

        this.mouseAltDeg = Bindings.createDoubleBinding(
                () -> this.mouseHorizontalPosition.get().altDeg(),
                this.mouseHorizontalPosition
        );

        this.objectUnderMouse = Bindings.createObjectBinding(
                () -> this.computeObjectUnderMouse(),
                this.observedSky, this.mousePosition, this.planeToCanvas
        );
    }

    /**
     * Computes the object under mouse.
     * @return
     */
    private CelestialObject computeObjectUnderMouse() {
        Point2D mousePosInverse;
        double inverseDistance;

        try {
            inverseDistance = this.planeToCanvas.get().inverseDeltaTransform(new Point2D(OBJECT_MOUSE_DISTANCE, 0)).getX();
            mousePosInverse = this.planeToCanvas.get().inverseTransform(this.mousePosition.get());
        } catch (NonInvertibleTransformException error) {
            System.out.println(
                    String.format("Erreur de transformation inverse du point: %s avec erreur: %s", this.mousePosition.get(), error)
            );
            return null;
        }

        Optional<CelestialObject> celObj = this.observedSky.get().objectClosestTo(
                toCartesian(mousePosInverse), inverseDistance
        );

        return celObj.orElse(null);
    }

    /**
     * Computes the Horizontal Position of the mouse using the inverse projection.
     * @return
     */
    private HorizontalCoordinates computeMouseHorizontalPosition() {

        Point2D point2D;
        try {
            point2D = this.planeToCanvas.get()
                    .inverseTransform(this.mousePosition.get());
        }
        //TODO: See if exception handling is correct!
        catch (NonInvertibleTransformException error) {
            System.out.println(
                    String.format("Erreur de transformation inverse du point: %s avec erreur: %s", this.mousePosition.get(), error)
            );
            return null;
        }

        return this.projection.get().inverseApply(CartesianCoordinates.of(
                point2D.getX(), point2D.getY()
        ));


    }

    /**
     * Computes the plane to canvas Transform property.
     * @param viewingParameters
     * @return
     */
    private Transform computePlaneToCanvas(ViewingParametersBean viewingParameters) {
        double width = canvas.widthProperty().get();
        double height = canvas.heightProperty().get();
        double scale = width / projection.get().applyToAngle(Angle.ofDeg(viewingParameters.getFieldOfViewDeg()));
        Transform scaleTransform =  Transform.scale(scale, -scale);
        Transform translationTransform = Transform.translate(width /2.0d, height / 2.0d);
        return translationTransform.createConcatenation(scaleTransform);
    }


    /**
     * Sets field of view to current plus param{fovDeg}. Clips to the interval
     * [30,150].
     *
     * @param fovDeg degrees to be added the current FOV. Can be negative.
     */
    private void addFOV(double fovDeg) {
        double currentFOVDeg = this.viewingParameters.getFieldOfViewDeg();
        this.viewingParameters.setFieldOfViewDeg(
                FOV_INTERVAL.clip(currentFOVDeg + fovDeg)
        );
    }

    /**
     * Translates the center horizontally by the stepDeg argument.
     * @param stepDeg
     */
    private void translateHorizontalProjectCenter(double stepDeg) {
        HorizontalCoordinates currentCenter = this.viewingParameters.getCenter();
        double newAzDeg = CENTER_AZDEG_INTERVAL.reduce(currentCenter.azDeg() + stepDeg);
        HorizontalCoordinates newCenter = HorizontalCoordinates.ofDeg(newAzDeg, currentCenter.altDeg());
        this.viewingParameters.setCenter(newCenter);
    }

    /**
     * Translates the center vertically by the stepDeg Argument.
     * @param stepDeg
     */
    private void translateVerticalProjectCenter(double stepDeg) {
        HorizontalCoordinates currentCenter = this.viewingParameters.getCenter();
        double newAltDeg = CENTER_ALTDEG_INTERVAL.clip(currentCenter.altDeg() + stepDeg);
        HorizontalCoordinates newCenter = HorizontalCoordinates.ofDeg(currentCenter.azDeg(), newAltDeg);
        this.viewingParameters.setCenter(newCenter);
    }

    /**
     * Converts a Point2D argument to cartesian coordinates.
     * @param point2D
     * @return
     */
    private static CartesianCoordinates toCartesian(Point2D point2D) {
        return CartesianCoordinates.of(point2D.getX(), point2D.getY());
    }

    /**
     * Returns a property containing the CelestialObject currently under/close
     * to the mouse.
     *
     * @return a property containing the CelestialObject currently under/close
     * to the mouse.
     */
    public ObservableValue<CelestialObject> objectUnderMouseProperty() {
        return this.objectUnderMouse;
    }

    /**
     * Returns the canvas used by the SkyCanvasManager to draw the sky.
     *
     * @return the canvas used by the SkyCanvasManager to draw the sky.
     */
    public Canvas canvas() {
        return this.canvas;
    }
}

