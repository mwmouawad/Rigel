package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * A JavaFX bean class representing a ZonedDateTime. Contains
 * a ZoneId, a LocalDate and a LocalTime.
 */
public final class DateTimeBean {

    //TODO: Should it be final?
     private final ObjectProperty<LocalDate>  date;
     private final ObjectProperty<LocalTime> time;
     private final ObjectProperty<ZoneId> zone;

    /**
     * Constructs a Date Time Bean instance.
     * Initialize's empty ObjectProperties.
     */
    public DateTimeBean(){
         this.date = new SimpleObjectProperty<>();
         this.time = new SimpleObjectProperty<>();
         this.zone = new SimpleObjectProperty<>();
     }

    /**
     * Gets the DateTimeBean in the form of a ZonedDateTime instance.
     * @return a ZonedDate time instance of  DateTimeBen instance's info.
     */
     public ZonedDateTime getZonedDateTime(){
         return ZonedDateTime.of(
                 this.getDate(),
                 this.getTime(),
                 this.getZone()
         );
     }

    /**
     * Set's the instance's properties as the given ZonedDateTime object.
     * @param zonedDateTime input ZonedDateTime to set the instance according to.
     */
     public void setZonedDateTime(ZonedDateTime zonedDateTime){
         this.setDate(zonedDateTime.toLocalDate());
         this.setTime(zonedDateTime.toLocalTime());
         this.setZone(zonedDateTime.getZone());
     }

    /**
     * Gets the stored ObjectProperty for the local date.
     * @return the LocalDate in the form of a ObjectProperty.
     */
    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    /**
     * Gets the stored LocalDate..
     * @return the stored LocalDate.
     */
    //TODO: public ou private ?
    public LocalDate getDate() {
        return this.date.get();
    }

    /**
     * Sets the zone id as the given LocalDate param.
     * @param date
     */
    public void setDate(LocalDate date) {
        this.date.set(date);;
    }

    /**
     * Gets the LocalTime ObjectProperty
     * @return the LocalTime ObjectProperty.
     */
    public ObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    /**
     * Gets the local time LocalTime object.
     * @return local time.
     */
    public LocalTime getTime() {
        return this.time.get();
    }

    /**
     * Sets the local time according to the given LocalTime param.
     * @param time LocalTime to set the instance to.
     */
    public void setTime(LocalTime time) {
        this.time.set(time);
    }

    /**
     * Gets the ZoneId ObjectProperty.
     * @return the ZoneId ObjectProperty.
     */
    public ObjectProperty<ZoneId> zoneProperty() {
        return zone;
    }

    /**
     * Gets the ZoneId.
     * @return the ZoneId.
     */
    public ZoneId getZone() {
        return this.zone.get();
    }

    /**
     * Sets the zone id as the given ZoneId param.
     * @param zone
     */
    public void setZone(ZoneId zone) {
        this.zone.set(zone);
    }
}
