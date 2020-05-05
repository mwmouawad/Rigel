package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.math.Angle;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableObjectValue;

import java.util.Objects;


/**
 * A class representing the observer location as a JavaFX bean. Will be used so the user
 * can change it's position and update the UI accordingly.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public class ObserverLocationBean {

    private final DoubleProperty lonDeg;
    private final DoubleProperty latDeg;
    private final ObjectProperty<GeographicCoordinates> coordinates;
    private final ObjectBinding<GeographicCoordinates> binding;

    /**
     * Constructs a observer location bean. Meant to be used by setting the values using
     * the available setters.
     */
    public ObserverLocationBean() {
        this.lonDeg = new SimpleDoubleProperty(0);
        this.latDeg = new SimpleDoubleProperty(0);
        this.coordinates = new SimpleObjectProperty();

        this.binding = Bindings.createObjectBinding(
                () -> (GeographicCoordinates.ofDeg(Objects.requireNonNull(this.lonDeg.getValue()),
                        Objects.requireNonNull(this.latDeg.getValue()))),
                this.lonDeg, this.latDeg);

        this.coordinates.bind(this.binding);
    }

    /**
     * Double property containing the longitude in degrees.
     * @return property containing the longitude in degrees.
     */
    public DoubleProperty lonDegProperty() { return lonDeg; }

    /**
     * Gets the longitude in degrees of the observer's location.
     * @return longitude in degrees of the observer's location.
     */
    public double getLonDeg() { return lonDeg.get(); }

    /**
     * Sets the longitude of the observer location in degrees.
     * @param lonDeg longitude in degrees to  set.
     */
    public void setLonDeg(double lonDeg) {this.lonDeg.set(lonDeg);}

    /**
     * Gets the property of the latitude in degrees
     * @return the property of the latitude in degrees.
     */
    public DoubleProperty latDegProperty() { return latDeg; }

    /**
     * Gets the latitude in degrees of the observer's location.
     * @return gets the latitude in degrees of the observer's location.
     */
    public double getLatDeg() { return latDeg.get(); }

    /**
     * Sets the latitude in degrees of the observer location.
     * @param latDeg the latitude in degrees to set.
     */
    public void setLatDeg(double latDeg) {this.latDeg.set(latDeg);}

    /**
     * Gets the observer's location in geographic coordinates property.
     * @return observer's location in geographic coordinates property.
     */
    public ObjectProperty<GeographicCoordinates> coordinatesProperty() { return coordinates; }

    /**
     * Gets the observer's location geographic coordinates.
     * @return observer's location geographic coordinates.
     */
    public GeographicCoordinates getCoordinates() { return coordinates.get(); }


   //TODO: Should it be possible to set directly?
    public void setCoordinates(GeographicCoordinates geoCoordinates){
        this.setLonDeg(geoCoordinates.lonDeg());
        this.setLatDeg(geoCoordinates.latDeg());
    }

}
