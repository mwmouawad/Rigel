package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;

/**
 * Handles the mouse movement animation during mouse movement mode. Extends the JavaFX Animation
 * Timer to animate to vertical/horizontal position depending of the mouse position.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public class MouseMovementAnimation extends AnimationTimer{

    private final ObjectProperty<Point2D> mousePositionProperty;
    private final DoubleProperty horizontalTranslation;
    private final DoubleProperty verticalTranslation;
    private final DoubleProperty canvasWidthProperty;
    private final DoubleProperty canvasHeightProperty;
    private final BooleanProperty mouseMovementEnableProperty;
    static private final double HOR_THRESHOLD = 0.1;
    static private final double VER_THRESHOLD = 0.15;


    public MouseMovementAnimation(ObjectProperty<Point2D> mousePositionProperty, DoubleProperty canvasWidthProperty, DoubleProperty canvasHeightProperty,
                                  BooleanProperty mouseMovementEnableProperty){
        this.mousePositionProperty = mousePositionProperty;
        this.canvasWidthProperty = canvasWidthProperty;
        this.canvasHeightProperty = canvasHeightProperty;
        this.mouseMovementEnableProperty = mouseMovementEnableProperty;
        this.horizontalTranslation = new SimpleDoubleProperty(0.0);
        this.verticalTranslation = new SimpleDoubleProperty(0.0);

    }



    //TODO: Improve movement using FOV property scale.
    @Override
    public void handle(long now) {
        //Binding sending translate hor/vert movement.
        if(!mouseMovementEnableProperty.get()){
            return;
        }


        if(this.mousePositionProperty.getValue().getX() > (this.canvasWidthProperty.get() * (1 - HOR_THRESHOLD))){
            this.horizontalTranslation.set(1.0);
        }
        else if(this.mousePositionProperty.getValue().getX() < (this.canvasWidthProperty.get() * HOR_THRESHOLD)){
            this.horizontalTranslation.set(-1.0);
        }

        if(this.mousePositionProperty.getValue().getY() > (this.canvasHeightProperty.get() * (1 - VER_THRESHOLD))){
            this.verticalTranslation.set(-1.0);
        }
        else if(this.mousePositionProperty.getValue().getY() < (this.canvasHeightProperty.get() * VER_THRESHOLD)){
            this.verticalTranslation.set(1.0);
        }

        this.horizontalTranslationProperty().set(0.0);
        this.verticalTranslationProperty().set(0.0);


    }


    public DoubleProperty horizontalTranslationProperty(){
        return this.horizontalTranslation;
    }

    public DoubleProperty verticalTranslationProperty(){
        return this.verticalTranslation;
    }
}