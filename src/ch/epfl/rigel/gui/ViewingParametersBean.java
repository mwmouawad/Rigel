package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.CartesianCoordinates;
import javafx.beans.property.ObjectProperty;

public class ViewingParametersBean {

    private final ObjectProperty<Double> fieldOfViewDeg;
    //TODO: what type of coord ?
    private final ObjectProperty<CartesianCoordinates> center;

    public ViewingParametersBean(ObjectProperty<Double> fieldOfViewDeg, ObjectProperty<CartesianCoordinates> center) {
        this.fieldOfViewDeg = fieldOfViewDeg;
        this.center = center;
    }

    public ObjectProperty<Double> fieldOfViewDegProperty() { return fieldOfViewDeg; }

    public double getFieldOfViewDeg() { return fieldOfViewDeg.get().doubleValue(); }

    public ObjectProperty<CartesianCoordinates> centerProperty() { return center; }

    public CartesianCoordinates getCenter() { return center.get(); }
}
