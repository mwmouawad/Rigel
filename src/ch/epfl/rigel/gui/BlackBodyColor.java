package ch.epfl.rigel.gui;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;
import javafx.scene.paint.Color;

/**
 * Non instantiable class, meant to be used to get the corresponding color in degrees kelvin from a celestial object
 * to a Color object representation.
 * Loads the information from 'bbr_color.txt'.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public class BlackBodyColor {

    private static final String BBR_COLOR = "/bbr_color.txt";
    private static final Interval TEMP_INTERVAL = ClosedInterval.of(1000, 40000);
    private static final List<String> COLOR_LIST = loadColors();

    /**
     *
     */
    private BlackBodyColor() { }

    /**
     * Loads the colors of the bbr color file in a list of strings
     * @return
     */
     private static List<String> loadColors() {

        List<String> colors = new ArrayList<>();

        try (InputStream inputStream = BlackBodyColor.class.getResourceAsStream(BBR_COLOR)) {

            InputStreamReader inStrReader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
            BufferedReader buffReader = new BufferedReader(inStrReader);
            String line = buffReader.readLine();

            while(line != null) {
                if(line.charAt(0) != '#') {
                    colors.add(line.substring(80,87).strip());
                }
                buffReader.readLine();
                line = buffReader.readLine();
            }
            return colors;

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    /** Gets the corresponding web Color for the given temperature in degrees.
     * @param temperatureInDeg
     * @return
     * @throws IllegalArgumentException if temp ( in kelvins ) is not in the interval [1000K, 40000K]
     */
    public static Color colorForTemperature(double temperatureInDeg) {
        //Check if the temperature is in the right interval
        Preconditions.checkInInterval(TEMP_INTERVAL, temperatureInDeg);
        int roundedTemp = (int) Math.round(temperatureInDeg / 100.0d) * 100;
        //the steps of the data are of 100 and the first value is a 1000.
        String colorCode = COLOR_LIST.get((roundedTemp - 1000)/ 100);

        return Color.web(colorCode);
    }

}
