package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class DateTimeBean {

     private final ObjectProperty<LocalDate>  date;
     private final ObjectProperty<LocalTime> time;
     private final ObjectProperty<ZoneId> zone;

     public DateTimeBean(){
         this.date = new SimpleObjectProperty<>();
         this.time = new SimpleObjectProperty<>();
         this.zone = new SimpleObjectProperty<>();
     }

     public ZonedDateTime getZonedDateTime(){
         return ZonedDateTime.of(
                 this.getDate(),
                 this.getTime(),
                 this.getZone()
         );
     }

     public void setZonedDateTime(ZonedDateTime zonedDateTime){
         this.setDate(zonedDateTime.toLocalDate());
         this.setTime(zonedDateTime.toLocalTime());
         this.setZone(zonedDateTime.getZone());
     }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }
    public LocalDate getDate() {
        return this.date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);;
    }

    public ObjectProperty<LocalTime> timeProperty() {
        return time;
    }
    public LocalTime getTime() {
        return this.time.get();
    }

    public void setTime(LocalTime time) {
        this.time.set(time);
    }

    public ObjectProperty<ZoneId> zoneProperty() {
        return zone;
    }
    public ZoneId getZone() {
        return this.zone.get();
    }

    public void setZone(ZoneId zone) {
        this.zone.set(zone);
    }
}
