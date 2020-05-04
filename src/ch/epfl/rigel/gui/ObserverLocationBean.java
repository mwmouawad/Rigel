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

public class ObserverLocationBean {

    //TODO: final?
    private final DoubleProperty lonDeg;
    private final DoubleProperty latDeg;
    private final ObjectProperty<GeographicCoordinates> coordinates;
    private final ObjectBinding<GeographicCoordinates> binding;

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

    public DoubleProperty lonDegProperty() { return lonDeg; }

    public double getLonDeg() { return lonDeg.get(); }

    public void setLonDeg(double lonDeg) {this.lonDeg.set(lonDeg);}

    public DoubleProperty latDegProperty() { return latDeg; }

    public double getLatDeg() { return latDeg.get(); }

    public void setLatDeg(double latDeg) {this.latDeg.set(latDeg);}

    public ObjectProperty<GeographicCoordinates> coordinatesProperty() { return coordinates; }

    public GeographicCoordinates getCoordinates() { return coordinates.get(); }

   //TODO: Should it be possible to set directly?
    public void setCoordinates(GeographicCoordinates geoCoordinates){
        this.setLonDeg(geoCoordinates.lonDeg());
        this.setLatDeg(geoCoordinates.latDeg());
    }

}
