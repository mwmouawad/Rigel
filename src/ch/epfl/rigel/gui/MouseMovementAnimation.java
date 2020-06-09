package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.*;
import javafx.geometry.Point2D;

/**
 * Handles the mouse movement animation during mouse movement mode. Extends the JavaFX Animation
 * Timer to animate to vertical/horizontal position depending on the mouse position.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public class MouseMovementAnimation extends AnimationTimer{

    private final ObjectProperty<Point2D> mousePositionProperty;
    private final DoubleProperty horizontalTranslation;
    private final DoubleProperty verticalTranslation;
    private final DoubleProperty canvasWidthProperty;
    private final DoubleProperty canvasHeightProperty;
    private final DoubleProperty fovDegProperty;
    private final BooleanProperty mouseMovementEnableProperty;
    static private final double HOR_THRESHOLD = 0.1;
    static private final double VER_THRESHOLD = 0.15;
    static private final double PER_SEC_IN_FRAME = 1.0 / 60.0;
    private long lastNanoTime = 0;


    /**
     * Constructs a mouve movement animation.
     *
     * @param mousePositionProperty the property containing the mouse coordinates on screen.
     * @param canvasWidthProperty   the canvas width property.
     * @param canvasHeightProperty the canvas height property.
     * @param fovDegProperty the Field of Vie (FOV) property.
     */
    public MouseMovementAnimation(ObjectProperty<Point2D> mousePositionProperty, DoubleProperty canvasWidthProperty, DoubleProperty canvasHeightProperty,
                                  DoubleProperty fovDegProperty){
        this.mousePositionProperty = mousePositionProperty;
        this.canvasWidthProperty = canvasWidthProperty;
        this.canvasHeightProperty = canvasHeightProperty;
        this.mouseMovementEnableProperty = new SimpleBooleanProperty(false);
        this.fovDegProperty = fovDegProperty;
        this.horizontalTranslation = new SimpleDoubleProperty(0.0);
        this.verticalTranslation = new SimpleDoubleProperty(0.0);

    }


    /**
     * Starts the mouse movement animation.
     */
    @Override
    public void start() {
        super.start();
        this.lastNanoTime = 0;
        this.mouseMovementEnableProperty.set(true);
    }

    /**
     * Stops the mouse movement animation.
     */
    @Override
    public void stop() {
        super.stop();
        this.mouseMovementEnableProperty.set(false);
    }

    @Override
    public void handle(long now) {
        //Binding sending translate hor/vert movement.
        if(!this.mouseMovementEnableProperty.get()){
            return;
        }
        if(this.lastNanoTime == 0){
            this.lastNanoTime = now;
            return;
        }

        double deltaSec = (now - lastNanoTime) * 1e-9;

        //Scale taking into account the FOV and the number of seconds passed.
        //For a FOV of 150deg you should have 1 deg of animation per sec.
        double deltaMov = this.fovDegProperty.get() / 150.0
                * (deltaSec  / PER_SEC_IN_FRAME)
                ;
        if(this.mousePositionProperty.getValue().getX() > (this.canvasWidthProperty.get() * (1 - HOR_THRESHOLD))){
            this.horizontalTranslation.set(deltaMov);
        }
        else if(this.mousePositionProperty.getValue().getX() < (this.canvasWidthProperty.get() * HOR_THRESHOLD)){
            this.horizontalTranslation.set(-deltaMov);
        }

        if(this.mousePositionProperty.getValue().getY() > (this.canvasHeightProperty.get() * (1 - VER_THRESHOLD))){
            this.verticalTranslation.set(-deltaMov);
        }
        else if(this.mousePositionProperty.getValue().getY() < (this.canvasHeightProperty.get() * VER_THRESHOLD)){
            this.verticalTranslation.set(deltaMov);
        }

        //Reset to 0 to trigger the listeners on SkyCanvasManager.
        this.horizontalTranslationProperty().set(0.0);
        this.verticalTranslationProperty().set(0.0);

        this.lastNanoTime = now;

    }


    /**
     * Returns the mouse movement Enable Property.
     * @return returns the mouse movement Enable Property.
     */
    public BooleanProperty getMouseMovementEnableProperty(){
        return this.mouseMovementEnableProperty;
    }

    /**
     * Returns horizontal translation property.
     * @return returns horizontal Translation property.
     */
    public DoubleProperty horizontalTranslationProperty(){
        return this.horizontalTranslation;
    }

    /**
     * Return the vertical translation property.
     * @return the vertical translation property.
     */
    public DoubleProperty verticalTranslationProperty(){
        return this.verticalTranslation;
    }
}