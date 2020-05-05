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
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;
import javafx.geometry.Point2D;
import java.util.Optional;


public class SkyCanvasManager {
    //Constants
    private final static double OBJECT_MOUSE_DISTANCE = 10;
    private final static ClosedInterval FOV_INTERVAL = ClosedInterval.of(30,150);
    private final static double STEP_HORIZONTAL_PROJECTION_DEG = 10;
    private final static double STEP_VERTICAL_PROJECTION_DEG = 5;
    //TODO: Use it with 360?
    private final static RightOpenInterval CENTER_AZDEG_INTERVAL = RightOpenInterval.of(0,359);
    private final static ClosedInterval CENTER_ALTDEG_INTERVAL = ClosedInterval.of(5,90);
    private final ViewingParametersBean viewingParameters;
    //Private bindings
    private ObjectBinding<Transform> planeToCanvas;
    private ObjectBinding<ObservedSky> observedSky;
    private ObjectBinding<StereographicProjection> projection;
    private SimpleObjectProperty<Point2D> mousePosition;
    private ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition;
    private ObjectBinding<CelestialObject> objectUnderMouse;
    //Public bindings
    public DoubleBinding mouseAzDeg;
    public DoubleBinding mouseAltDeg;
    private final Canvas canvas;
    private SkyCanvasPainter skyCanvasPainter;


    public SkyCanvasManager(StarCatalogue catalogue, DateTimeBean dateTime, ObserverLocationBean observerLocation, ViewingParametersBean viewingParameters) {
        this.canvas = new Canvas();
        this.viewingParameters = viewingParameters;
        this.skyCanvasPainter = new SkyCanvasPainter(this.canvas);
        this.mousePosition = new SimpleObjectProperty(Point2D.ZERO);
        this.initBindings(catalogue, dateTime, observerLocation);
        this.initListeners();
    }

    public void drawSky(){
        this.skyCanvasPainter.clear();
        Transform planeToCanvas = this.planeToCanvas.get();
        this.skyCanvasPainter.drawStars(this.observedSky.get(), planeToCanvas);
        this.skyCanvasPainter.drawPlanets(this.observedSky.get(), planeToCanvas);
        this.skyCanvasPainter.drawSun(this.observedSky.get(), this.projection.get(), planeToCanvas);
        this.skyCanvasPainter.drawMoon(this.observedSky.get(),this.projection.get(), planeToCanvas);
        this.skyCanvasPainter.drawHorizon(this.projection.get(), planeToCanvas);
    }


