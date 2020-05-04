package ch.epfl.rigel.gui;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    private final double STAGE_MIN_WIDTH = 800;
    private final double STAGE_MIN_HEIGHT = 600;

    public static void main(String[] args) { launch();}

    @Override
    public void start(Stage primaryStage) throws Exception {
        ImageView imageView = new ImageView();
        BorderPane mainPane = new BorderPane(imageView);
        primaryStage.setTitle("Rigel");
        primaryStage.minWidthProperty().setValue(STAGE_MIN_WIDTH);
        primaryStage.minHeightProperty().setValue(STAGE_MIN_HEIGHT);
        setControlBar();
        primaryStage.show();
    }

    private void setControlBar() {
        HBox controlBar = new HBox(controlBarPosition(), controlBarInstant(), controlBarTimeZone());
        controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");
    }

    private HBox controlBarPosition(){
        HBox controlBarPosition = new HBox();
        controlBarPosition.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        return controlBarPosition;
    }

    private HBox controlBarInstant(){
        return null;
    }

    private HBox controlBarTimeZone(){
        return null;
    }



}
