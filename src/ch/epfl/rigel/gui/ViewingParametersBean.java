package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * JavaFX Bean type class encapsulating viewing parameters for the projection.
 * (i.e Field Of View (FOV), center of the stereographic projection)
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
final public class ViewingParametersBean {

    private final DoubleProperty fieldOfViewDeg;
    private final ObjectProperty<HorizontalCoordinates> center;

    /**
     * Builds an instance of ViewingParametersBean by initializing
     * the center and FOV empty properties.
     */
    public ViewingParametersBean() {
        this.fieldOfViewDeg = new SimpleDoubleProperty();
        this.center = new SimpleObjectProperty<HorizontalCoordinates>();
    }

    /**
     * Returns the field of view (FOV) property.
     * @return field of view (FOV) property.
     */
    public DoubleProperty fieldOfViewDegProperty() {
        return fieldOfViewDeg;
    }

    /**
     * Returns the field of view (FOV) in degrees.
     * @return field of view (FOV) in degrees.
     */
    public double getFieldOfViewDeg() {
        return fieldOfViewDeg.getValue();
    }

    /**
     * Sets the field of view (FOV) in degrees property to the given input value.
     * @param fov field of view (FOV) in degrees to set.
     */
    public void setFieldOfViewDeg(double fov) {
        this.fieldOfViewDeg.setValue(fov);
    }

    /**
     * Returns the center of projection property.
     * @return the center of projection property.
     */
    public ObjectProperty<HorizontalCoordinates> centerProperty() {
        return center;
    }

    /**
     * Returns the center of projection horizontal coordinates.
     * @return Returns the center of projection horizontal coordinates.
     */
    public HorizontalCoordinates getCenter() {
        return center.get();
    }

    /**
     * Sets the center of projection to the given input value.
     * @param coordinates in horizontal coordinates system to set the center of projection.
     */
    public void setCenter(HorizontalCoordinates coordinates) {
        this.center.set(coordinates);
    }


}
