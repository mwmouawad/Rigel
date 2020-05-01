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

public class ObserverLocationBean {

    //TODO: final?
    private final ObjectProperty<Double> lonDeg;
    private final ObjectProperty<Double> latDeg;
    private final ObjectProperty<GeographicCoordinates> coordinates;

    public ObserverLocationBean(double lonDeg, double latDeg) {
        this.lonDeg = new SimpleObjectProperty(latDeg);
        this.latDeg = new SimpleObjectProperty(lonDeg);
        this.coordinates = new SimpleObjectProperty(
                GeographicCoordinates.ofDeg(lonDeg, latDeg)
        );


        ObservableObjectValue objectValue = Bindings.createObjectBinding(
                () -> (GeographicCoordinates.ofDeg(this.lonDeg.getValue(), this.latDeg.getValue())),
                this.lonDeg, this.latDeg);

        //TODO: Other way to bind values? Maybe biderection?
        this.coordinates.bind(objectValue);


    }

    public ObjectProperty<Double> lonDegProperty() { return lonDeg; }

    public double getLonDeg() { return lonDeg.get().doubleValue(); }

    public void setLonDeg(double lonDeg) {this.lonDeg.set(lonDeg);}

    public ObjectProperty<Double> latDegProperty() { return latDeg; }

    public double getLatDeg() { return latDeg.get().doubleValue(); }

    public void setLatDeg(double latDeg) {this.latDeg.set(latDeg);}

    public ObjectProperty<GeographicCoordinates> coordinatesProperty() { return coordinates; }

    public GeographicCoordinates getCoordinates() { return coordinates.get(); }

   //TODO: Should it be possible to set directly?
    public void setCoordinates(GeographicCoordinates geoCoordinates){
        this.setLonDeg(geoCoordinates.lonDeg());
        this.setLatDeg(geoCoordinates.latDeg());
        //this.coordinates.set(geoCoordinates);
    }

}
