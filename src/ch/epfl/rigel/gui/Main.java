package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private static final String CITIES_PATH = "/cities5000.txt";
    private static final String FONT_PATH = "/Font Awesome 5 Free-Solid-900.otf";
    private static final String APP_ICON_PATH = "/icon.png";
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
    private static final String CITY_LABEL = " Localité :";

    private static final int QUERY_LIMIT_SEARCH = 10;

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
    private static final TextFormatter<Number> LATITUDE_TEXT_FORMATTER = buildTextLonLatFormatter(NUMBER_STRING_CONVERTER, GeographicCoordinates::isValidLatDeg);
    private static final TextFormatter<Number> LONGITUDE_TEXT_FORMATTER = buildTextLonLatFormatter(NUMBER_STRING_CONVERTER, GeographicCoordinates::isValidLonDeg);
    private static final TextFormatter<LocalTime> DATE_TIME_TEXT_FORMATTER = new TextFormatter<LocalTime>(LOCAL_TIME_STRING_CONVERTER);
    private static final String CONTROL_BAR_STYLE = "-fx-spacing: inherit; -fx-alignment: baseline-left;";
    private static final String LAT_LON_STYLE = "-fx-pref-width: 60; -fx-alignment: baseline-right;";

    private  CityCatalogue cityCatalogue;

    /**
     * Launches the application.
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
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {

        this.cityCatalogue = loadCityCatalogue();
        //Build the application properties
        TimeAnimator timeAnimator = new TimeAnimator(buildDateTimeBean());
        ViewingParametersBean viewParams = buildViewingParamBean();
        ObserverLocationBean obsLocation = buildObserverLocationBean();
        DateTimeBean dateTimeBean = timeAnimator.getDateTimeProperty();

        SkyCanvasManager skyCanvasManager = buildSkyCanvasManager(
                this.loadCatalogue(), dateTimeBean, obsLocation, viewParams
        );

        Canvas sky = skyCanvasManager.canvas();

        BorderPane mainPane = buildMainPane(dateTimeBean, obsLocation, viewParams, timeAnimator, skyCanvasManager, sky);

        primaryStage.setTitle(APP_TITLE);
        primaryStage.minWidthProperty().setValue(STAGE_MIN_WIDTH);
        primaryStage.minHeightProperty().setValue(STAGE_MIN_HEIGHT);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.show();
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream(APP_ICON_PATH)));
        sky.requestFocus();

    }

   /* private void multipleChoice(){

        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("Asterisms");
        list.add("Planets");
        list.add("Stars");
        list.add("Moon");
        list.add("Sun");

        ChoiceBox choiceBox = new ChoiceBox<String>(list);

        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

            }
        });

    }*/

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
        Pane skyViewPane = new Pane(sky);

        //Sets control bar
        HBox controlBar = buildControlBar(dateTimeBean, obsLocation, timeAnimator);
        BorderPane informationBar = buildInformationBar(viewParams, skyCanvasManager.objectUnderMouseProperty(),
                skyCanvasManager.mouseAzDegProperty(), skyCanvasManager.mouseAltDegProperty());
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
     * @return the bottom BorderPane containing information for the user FOV (Field of View Degrees)
     * and the current Mouse Position.
     */
    private BorderPane buildInformationBar(ViewingParametersBean viewParamBean,
                                           ObservableValue<CelestialObject> objMouse,
                                           DoubleProperty mouseAzDeg,
                                           DoubleProperty mouseAltDeg) {

        BorderPane informationBar = new BorderPane();
        Label fovLabel = buildFOVLabel(viewParamBean);
        Label closestCelObjLabel = buildClosestCelObjLabel(objMouse);
        Label obsMousePositionLabel = buildObsMousePositionLabel(mouseAzDeg, mouseAltDeg);

        informationBar.setLeft(fovLabel);
        informationBar.setCenter(closestCelObjLabel);
        informationBar.setRight(obsMousePositionLabel);

        //Styles
        informationBar.setStyle("-fx-padding: 4; -fx-background-color: white;");

        return informationBar;
    }

    /**
     * Builds the top BorderPane containing the inputs for the user to select it's position,
     * current DateTime and the animation options.
     *
     * @param dateTimeBean
     * @param obsLocationBean
     * @param timeAnimator
     * @return the top BorderPane containing the inputs for the user to select it's position,
     * current DateTime and the animation options.
     */
    private HBox buildControlBar(DateTimeBean dateTimeBean, ObserverLocationBean obsLocationBean, TimeAnimator timeAnimator) {
        //Disable nodes when time animator is running
        HBox controlBarCitySearch = buildControlBarCitySearch(obsLocationBean, dateTimeBean);
        HBox controlBarPosition = buildControlBarPosition(obsLocationBean);
        HBox controlBarInstant = buildControlBarInstant(dateTimeBean);
        HBox controlBarTimeAnimator = buildControlBarTimeAnimator(timeAnimator);

        HBox controlBar = new HBox(controlBarCitySearch, controlBarPosition, controlBarInstant, controlBarTimeAnimator);
        //Styles
        controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");
        //Disable Binding.
        controlBarInstant.disableProperty().bind(timeAnimator.runningProperty());

        return controlBar;
    }

    private HBox buildControlBarCitySearch(ObserverLocationBean observerLocationBean, DateTimeBean dateTimeBean) {
        Label cityLabel = new Label(CITY_LABEL);
        ComboBox<City> citySearchBar = buildCitySearchBar();
        citySearchBar.valueProperty().addListener((e, o, n) ->
                {
                    if(n != null){
                        observerLocationBean.setCoordinates(n.getGeoCoordinates());
                        dateTimeBean.setZone(n.getZoneId());
                    }

                }
                );


        //Create control bar
        HBox controlBarCity = new HBox(cityLabel, citySearchBar);
        controlBarCity.setStyle(CONTROL_BAR_STYLE);
        return controlBarCity;
    }

    /**
     * Builds the HBox containing the controls for the user position.
     *
     * @param obsLocationBean
     * @return the HBox containing the controls for the user position.
     */
    private HBox buildControlBarPosition(ObserverLocationBean obsLocationBean) {
        Label longitudeLabel = buildLongitudeLabel();
        TextField longitudeTextField = buildLongitudeTextField(obsLocationBean);
        Label latitudeLabel = buildLatitudeLabel();
        TextField latitudeTextField = buildLatitudeTextField(obsLocationBean);

        //Create control bar
        HBox controlBarPosition = new HBox(longitudeLabel, longitudeTextField, latitudeLabel, latitudeTextField);
        controlBarPosition.setStyle(CONTROL_BAR_STYLE);
        return controlBarPosition;
    }


    /**
     * Builds the HBox containing the controls for the user current date and time
     * instants.
     *
     * @param dateTimeBean
     * @return the HBox containing the controls for the user current date and time
     * instants.
     */
    private HBox buildControlBarInstant(DateTimeBean dateTimeBean) {
        //Build nodes
        Label dateLabel = buildDateLabel();
        Label hourLabel = buildHourLabel();
        DatePicker datePicker = buildDatePicker(dateTimeBean);
        TextField hourTextField = buildHourTextLabel(dateTimeBean);
        ComboBox<ZoneId> zoneComboBox = buildZoneIdComboBox(dateTimeBean);
        HBox controlBarInstant = new HBox(dateLabel, datePicker, hourLabel, hourTextField, zoneComboBox);
        controlBarInstant.setStyle(CONTROL_BAR_STYLE);

        return controlBarInstant;
    }

    /**
     * Builds the Hbox containing the time animation options.
     *
     * @param timeAnimator
     * @return  the Hbox containing the time animation options.
     */
    private HBox buildControlBarTimeAnimator(TimeAnimator timeAnimator) {

        //Set the choice box
        ChoiceBox choiceBox = buildTimeAcceleratorChoiceBox(timeAnimator);
        timeAnimator.timeAcceleratorProperty().bind(Bindings.select(choiceBox.valueProperty(), "accelerator"));
        Font buttonFont = this.loadFont();
        Button resetButton = buildResetButton(timeAnimator, buttonFont);
        Button playButton = buildPlayPauseButton(timeAnimator, buttonFont);
        HBox controlBarTimeAnimator = new HBox(choiceBox, resetButton, playButton);
        controlBarTimeAnimator.setStyle("-fx-spacing: inherit;");

        return controlBarTimeAnimator;
    }

    /**
     * Builds the reset button.
     *
     * @return the reset button.
     */
    private Button buildResetButton(TimeAnimator timeAnimator, Font font) {
        Button resetButton = new Button(UNICODE_RESET);
        resetButton.setFont(font);
        resetButton.setOnAction((e) -> timeAnimator.getDateTimeProperty().setZonedDateTime(getCurrentZonedDateTime()));
        resetButton.disableProperty().bind(timeAnimator.runningProperty());

        return resetButton;
    }

    /**
     * Builds the play/pause button.
     *
     * @return the play/pause button.
     */
    private Button buildPlayPauseButton(TimeAnimator timeAnimator, Font font) {
        Button playButton = new Button(UNICODE_PLAY);
        playButton.setFont(font);
        playButton.setOnAction((e) -> {
            if (timeAnimator.getRunning()) timeAnimator.stop();
            else timeAnimator.start();
        });
        playButton.textProperty().bind(
                Bindings.when(timeAnimator.runningProperty()).then(UNICODE_PAUSE).otherwise(UNICODE_PLAY)
        );

        return playButton;
    }

    /**
     * Builds the time accelerator choice box.
     *
     * @return the time accelerator choice box.
     */
    private ChoiceBox<NamedTimeAccelerator> buildTimeAcceleratorChoiceBox(TimeAnimator timeAnimator) {
        ChoiceBox<NamedTimeAccelerator> choiceBox = new ChoiceBox<NamedTimeAccelerator>();
        ObservableList<NamedTimeAccelerator> obsList = FXCollections.observableList(Arrays.asList(NamedTimeAccelerator.values()));
        choiceBox.setItems(obsList);
        choiceBox.setValue(obsList.get(0));
        choiceBox.disableProperty().bind(timeAnimator.runningProperty());
        return choiceBox;
    }

    /**
     * Builds the Closest Celestial Object to the mouse label.
     *
     * @return the Closest Celestial Object to the mouse label.
     */
    private Label buildClosestCelObjLabel(ObservableValue<CelestialObject> objMouse) {
        Label closestCelObjLabel = new Label();

        ObjectBinding celObjString = Bindings.createObjectBinding(() -> {
            if (objMouse.getValue() == null) return EMPTY_STRING;
            else return objMouse.getValue().name();
        }, objMouse);

        closestCelObjLabel.textProperty().bind(
                Bindings.format("%s", celObjString)
        );

        return closestCelObjLabel;
    }

    /**
     * Builds the FOV label.
     *
     * @return the FOV label.
     */
    private Label buildFOVLabel(ViewingParametersBean viewParamBean) {
        Label fovLabel = new Label();
        fovLabel.textProperty().bind(
                Bindings.format(INFO_FOV_FORMAT, viewParamBean.fieldOfViewDegProperty())
        );
        return fovLabel;
    }

    /**
     * Builds the mouse position label.
     *
     * @return  the mouse position label.
     */
    private Label buildObsMousePositionLabel(DoubleProperty mouseAzDeg, DoubleProperty mouseAltDeg) {
        Label obsMousePositionLabel = new Label();
        obsMousePositionLabel.textProperty().bind(
                Bindings.format(INFO_COORD_FORMAT, mouseAzDeg, mouseAltDeg)
        );

        return obsMousePositionLabel;
    }

    private ComboBox<City> buildCitySearchBar(){

        ComboBox<City> citySearchField = new ComboBox<City>();

        citySearchField.getSelectionModel().selectedItemProperty().addListener((e,o,n) -> System.out.println("Changed Selection " + AutoCompleteBox.getComboBoxValue(citySearchField)));

        AutoCompleteBox.autoCompleteComboBoxPlus(citySearchField, this.cityCatalogue, QUERY_LIMIT_SEARCH);

        return citySearchField;
    }

    /**
     * Builds the longitude label.
     *
     * @return  the longitude label.
     */
    private Label buildLongitudeLabel() {
        return new Label(LONGITUDE_LABEL);
    }

    /**
     * Builds the latitude label.
     *
     * @return the latitude label.
     */
    private Label buildLatitudeLabel() {
        return new Label(LATITUDE_LABEL);
    }

    /**
     * Builds the date label.
     *
     * @return the date label.
     */
    private Label buildDateLabel() {
        return new Label(DATE_LABEL);
    }

    /**
     * Builds the hour label.
     *
     * @return the hour label.
     */
    private Label buildHourLabel() {
        return new Label(HOUR_LABEL);
    }

    /**
     * Builds the longitude text field.
     *
     * @return the longitude text field.
     */
    private TextField buildLongitudeTextField(ObserverLocationBean obsLocationBean) {
        TextField longitudeTextField = new TextField();
        LONGITUDE_TEXT_FORMATTER.valueProperty().bindBidirectional(obsLocationBean.lonDegProperty());
        longitudeTextField.setTextFormatter(LONGITUDE_TEXT_FORMATTER);
        longitudeTextField.setStyle(LAT_LON_STYLE);
        return longitudeTextField;
    }

    /**
     * Builds the altitude text field.
     *
     * @return  the altitude text field.
     */
    private TextField buildLatitudeTextField(ObserverLocationBean obsLocationBean) {
        TextField latitudeTextField = new TextField();
        LATITUDE_TEXT_FORMATTER.valueProperty().bindBidirectional(obsLocationBean.latDegProperty());
        latitudeTextField.setTextFormatter(LATITUDE_TEXT_FORMATTER);
        latitudeTextField.setStyle(LAT_LON_STYLE);
        return latitudeTextField;
    }

    /**
     * Build the hour text field.
     *
     * @return the hour text field.
     */
    private TextField buildHourTextLabel(DateTimeBean dateTimeBean) {
        TextField hourTextField = new TextField();
        DATE_TIME_TEXT_FORMATTER.valueProperty().bindBidirectional(dateTimeBean.timeProperty());
        hourTextField.setTextFormatter(DATE_TIME_TEXT_FORMATTER);
        hourTextField.setStyle("-fx-pref-width: 75;-fx-alignment: baseline-right;");
        return hourTextField;
    }

    /**
     * Builds the zone id ComboBox.
     *
     * @return the zone id ComboBox.
     */
    private ComboBox<ZoneId> buildZoneIdComboBox(DateTimeBean dateTimeBean) {
        ComboBox<ZoneId> zoneComboBox = new ComboBox<ZoneId>();

        ArrayList<String> zoneIdStringList = new ArrayList<String>(ZoneId.getAvailableZoneIds());
        ArrayList<ZoneId> zoneIdList = new ArrayList<ZoneId>();
        Collections.sort(zoneIdStringList);
        for (String s : zoneIdStringList) {
            zoneIdList.add(ZoneId.of(s));
        }

        ObservableList<ZoneId> obsZoneIdList = FXCollections.observableList(zoneIdList);

        zoneComboBox.setItems(obsZoneIdList);

        zoneComboBox.valueProperty().bindBidirectional(dateTimeBean.zoneProperty());
        zoneComboBox.setStyle("-fx-pref-width: 180;");
        return zoneComboBox;
    }

    /**
     * Builds the Date Picker.
     *
     * @return the Date Picker.
     */
    private DatePicker buildDatePicker(DateTimeBean dateTimeBean) {
        DatePicker datePicker = new DatePicker();
        datePicker.valueProperty().bindBidirectional(dateTimeBean.dateProperty());
        datePicker.setStyle("-fx-pref-width: 120;");
        return datePicker;
    }

    /**
     * Builds the application observer location bean property with initial coordinates.
     * @return the application observer location bean property with initial coordinates.
     */
    private ObserverLocationBean buildObserverLocationBean() {
        ObserverLocationBean observerLocationBean = new ObserverLocationBean();
        observerLocationBean.setCoordinates(INIT_COORDINATES);
        return observerLocationBean;
    }

    /**
     * Builds the application date time bean with initial date time.
     * @return the application date time bean with initial date time.
     */
    private DateTimeBean buildDateTimeBean() {
        DateTimeBean dateTimeBean = new DateTimeBean();
        dateTimeBean.setZonedDateTime(INIT_DATETIME);
        return dateTimeBean;
    }

    /**
     * Builds the application viewing parameters  bean with view params .
     * @return the application viewing parameters  bean with view params .
     */
    private ViewingParametersBean buildViewingParamBean() {
        ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
        viewingParametersBean.setCenter(INIT_VIEW_PARAM);
        viewingParametersBean.setFieldOfViewDeg(INIT_FOVDEG);
        return viewingParametersBean;
    }

    /**
     * Builds the applicastion sky canvas manager.
     * @param catalogue
     * @param dateTimeBean
     * @param observerLocationBean
     * @param viewingParametersBean
     * @return Builds the applicastion sky canvas manager.
     */
    private SkyCanvasManager buildSkyCanvasManager(
            StarCatalogue catalogue, DateTimeBean dateTimeBean,
            ObserverLocationBean observerLocationBean, ViewingParametersBean viewingParametersBean) {
        return new SkyCanvasManager(catalogue, dateTimeBean, observerLocationBean, viewingParametersBean);
    }


    /**
     * Utility method for getting the current DateTime and Zone of the application.
     * @return
     */
    static private ZonedDateTime getCurrentZonedDateTime() {
        return ZonedDateTime.now();
    }

    /**
     * Builds a text formater for lon and lat deg.
     * Uses predicate to determine if new value is valid.
     *
     * @param nbStringConverter
     * @param predicate
     * @return
     */
    static private TextFormatter<Number> buildTextLonLatFormatter(NumberStringConverter nbStringConverter, Predicate<Double> predicate) {
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

    /**
     * Loads the font resource.
     *
     * @return
     */
    private Font loadFont() {
        try (InputStream fontStream = getClass().getResourceAsStream(FONT_PATH)) {
            return Font.loadFont(fontStream, FONT_SIZE);
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
    private StarCatalogue loadCatalogue() {

        try (InputStream hs = getClass().getResourceAsStream(HYG_PATH);
             InputStream astStream = getClass().getResourceAsStream(AST_PATH)) {

            return new StarCatalogue.Builder()
                    .loadFrom(hs, HygDatabaseLoader.INSTANCE)
                    .loadFrom(astStream, AsterismLoader.INSTANCE)
                    .build();

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
    private CityCatalogue loadCityCatalogue() {

        try (InputStream ct = getClass().getResourceAsStream(CITIES_PATH)) {

            return new CityCatalogue.Builder()
                    .loadFrom(ct, CitiesLoader.INSTANCE)
                    .build();

        } catch (IOException ioException) {
            System.out.println(String.format("Got an error while loading Font. Error: %s", ioException));
            return null;
        }

    }


}
