package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.scene.canvas.Canvas;


public class SkyCanvasManager {

    public SkyCanvasManager(StarCatalogue catalogue, DateTimeBean dateTime, ViewingParametersBean viewingParameters,
                            ObserverLocationBean observerLocation) {
        Canvas canvas = new Canvas();
        SkyCanvasPainter painter = new SkyCanvasPainter(canvas);

        canvas.setOnMouseMoved((event) ->  {
            if(event.isPrimaryButtonDown()) canvas.requestFocus();
        });

        StereographicProjection projection = new StereographicProjection(viewingParameters.getCenter());

        //ObjectProperty<HorizontalCoordinates> mousePosition = Bindings.createObjectBinding(() -> )



    }
}

