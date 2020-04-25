package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.ZonedDateTime;

public final class TimeAnimator extends AnimationTimer {


    private SimpleObjectProperty<TimeAccelerator> timeAccelerator  = new SimpleObjectProperty<>();
    final private SimpleBooleanProperty running = new SimpleBooleanProperty(false);
    final private DateTimeBean dateTimeBean;
    private long lastTimeNano = 0;


    public TimeAnimator(DateTimeBean dateTimeBean){
        this.dateTimeBean = dateTimeBean;
    }

    @Override
    public void start() {
        super.start();
        this.running.set(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.running.set(false);
    }

    /**
     * Method called before each frame update.
     * @param now
     */
    @Override
    public void handle(long now) {
        if(this.lastTimeNano > 0){
            long delta = now - this.lastTimeNano;
            double deltaSec = delta * 1e-9;
            ZonedDateTime updatedTime = this.timeAccelerator.get().adjust(dateTimeBean.getZonedDateTime(), delta);
            this.dateTimeBean.setZonedDateTime(updatedTime);
            this.lastTimeNano = now;
        }
        else{
            this.lastTimeNano = now;
            return;
        }

    }

    public void setAccelerator(TimeAccelerator timeAccelerator){
        this.timeAccelerator.set(timeAccelerator);
    }

    public ReadOnlyBooleanProperty runningProperty() {
        return this.running;
    }


}
