package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.ObjectProperty;

public class ViewingParametersBean {

    //TODO: REVOIR SYNTAXe DES NOMS SI COMPATIBLE AVEC JAVAFX BEANS
    //TODO: Should it be final?

    private final ObjectProperty<Double> fieldOfViewDeg;
    private final ObjectProperty<HorizontalCoordinates> center;

    public ViewingParametersBean(ObjectProperty<Double> fieldOfViewDeg, ObjectProperty<HorizontalCoordinates> center) {
        this.fieldOfViewDeg = fieldOfViewDeg;
        this.center = center;
    }

    public ObjectProperty<Double> fieldOfViewDegProperty() { return fieldOfViewDeg; }

    public double getFieldOfViewDeg() { return fieldOfViewDeg.get().doubleValue(); }

    public ObjectProperty<HorizontalCoordinates> centerProperty() { return center; }

    public HorizontalCoordinates getCenter() { return center.get(); }

    public void setFieldOfView(Double fov){
        this.fieldOfViewDeg.set(fov);
    }

    public void setCenter(HorizontalCoordinates coordinates){
        this.center.set(coordinates);
    }

}
