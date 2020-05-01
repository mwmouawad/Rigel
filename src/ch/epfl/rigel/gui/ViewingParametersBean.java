package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.ObjectProperty;

public class ViewingParametersBean {

    private final ObjectProperty<Double> fieldOfViewDeg;
    //TODO: what type of coord ?
    private final ObjectProperty<HorizontalCoordinates> center;

    public ViewingParametersBean(ObjectProperty<Double> fieldOfViewDeg, ObjectProperty<HorizontalCoordinates> center) {
        this.fieldOfViewDeg = fieldOfViewDeg;
        this.center = center;
    }

    public ObjectProperty<Double> fieldOfViewDegProperty() { return fieldOfViewDeg; }

    public double getFieldOfViewDeg() { return fieldOfViewDeg.get().doubleValue(); }

    public ObjectProperty<HorizontalCoordinates> centerProperty() { return center; }

    public HorizontalCoordinates getCenter() { return center.get(); }
}
