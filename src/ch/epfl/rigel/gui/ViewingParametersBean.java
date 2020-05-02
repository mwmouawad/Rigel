package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ViewingParametersBean {

    //TODO: REVOIR SYNTAXe DES NOMS SI COMPATIBLE AVEC JAVAFX BEANS
    //TODO: Should it be final?

    private final DoubleProperty fieldOfViewDeg;
    private final ObjectProperty<HorizontalCoordinates> center;

    public ViewingParametersBean() {
        this.fieldOfViewDeg = new SimpleDoubleProperty();
        this.center = new SimpleObjectProperty();
    }

    public DoubleProperty fieldOfViewDegProperty() { return fieldOfViewDeg; }

    public double getFieldOfViewDeg() { return fieldOfViewDeg.getValue(); }

    public void setFieldOfViewDeg(double fov){
        this.fieldOfViewDeg.setValue(fov);
    }

    public ObjectProperty<HorizontalCoordinates> centerProperty() { return center; }

    public HorizontalCoordinates getCenter() { return center.get(); }

    public void setCenter(HorizontalCoordinates coordinates){
        this.center.set(coordinates);
    }


}
