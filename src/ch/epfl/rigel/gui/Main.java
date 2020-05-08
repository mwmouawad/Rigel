package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.UnaryOperator;


public class Main extends Application {

    private final double STAGE_MIN_WIDTH = 800;
    private final double STAGE_MIN_HEIGHT = 600;

    //TODO: Create other class to handle all of this?
    private final String HYG_PATH = "/hygdata_v3.csv";
    private final String AST_PATH = "asterisms.txt";
    private final ZonedDateTime INIT_DATETIME = ZonedDateTime.parse("2020-02-17T20:15:00-03:00");
    private final HorizontalCoordinates INIT_VIEW_PARAM = HorizontalCoordinates.ofDeg(180.000000000001, 15);
    private static final double INIT_FOVDEG = 100.0;
    private final StarCatalogue starCatalogue = loadCatalogue();


    public static void main(String[] args) {
        launch();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        //TODO: should the beans be attributes ?

        BorderPane mainPane = new BorderPane();
        primaryStage.setTitle("Rigel");
        primaryStage.minWidthProperty().setValue(STAGE_MIN_WIDTH);
        primaryStage.minHeightProperty().setValue(STAGE_MIN_HEIGHT);


        //Sets SkyCanvasManager
        ViewingParametersBean viewParams = buildViewingParamBean();
        ObserverLocationBean obsLocation = buildObserverLocationBean();
        DateTimeBean dateTimeBean = buildDateTimeBean();
        SkyCanvasManager skyCanvasManager = buildSkyCanvasManager(Objects.requireNonNull(starCatalogue),
                dateTimeBean,obsLocation,viewParams);


        Canvas sky = skyCanvasManager.canvas();
        Pane skyViewPane = new Pane(sky);
        sky.widthProperty().bind(skyViewPane.widthProperty());
        sky.heightProperty().bind(skyViewPane.heightProperty());

        //Sets control bar
        HBox controlBar = buildControlBar(dateTimeBean,obsLocation);
        BorderPane informationBar = buildInformationBar(viewParams, skyCanvasManager.objectUnderMouseProperty(), skyCanvasManager.mouseAzDeg, skyCanvasManager.mouseAltDeg);


        //Show the stage;
        mainPane.setTop(controlBar);
        mainPane.setCenter(skyViewPane);
        mainPane.setBottom(informationBar);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.show();

        //Set focus
        sky.requestFocus();
    }

    private ObserverLocationBean buildObserverLocationBean() {
        ObserverLocationBean observerLocationBean = new ObserverLocationBean();
        observerLocationBean.setCoordinates(GeographicCoordinates.ofDeg(6.57, 46.52));
        return observerLocationBean;
    }

    private DateTimeBean buildDateTimeBean(){
        DateTimeBean dateTimeBean = new DateTimeBean();
        dateTimeBean.setZonedDateTime(INIT_DATETIME);
        return dateTimeBean;
    }

    private ViewingParametersBean buildViewingParamBean() {
        ViewingParametersBean viewingParametersBean =
                new ViewingParametersBean();
        viewingParametersBean.setCenter(INIT_VIEW_PARAM);
        viewingParametersBean.setFieldOfViewDeg(INIT_FOVDEG);

        return viewingParametersBean;

    }


    private SkyCanvasManager buildSkyCanvasManager(
            StarCatalogue catalogue, DateTimeBean dateTimeBean,
            ObserverLocationBean observerLocationBean, ViewingParametersBean viewingParametersBean) {

        return new SkyCanvasManager(catalogue, dateTimeBean, observerLocationBean, viewingParametersBean);
    }

    private BorderPane buildInformationBar(ViewingParametersBean viewParamBean, ObservableValue<CelestialObject> objMouse, DoubleBinding mouseAzDeg, DoubleBinding mouseAltDeg){

        BorderPane informationBar = new BorderPane();
        informationBar.setStyle("-fx-padding: 4; -fx-background-color: white;");

        Label fovLabel = new Label();
        fovLabel.textProperty().bind(
                Bindings.format("Champ de vue : %.2f°", viewParamBean.fieldOfViewDegProperty())
        );

        //TODO: Is it good? empty string or new string?
        ObjectBinding celObjString = Bindings.createObjectBinding( () -> {
            if (objMouse.getValue() == null) {
                return "";
            } return objMouse.getValue().name();
        }, objMouse);


        Label closestCelObjLabel = new Label();
        closestCelObjLabel.textProperty().bind(
                Bindings.format("%s", celObjString)
        );

        Label obsMousePositionLabel = new Label();
        obsMousePositionLabel.textProperty().bind(
                Bindings.format("Azimut : %.2f°,  hauteur : %.2f°" ,mouseAzDeg,mouseAltDeg)
        );

        informationBar.setLeft(fovLabel);
        informationBar.setCenter(closestCelObjLabel);
        informationBar.setRight(obsMousePositionLabel);



        return  informationBar;

    }


