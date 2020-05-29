package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;

import java.util.Objects;

//TODO: Check if needs to add throws here.
/**
 * JavaFX Bean type class encapsulating the observer location. Will be used so the user
 * can change it's position and update the UI accordingly.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public class ObserverLocationBean {

    private final DoubleProperty lonDeg;
    private final DoubleProperty latDeg;
    private final ObjectProperty<GeographicCoordinates> coordinates;
    private final ObjectBinding<GeographicCoordinates> coordinatesBinding;

    /**
     * Constructs a observer location bean with empty coordinates properties.
     * Meant to be used by setting the values using the available setters.
     */
    public ObserverLocationBean() {
        this.lonDeg = new SimpleDoubleProperty(0);
        this.latDeg = new SimpleDoubleProperty(0);
        this.coordinates = new SimpleObjectProperty<GeographicCoordinates>();

        this.coordinatesBinding = Bindings.createObjectBinding(
                () -> (GeographicCoordinates.ofDeg(Objects.requireNonNull(this.lonDeg.getValue()),
                        Objects.requireNonNull(this.latDeg.getValue()))),
                this.lonDeg, this.latDeg);

        this.coordinates.bind(this.coordinatesBinding);
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
    public DoubleProperty latDegProperty() { return latDegProperty(); }

    /**
     * Gets the latitude in degrees of the observer's location.
     * @return gets the latitude in degrees of the observer's location.
     */
    public double getLatDeg() { return latDeg.get(); }

    /**
     * Sets the latitude in degrees of the observer location.
     * @param latDeg the latitude in degrees to set.
     * //TODO: Should add throws here?
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


    /**
     * Sets the Geographic coordinates in degrees of the observer.
     * @param geoCoordinates the geographic coordinates to set the observer location.
     */
    public void setCoordinates(GeographicCoordinates geoCoordinates){
        this.setLonDeg(geoCoordinates.lonDeg());
        this.setLatDeg(geoCoordinates.latDeg());
    }

}
