package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.math.Angle;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableObjectValue;

public class ObserverLocationBean {

    //TODO: final?
    private final ObjectProperty<Double> lonDeg;
    private final ObjectProperty<Double> latDeg;
    private final ObjectProperty<GeographicCoordinates> coordinates;

    public ObserverLocationBean(ObjectProperty<Double> lonDeg, ObjectProperty<Double> latDeg) {
        this.lonDeg = lonDeg;
        this.latDeg = latDeg;
        //TODO: right way?
        ObservableObjectValue objectValue = Bindings.createObjectBinding((() -> GeographicCoordinates.ofDeg(lonDeg.getValue(), latDeg.getValue())),
                lonDeg, latDeg);
        this.coordinates = (ObjectProperty<GeographicCoordinates>) ObjectProperty.objectExpression(objectValue);
    }

    public ObjectProperty<Double> lonDegProperty() { return lonDeg; }

    public double getLonDeg() { return lonDeg.get().doubleValue(); }

    public ObjectProperty<Double> latDegProperty() { return latDeg; }

    public double getLatDeg() { return latDeg.get().doubleValue(); }

    public ObjectProperty<GeographicCoordinates> coordinatesProperty() { return coordinates; }

    public GeographicCoordinates getCoordinates() { return coordinates.get(); }

}
