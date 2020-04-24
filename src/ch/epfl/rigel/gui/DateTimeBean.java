package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public final class DateTimeBean {

    //private ?
    private LocalDate date = null;
    private LocalTime time = null;
    private ZoneId zone = null;

    //TODO: pas compris ?
    public ObjectProperty<LocalDate> dateProperty() {
        return new SimpleObjectProperty<LocalDate>(this.date, "Date");
    }
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
