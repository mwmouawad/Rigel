package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public final class TimeAnimator extends AnimationTimer {


    private long initTimeNano;
    private TimeAccelerator timeAccelerator;
    final private DateTimeBean dateTimeBean;
    final private SimpleBooleanProperty running;


    public TimeAnimator(DateTimeBean dateTimeBean){
        this.dateTimeBean = dateTimeBean;
        this.running = new SimpleBooleanProperty(false);

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
        
    }
}