    private HBox buildControlBar(DateTimeBean dateTimeBean, ObserverLocationBean obsLocationBean) {
        HBox controlBar = new HBox(10, controlBarPosition(obsLocationBean),
                controlBarInstant(dateTimeBean), controlBarTimeAnimator());
        controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");
        return controlBar;
    }

    private HBox controlBarPosition(ObserverLocationBean obsLocationBean) {
        Label longitudeLabel = new Label("Longitude (°) :");
        TextField longitudeTextField = new TextField();
        longitudeTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        Label latitudeLabel = new Label("Latitude (°) :");
        TextField latitudeTextField = new TextField();
        latitudeTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");

        //Set formatter
        NumberStringConverter stringConverter = new NumberStringConverter("#0.00");
        latitudeTextField.setTextFormatter(buildTextFormatter(stringConverter, false));
        longitudeTextField.setTextFormatter(buildTextFormatter(stringConverter, true));

        //Bind property to TextFormatter
        ObjectProperty latProperty = latitudeTextField.getTextFormatter().valueProperty();
        latProperty.bindBidirectional(obsLocationBean.latDegProperty());
        ObjectProperty longProperty = longitudeTextField.getTextFormatter().valueProperty();
        longProperty.bindBidirectional(obsLocationBean.lonDegProperty());

        //Create control bar
        HBox controlBarPosition = new HBox(longitudeLabel,longitudeTextField, latitudeLabel,latitudeTextField);
        controlBarPosition.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        return controlBarPosition;
    }

    private HBox controlBarInstant(DateTimeBean dateTimeBean) {
        Label dateLabel = new Label("Date : ");
        DatePicker datePicker = new DatePicker();
        datePicker.getValue();
        datePicker.setStyle("-fx-pref-width: 120;");

        Label hourLabel = new Label("Heure : ");
        TextField hourTextField = new TextField();
        hourTextField.setStyle("-fx-pref-width: 75;-fx-alignment: baseline-right;");

        //Set Zone combo Box
        ComboBox zoneComboBox = new ComboBox();
        //TODO: Sort items

        List<String> sortedList = new ArrayList<>(ZoneId.getAvailableZoneIds());
        Collections.sort(sortedList);

        zoneComboBox.getItems().addAll(
                sortedList
        );

        zoneComboBox.setStyle("-fx-pref-width: 180;");

        //Set Formatters
        DateTimeFormatter hmsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTimeStringConverter stringConverter = new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
        TextFormatter<LocalTime> timeFormatter = new TextFormatter<>(stringConverter);

        hourTextField.setTextFormatter(timeFormatter);

        //Bind values
        ObjectProperty hourProperty = hourTextField.getTextFormatter().valueProperty();
        //TODO: what to do?
        hourProperty.bindBidirectional(dateTimeBean.timeProperty());
        zoneComboBox.valueProperty().bind(dateTimeBean.zoneProperty());
        
        datePicker.valueProperty().bindBidirectional(dateTimeBean.dateProperty());

        HBox controlBarInstant = new HBox(dateLabel,datePicker, hourLabel,hourTextField, zoneComboBox);
        controlBarInstant.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        return controlBarInstant;
    }


    private HBox controlBarTimeAnimator() {
        ChoiceBox choiceBox = new ChoiceBox();
        ObservableList obsList =  FXCollections.observableList(Arrays.asList( NamedTimeAccelerator.values()));
        choiceBox.setItems(obsList);

        HBox controlBarTimeAnimator = new HBox(choiceBox);
        controlBarTimeAnimator.setStyle("-fx-spacing: inherit;");

        return controlBarTimeAnimator;
    }

    private StarCatalogue loadCatalogue(){

        //TODO: Check exception handling
        try (InputStream hs = Main.class.getResourceAsStream("/hygdata_v3.csv")) {
            InputStream astStream = Main.class.getResourceAsStream("/asterisms.txt");

            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hs, HygDatabaseLoader.INSTANCE)
                    .loadFrom(astStream, AsterismLoader.INSTANCE)
                    .build();

            return catalogue;


        } catch (IOException ioException) {
            System.out.println(String.format("Got an error while loading Start Catalogue. Error: %s", ioException));
            return null;
        }

    }



    /**
     * Utility method.
     */
    private TextFormatter<Number> buildTextFormatter(NumberStringConverter nbStringConverter, boolean isLon) {
        UnaryOperator<TextFormatter.Change> filter = (change -> {
            try {
                String newText = change.getControlNewText();
                change.getControlNewText();
                double newCoordinate =
                        nbStringConverter.fromString(newText).doubleValue();
                if (isLon) {
                    return GeographicCoordinates.isValidLonDeg(newCoordinate)
                            ? change : null;
                }
                return GeographicCoordinates.isValidLatDeg(newCoordinate)
                        ? change : null;

            } catch (Exception e) {
                return null;
            }
        });

        return new TextFormatter<>(nbStringConverter, 0, filter);

    }


}