    private void initListeners() {
        //Listens to mouse movement and stores on mousePosition property
        this.canvas.setOnMouseMoved(event -> {
            this.mousePosition.set(
                    new Point2D(event.getX(), event.getY())
            );
        });

        this.canvas.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.DOWN){
                this.translateVerticalProjectCenter(-STEP_VERTICAL_PROJECTION_DEG);
                event.consume();
            }
            else if(event.getCode() == KeyCode.UP){
                this.translateVerticalProjectCenter(STEP_VERTICAL_PROJECTION_DEG);
                event.consume();
            }
            else if(event.getCode() == KeyCode.RIGHT){
                this.translateHorizontalProjectCenter(STEP_HORIZONTAL_PROJECTION_DEG);
                event.consume();
            }
            else if(event.getCode() == KeyCode.LEFT){
                this.translateHorizontalProjectCenter(-STEP_HORIZONTAL_PROJECTION_DEG);
                event.consume();
            }
        });

        this.canvas.setOnMousePressed(event -> {
            if(event.isPrimaryButtonDown()){
                this.canvas().requestFocus();
            }
        });


        this.canvas.setOnScroll(event -> {
            if(Math.abs(event.getDeltaX()) > Math.abs(event.getDeltaY())){
                this.addFOV(event.getDeltaX());
            }
            else if(Math.abs(event.getDeltaX()) < Math.abs(event.getDeltaY())){
                this.addFOV(event.getDeltaY());
            }
        });

        this.viewingParameters.centerProperty().addListener((e) -> { this.drawSky(); });

        this.viewingParameters.fieldOfViewDegProperty().addListener((e) -> { this.drawSky(); });

        this.observedSky.addListener((e) -> this.drawSky());

        this.planeToCanvas.addListener((e) -> this.drawSky());

    }

    private void initBindings(StarCatalogue catalogue, DateTimeBean dateTime, ObserverLocationBean observerLocation) {

        this.projection = Bindings.createObjectBinding(
                () ->  new StereographicProjection(this.viewingParameters.getCenter()),
                viewingParameters.centerProperty()
        );

        this.planeToCanvas = Bindings.createObjectBinding(
                () -> computePlaneToCanvas(this.viewingParameters)
                , canvas.widthProperty(), canvas.heightProperty(), this.viewingParameters.fieldOfViewDegProperty(), this.projection
        );

        this.observedSky = Bindings.createObjectBinding(
                () -> new ObservedSky(dateTime.getZonedDateTime(), observerLocation.getCoordinates(), this.projection.getValue(), catalogue)
                , dateTime.dateProperty(), observerLocation.coordinatesProperty(), this.projection);

        this.mouseHorizontalPosition = Bindings.createObjectBinding(
                () -> this.computeMouseHorizontalPosition()
                ,this.mousePosition, this.projection, this.planeToCanvas
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

    private CelestialObject  computeObjectUnderMouse(){
        Point2D mousePosInverse = new Point2D(0,0);
        double inverseDistance = 0.0;

        //TODO: Find a better exception handling is correct!
        try {
             inverseDistance = this.planeToCanvas.get().inverseDeltaTransform(new Point2D(OBJECT_MOUSE_DISTANCE, 0)).getX();
             mousePosInverse = this.planeToCanvas.get().inverseTransform(this.mousePosition.get());
        }
        catch (NonInvertibleTransformException error){
            System.out.println(
                    String.format("Erreur de transformation inverse du point: %s avec erreur: %s", this.mousePosition.get(), error)
            );
        }

        Optional<CelestialObject> celObj = this.observedSky.get().objectClosestTo(
                toCartesian(mousePosInverse), inverseDistance);

        return celObj.orElse(null);
    }

    private HorizontalCoordinates computeMouseHorizontalPosition(){

        //On prend la transformation inverse des coordonnées de l'écran.
        Point2D point2D = new Point2D(0,0);
        try{
             point2D = this.planeToCanvas.get()
                    .inverseTransform(this.mousePosition.get());
        }
        //TODO: See if exception handling is correct!
        catch (NonInvertibleTransformException error){
            System.out.println(
                    String.format("Erreur de transformation inverse du point: %s avec erreur: %s", this.mousePosition.get(), error)
            );
        }

        //Puis on prend la transformation inverse, afin d'avoir les coordonnées horizontales.
        HorizontalCoordinates horPoint = this.projection.get().inverseApply(CartesianCoordinates.of(point2D.getX(), point2D.getY()));

        return horPoint;
    }

    private Transform computePlaneToCanvas(ViewingParametersBean viewingParameters) {

        //TODO: How handle when canvas are still width and height are still 0
        double width = canvas.widthProperty().get();
        double height = canvas.heightProperty().get();
        double scale = width / projection.get().applyToAngle(Angle.ofDeg(viewingParameters.getFieldOfViewDeg()));
        return Transform.affine(scale, 0, 0, -scale, width/2, height/2);

    }


    /**
     * Sets field of view to current plus param{fovDeg}. Clips to the interval
     * [30,150].
     * @param fovDeg degrees to be added the current FOV. Can be negative.
     */
    private void addFOV(double fovDeg){
        double currentFOVDeg = this.viewingParameters.getFieldOfViewDeg();
        this.viewingParameters.setFieldOfViewDeg(
                FOV_INTERVAL.clip(currentFOVDeg + fovDeg)
        );
    }

    private void translateHorizontalProjectCenter(double stepDeg){
        HorizontalCoordinates currentCenter = this.viewingParameters.getCenter();
        double newAzDeg = CENTER_AZDEG_INTERVAL.reduce(currentCenter.azDeg() + stepDeg);
        System.out.println("New AZ DEf: "  + newAzDeg);

        HorizontalCoordinates newCenter = HorizontalCoordinates.ofDeg(newAzDeg,currentCenter.altDeg());
        System.out.println("New Horizontal coordinates: " + newCenter );

        this.viewingParameters.setCenter(newCenter);
    }

    private void translateVerticalProjectCenter(double stepDeg){
        System.out.println("Translating vertical with step: " + stepDeg);
        HorizontalCoordinates currentCenter = this.viewingParameters.getCenter();
        double newAltDeg = CENTER_ALTDEG_INTERVAL.clip(currentCenter.altDeg() + stepDeg);

        HorizontalCoordinates newCenter = HorizontalCoordinates.ofDeg(currentCenter.azDeg(),newAltDeg);
        System.out.println("New Horizontal coordinates: " + newCenter );

        this.viewingParameters.setCenter(newCenter);
    }

    private static CartesianCoordinates toCartesian(Point2D point2D){
        return CartesianCoordinates.of(point2D.getX(), point2D.getY());
    }

    private static Point2D toPoint2D(CartesianCoordinates coordinates){
        return new Point2D(coordinates.x(), coordinates.y());
    }


    public ObservableValue<CelestialObject> objectUnderMouseProperty() {
        return this.objectUnderMouse;
    }

    public Canvas canvas() {
        return this.canvas;
    }
}

