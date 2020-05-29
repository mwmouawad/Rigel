package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;


/**
 * Main class of the  application. Runs a SkyCanvasManager handling the sky view,
 * sets additional UI information and listeners.
 * Divides the screen in three panes.
 * Loads resources as stars catalogue, asterisms catalogue and font awesome.
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
final public class Main extends Application {

    //Initial Preferences
    private static final double STAGE_MIN_WIDTH = 800;
    private static final double STAGE_MIN_HEIGHT = 600;
    private static final String APP_TITLE = "Rigel";
    private static final String HYG_PATH = "/hygdata_v3.csv";
    private static final String AST_PATH = "/asterisms.txt";
    private static final String FONT_PATH = "/Font Awesome 5 Free-Solid-900.otf";
    private static final int FONT_SIZE = 15;
    private static final ZonedDateTime INIT_DATETIME = getCurrentZonedDateTime();
    private static final HorizontalCoordinates INIT_VIEW_PARAM = HorizontalCoordinates.ofDeg(180.000000000001, 15);
    private static final double INIT_FOVDEG = 100.0;
    private static final GeographicCoordinates INIT_COORDINATES = GeographicCoordinates.ofDeg(6.57, 46.52);
    private static final String UNICODE_RESET = "\uf0e2";
    private static final String UNICODE_PLAY = "\uf04b";
    private static final String UNICODE_PAUSE = "\uf04c";
    private static final String EMPTY_STRING = "";

    //Labels
    private static final String LATITUDE_LABEL = "Latitude (°) :";
    private static final String LONGITUDE_LABEL = "Longitude (°) :";
    private static final String DATE_LABEL = "Date : ";
    private static final String HOUR_LABEL = "Heure : ";

    //Information Bar String Formatters
    private static final String INFO_COORD_FORMAT = "Azimut : %.2f°  hauteur : %.2f°";
    private static final String INFO_FOV_FORMAT = "Champ de vue : %.2f°";

    //String converters
    private static final NumberStringConverter NUMBER_STRING_CONVERTER = new NumberStringConverter("#0.00");
    private static final LocalTimeStringConverter LOCAL_TIME_STRING_CONVERTER = new LocalTimeStringConverter(
            DateTimeFormatter.ofPattern("HH:mm:ss"),
            DateTimeFormatter.ofPattern("HH:mm:ss")
    );

    //Formatters
    private static TextFormatter LATITUDE_TEXT_FORMATTER = buildTextLonLatFormatter(NUMBER_STRING_CONVERTER, GeographicCoordinates::isValidLatDeg);
    private static TextFormatter LONGITUDE_TEXT_FORMATTER = buildTextLonLatFormatter(NUMBER_STRING_CONVERTER, GeographicCoordinates::isValidLonDeg);
    private static TextFormatter DATE_TIME_TEXT_FORMATTER = new TextFormatter<>(LOCAL_TIME_STRING_CONVERTER);

    //Resources
    private final Font fontAwesome = loadFont();
    private final StarCatalogue starCatalogue = loadCatalogue();


    /**
     * Launchs the application.
     * Calls the JavaFX Application launch method.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Overrides JavaFx start method.
     * Starts the application, builds panes, bindings and SkyCanvasManager to display the sky.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Build the application properties
        TimeAnimator timeAnimator = new TimeAnimator(buildDateTimeBean());
        ViewingParametersBean viewParams = buildViewingParamBean();
        ObserverLocationBean obsLocation = buildObserverLocationBean();
        DateTimeBean dateTimeBean = timeAnimator.getDateTimeProperty();
        SkyCanvasManager skyCanvasManager = buildSkyCanvasManager(
                this.starCatalogue, dateTimeBean, obsLocation, viewParams
        );
        Canvas sky = skyCanvasManager.canvas();
        //Build the Pane
        BorderPane mainPane = buildMainPane(dateTimeBean, obsLocation, viewParams, timeAnimator, skyCanvasManager, sky);
        primaryStage.setTitle(APP_TITLE);
        primaryStage.minWidthProperty().setValue(STAGE_MIN_WIDTH);
        primaryStage.minHeightProperty().setValue(STAGE_MIN_HEIGHT);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.show();
        sky.requestFocus();

    }

    /**
     * Builds all panes for the application:
     * -Center pane: the SkyCanvasManager rendering the sky.
     * -Top pane: Control Bar.
     * -Bottom pane: Information Bar.
     *
     * @param dateTimeBean
     * @param obsLocation
     * @param viewParams
     * @param timeAnimator
     * @param skyCanvasManager
     * @param sky
     * @return
     */
    private BorderPane buildMainPane(DateTimeBean dateTimeBean, ObserverLocationBean obsLocation, ViewingParametersBean viewParams,
                                     TimeAnimator timeAnimator, SkyCanvasManager skyCanvasManager, Canvas sky) {

        BorderPane mainPane = new BorderPane();
        //Sets control bar
        HBox controlBar = buildControlBar(dateTimeBean, obsLocation, timeAnimator);
        BorderPane informationBar = buildInformationBar(viewParams, skyCanvasManager.objectUnderMouseProperty(), skyCanvasManager.mouseAzDeg, skyCanvasManager.mouseAltDeg);
        Pane skyViewPane = new Pane(sky);
        //Bindings
        sky.widthProperty().bind(skyViewPane.widthProperty());
        sky.heightProperty().bind(skyViewPane.heightProperty());
        //Show the stage;
        mainPane.setTop(controlBar);
        mainPane.setCenter(skyViewPane);
        mainPane.setBottom(informationBar);
        return mainPane;
    }

    /**
     * Builds the bottom BorderPane containing information for the user FOV (Field of View Degrees)
     * and the current Mouse Position.
     *
     * @param viewParamBean
     * @param objMouse
     * @param mouseAzDeg
     * @param mouseAltDeg
     * @return
     */
    static private BorderPane buildInformationBar(ViewingParametersBean viewParamBean,
                                                  ObservableValue<CelestialObject> objMouse,
                                                  DoubleBinding mouseAzDeg,
                                                  DoubleBinding mouseAltDeg) {

        BorderPane informationBar = new BorderPane();
        Label fovLabel = new Label();
        Label closestCelObjLabel = new Label();
        Label obsMousePositionLabel = new Label();

        informationBar.setLeft(fovLabel);
        informationBar.setCenter(closestCelObjLabel);
        informationBar.setRight(obsMousePositionLabel);

        //Styles
        informationBar.setStyle("-fx-padding: 4; -fx-background-color: white;");

        //Bindings
        ObjectBinding celObjString = Bindings.createObjectBinding(() -> {
            if (objMouse.getValue() == null) return EMPTY_STRING;
            else return objMouse.getValue().name();
        }, objMouse);

        fovLabel.textProperty().bind(
                Bindings.format(INFO_FOV_FORMAT, viewParamBean.fieldOfViewDegProperty())
        );
        closestCelObjLabel.textProperty().bind(
                Bindings.format("%s", celObjString)
        );
        obsMousePositionLabel.textProperty().bind(
                Bindings.format(INFO_COORD_FORMAT, mouseAzDeg, mouseAltDeg)
        );

        return informationBar;
    }


    /**
     * Builds the top BorderPane containing the inputs for the user to select it's position,
     * current DateTime and the animation options.
     *
     * @param dateTimeBean
     * @param obsLocationBean
     * @param timeAnimator
     * @return
     */
    private HBox buildControlBar(DateTimeBean dateTimeBean, ObserverLocationBean obsLocationBean, TimeAnimator timeAnimator) {
        //Disable nodes when time animator is running
        Binding disableBinding = Bindings.when(timeAnimator.runningProperty()).then(true).otherwise(false);
        HBox controlBarPosition = buildControlBarPosition(obsLocationBean);
        HBox controlBarInstant = buildControlBarInstant(dateTimeBean);
        HBox controlBarTimeAnimator = buildControlBarTimeAnimator(timeAnimator, disableBinding);
        //Disable entire control bar for instant of observation.
        HBox controlBar = new HBox(controlBarPosition, controlBarInstant, controlBarTimeAnimator);
        //Styles
        controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");
        //Bindings
        controlBarInstant.disableProperty().bind(disableBinding);

        return controlBar;
    }


    /**
     * Builds the HBox containing the controls for the user position.
     *
     * @param obsLocationBean
     * @return
     */
    private HBox buildControlBarPosition(ObserverLocationBean obsLocationBean) {
        Label longitudeLabel = buildLongitudeLabel();
        TextField longitudeTextField = buildLongitudeTextField();
        Label latitudeLabel = buildLatitudeLabel();
        TextField latitudeTextField = buildLatitudeTextField();

        //Set formatters
        latitudeTextField.setTextFormatter(LATITUDE_TEXT_FORMATTER);
        longitudeTextField.setTextFormatter(LONGITUDE_TEXT_FORMATTER);

        //Bind property to TextFormatter
        ObjectProperty latProperty = latitudeTextField.getTextFormatter().valueProperty();
        latProperty.bindBidirectional(obsLocationBean.latDegProperty());
        ObjectProperty longProperty = longitudeTextField.getTextFormatter().valueProperty();
        longProperty.bindBidirectional(obsLocationBean.lonDegProperty());

        //Create control bar
        HBox controlBarPosition = new HBox(longitudeLabel, longitudeTextField, latitudeLabel, latitudeTextField);
        controlBarPosition.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        return controlBarPosition;
    }

    /**
     * Builds the HBox containing the controls for the user current date and time
     * instants.
     *
     * @param dateTimeBean
     * @return
     */
    private HBox buildControlBarInstant(DateTimeBean dateTimeBean) {
        //Build nodes
        Label dateLabel = buildDateLabel();
        Label hourLabel = buildHourLabel();
        DatePicker datePicker = buildDatePicker();
        TextField hourTextField = buildHourTextLabel();
        ComboBox zoneComboBox = buildZoneIdComboBox();

        //Set formatters
        hourTextField.setTextFormatter(DATE_TIME_TEXT_FORMATTER);

        //Bind values
        ObjectProperty hourProperty = hourTextField.getTextFormatter().valueProperty();
        hourProperty.bindBidirectional(dateTimeBean.timeProperty());
        zoneComboBox.valueProperty().bindBidirectional(dateTimeBean.zoneProperty());
        datePicker.valueProperty().bindBidirectional(dateTimeBean.dateProperty());

        HBox controlBarInstant = new HBox(dateLabel, datePicker, hourLabel, hourTextField, zoneComboBox);
        controlBarInstant.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        return controlBarInstant;
    }

    /**
     * Builds the Hbox containing the time animation options.
     *
     * @param timeAnimator
     * @param disableBinding
     * @return
     */
    private HBox buildControlBarTimeAnimator(TimeAnimator timeAnimator, Binding disableBinding) {

        //Set the choice box
        ChoiceBox choiceBox = buildTimeAcceleratorChoiceBox();
        timeAnimator.timeAcceleratorProperty().bind(Bindings.select(choiceBox.valueProperty(), "accelerator"));

        Button resetButton = buildResetButton();
        Button playButton = buildPlayPauseButton();
        resetButton.setFont(this.fontAwesome);
        playButton.setFont(this.fontAwesome);

        //Actions
        resetButton.setOnAction((e) -> timeAnimator.getDateTimeProperty().setZonedDateTime(this.getCurrentZonedDateTime()));
        playButton.setOnAction(
                (e) -> timeAnimator.start()
        );
        playButton.setOnAction((e) -> {
            if (timeAnimator.getRunning()) timeAnimator.stop();
            else timeAnimator.start();
        });

        //Bindings
        Binding playPauseBinding = Bindings.when(timeAnimator.runningProperty()).then(UNICODE_PAUSE).otherwise(UNICODE_PLAY);
        choiceBox.disableProperty().bind(disableBinding);
        resetButton.disableProperty().bind(disableBinding);
        playButton.textProperty().bind(playPauseBinding);

        HBox controlBarTimeAnimator = new HBox(choiceBox, resetButton, playButton);
        controlBarTimeAnimator.setStyle("-fx-spacing: inherit;");

        return controlBarTimeAnimator;
    }

    /**
     * Builds the reset button.
     *
     * @return
     */
    static private Button buildResetButton() {
        Button resetButton = new Button(UNICODE_RESET);
        return resetButton;
    }

    /**
     * Builds the play/pause button.
     *
     * @return
     */
    static private Button buildPlayPauseButton() {
        Button playButton = new Button(UNICODE_PLAY);
        return playButton;
    }

    /**
     * Builds the time accelerator choice box.
     *
     * @return
     */
    static private ChoiceBox buildTimeAcceleratorChoiceBox() {
        ChoiceBox choiceBox = new ChoiceBox();
        ObservableList<NamedTimeAccelerator> obsList = FXCollections.observableList(Arrays.asList(NamedTimeAccelerator.values()));
        choiceBox.setItems(obsList);
        choiceBox.setValue(obsList.get(0));
        return choiceBox;
    }

    /**
     * Builds the longitude label.
     *
     * @return
     */
    static private Label buildLongitudeLabel() {
        return new Label(LONGITUDE_LABEL);
    }

    /**
     * Builds the latitude label.
     *
     * @return
     */
    static private Label buildLatitudeLabel() {
        return new Label(LATITUDE_LABEL);
    }

    /**
     * Builds the date label.
     *
     * @return
     */
    static private Label buildDateLabel() {
        Label dateLabel = new Label(DATE_LABEL);
        return dateLabel;
    }

    /**
     * Builds the hour label.
     *
     * @return
     */
    static private Label buildHourLabel() {
        Label hourLabel = new Label(HOUR_LABEL);
        return hourLabel;
    }

    /**
     * Builds the longitude text field.
     *
     * @return
     */
    static private TextField buildLongitudeTextField() {
        TextField longitudeTextField = new TextField();
        longitudeTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        return longitudeTextField;
    }

    /**
     * Builds the altitude text field.
     *
     * @return
     */
    static private TextField buildLatitudeTextField() {
        TextField latitudeTextField = new TextField();
        latitudeTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        return latitudeTextField;
    }

    /**
     * Build the hour text field.
     *
     * @return
     */
    static private TextField buildHourTextLabel() {
        TextField hourTextField = new TextField();
        hourTextField.setStyle("-fx-pref-width: 75;-fx-alignment: baseline-right;");
        return hourTextField;
    }

    /**
     * Builds the zone id ComboBox.
     *
     * @return
     */
    static private ComboBox buildZoneIdComboBox() {
        ComboBox zoneComboBox = new ComboBox();
        List<String> zoneIdStringList = new ArrayList<>(ZoneId.getAvailableZoneIds());
        List<ZoneId> zoneIdList = new ArrayList<ZoneId>();
        Collections.sort(zoneIdStringList);
        for (String s : zoneIdStringList) {
            zoneIdList.add(ZoneId.of(s));
        }
        zoneComboBox.getItems().addAll(zoneIdList);
        zoneComboBox.setStyle("-fx-pref-width: 180;");
        return zoneComboBox;
    }

    /**
     * Builds the Date Picker.
     *
     * @return
     */
    private DatePicker buildDatePicker() {
        DatePicker datePicker = new DatePicker();
        datePicker.setStyle("-fx-pref-width: 120;");
        return datePicker;
    }

    static private ObserverLocationBean buildObserverLocationBean() {
        ObserverLocationBean observerLocationBean = new ObserverLocationBean();
        observerLocationBean.setCoordinates(INIT_COORDINATES);
        return observerLocationBean;
    }

    static private DateTimeBean buildDateTimeBean() {
        DateTimeBean dateTimeBean = new DateTimeBean();
        dateTimeBean.setZonedDateTime(INIT_DATETIME);
        return dateTimeBean;
    }

    static private ViewingParametersBean buildViewingParamBean() {
        ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
        viewingParametersBean.setCenter(INIT_VIEW_PARAM);
        viewingParametersBean.setFieldOfViewDeg(INIT_FOVDEG);
        return viewingParametersBean;
    }

    static private SkyCanvasManager buildSkyCanvasManager(
            StarCatalogue catalogue, DateTimeBean dateTimeBean,
            ObserverLocationBean observerLocationBean, ViewingParametersBean viewingParametersBean) {
        return new SkyCanvasManager(catalogue, dateTimeBean, observerLocationBean, viewingParametersBean);
    }

    /**
     * Loads the font resource.
     *
     * @return
     */
    private Font loadFont() {
        try (InputStream fontStream = getClass().getResourceAsStream(FONT_PATH)) {
            Font font = Font.loadFont(fontStream, FONT_SIZE);
            return font;
        } catch (IOException ioException) {
            System.out.println(String.format("Got an error while loading Font. Error: %s", ioException));
            return null;
        }
    }

    /**
     * Loads the StarCatalogue.
     *
     * @return
     */
    private static StarCatalogue loadCatalogue() {

        try (InputStream hs = Main.class.getResourceAsStream(HYG_PATH);
             InputStream astStream = Main.class.getResourceAsStream(AST_PATH)) {

            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hs, HygDatabaseLoader.INSTANCE)
                    .loadFrom(astStream, AsterismLoader.INSTANCE)
                    .build();

            return catalogue;

        } catch (IOException ioException) {
            throw new UncheckedIOException(ioException);
        }

    }

    static private ZonedDateTime getCurrentZonedDateTime() {
        return ZonedDateTime.now();
    }


    /**
     * Builds a text formater for lon and lat deg.
     * Uses predicate to determine if new value is valid.
     * @param nbStringConverter
     * @param predicate
     * @return
     */
    static private TextFormatter<Number> buildTextLonLatFormatter(NumberStringConverter nbStringConverter, Predicate<Double> predicate){
        UnaryOperator<TextFormatter.Change> filter = (change -> {
            try {
                String newText = change.getControlNewText();
                change.getControlNewText();
                double newCoordinate =
                        nbStringConverter.fromString(newText).doubleValue();
                return predicate.test(newCoordinate) ? change : null;

            } catch (Exception e) {
                return null;
            }
        });

        return new TextFormatter<>(nbStringConverter, 0, filter);
    }


}
