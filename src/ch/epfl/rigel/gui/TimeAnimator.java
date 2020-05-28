package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.ZonedDateTime;

/**
 * Extends the JavaFx AnimationTimer intended to be used for accelerating
 * the application time with it's associated TimeAccelerator.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class TimeAnimator extends AnimationTimer {


    final private SimpleObjectProperty<TimeAccelerator> timeAccelerator  = new SimpleObjectProperty<>();
    final private SimpleBooleanProperty running = new SimpleBooleanProperty(false);
    final private DateTimeBean dateTimeBean;
    private ZonedDateTime initSimulatedTime;
    private long initTimeStampNano = 0;


    /**
     * Construct's a TimeAnimator instance and associates the dateTimeBean input to it.
     * @param dateTimeBean object to be associated with the instance.
     */
    public TimeAnimator(DateTimeBean dateTimeBean){
        this.dateTimeBean = dateTimeBean;
    }

    /**
     * Overrides the AnimationTimer start() method called to begin the animation
     * timer.
     */
    @Override
    public void start() {
        super.start();
        this.initSimulatedTime = dateTimeBean.getZonedDateTime();
        this.initTimeStampNano = 0;
        this.running.set(true);
    }

    /**
     * Overrides the AnimationTimer stop() method called to stop the animation
     * timer.
     */
    @Override
    public void stop() {
        super.stop();
        this.running.set(false);
    }

    /**
     * Method called before each frame update.
     * Adjust's the associated DateTimeBean using the accelerator and
     * the time difference with the last call.
     * @param now JavaFX timestamp for the current time of the update.
     */
    @Override
    public void handle(long now) {
        if(this.initTimeStampNano > 0){
            long delta = now - this.initTimeStampNano;
            ZonedDateTime updatedTime = this.timeAccelerator.get().adjust(initSimulatedTime, delta);
            this.dateTimeBean.setZonedDateTime(updatedTime);
        }else{
            this.initTimeStampNano = now;
        }
    }

    /**
     * Sets the TimeAnimator accelerator.
     * @param timeAccelerator
     */
    public void setAccelerator(TimeAccelerator timeAccelerator){
        this.timeAccelerator.set(timeAccelerator);
    }

    /**
     * Returns the current time accelerator property.
     * @return the current time accelerator property.
     */
    public SimpleObjectProperty<TimeAccelerator> timeAcceleratorProperty() { return timeAccelerator; }

    /**
     * Returns the current time accelerator.
     * @return the current time accelerator.
     */
    public TimeAccelerator getTimeAccelerator() { return timeAccelerator.getValue(); }
    /**
     * Returns the given running state of the animation timer.
     * @return state of the animation state.
     */
    public ReadOnlyBooleanProperty runningProperty() {
        return this.running;
    }

    /**
     * Gets the running status of the current TimeAnimator instance. true if it is currently running
     * otherwise false.
     * @return the running status of the current TimeAnimator instance. true if it is currently running
     * otherwise false.
     */
    public boolean getRunning() { return running.getValue(); }


    /**
     * Gets the current DateTimeBean property of the time animator.
     * @return the current DateTimeBean property of the time animator.
     */
    public DateTimeBean getDateTimeProperty() {
        return dateTimeBean;
    }


}
