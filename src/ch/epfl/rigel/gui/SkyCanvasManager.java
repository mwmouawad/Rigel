package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;

public class SkyCanvasManager {

    private ObjectBinding planeToCanvas;
    private ObjectBinding<ObservedSky> observedSky;
    private ObjectBinding<StereographicProjection> projection;
    private SimpleObjectProperty<CartesianCoordinates> mousePosition;
    private ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition;
    public  DoubleBinding mouseAzDeg;
    public  DoubleBinding mouseAltDeg;
    public  ObjectBinding<CelestialObject> objectUnderMouse;
    public  Canvas canvas;

    public SkyCanvasManager(StarCatalogue catalogue, DateTimeBean dateTime, ViewingParametersBean viewingParameters, ObserverLocationBean observerLocation) {
        this.canvas = new Canvas();
        this.canvas.requestFocus();
        this.initProperties();
        this.initBindings(catalogue, dateTime, viewingParameters, observerLocation);
        this.initListeners();
    }

    private void initProperties(){
        this.mousePosition = new SimpleObjectProperty();
    }

    private void initBindings(StarCatalogue catalogue, DateTimeBean dateTime, ViewingParametersBean viewingParameters, ObserverLocationBean observerLocation) {

        this.projection = Bindings.createObjectBinding(() -> new StereographicProjection(viewingParameters.getCenter()), viewingParameters.centerProperty());
        this.planeToCanvas = Bindings.createObjectBinding(
                () -> (canvas.widthProperty().get() / 2 * Math.tan(viewingParameters.getFieldOfViewDeg()))
                , canvas.widthProperty(), canvas.heightProperty(), viewingParameters.fieldOfViewDegProperty()
        );
        this.observedSky = Bindings.createObjectBinding(
                () -> new ObservedSky(dateTime.getZonedDateTime(), observerLocation.getCoordinates(), this.projection.getValue(), catalogue)
                , dateTime.dateProperty(), observerLocation.coordinatesProperty(), this.projection);
    }


    private void initListeners(){
        this.canvas.setOnKeyPressed( event -> {

        });

        this.canvas.setOnMousePressed( event -> {});
        this.canvas.setOnMouseMoved( event ->  {
            this.mousePosition.set(
                    CartesianCoordinates.of(
                            event.getX(),
                            event.getY()
                    )
            );
        });

        this.canvas.setOnScroll( event -> {
        });

    }


}

