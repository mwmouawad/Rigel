package ch.epfl.rigel.gui;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;
import javafx.scene.paint.Color;

/**
 * Non instanciable class, meant to be used to get the corresponding color in degrees kelvin from a celestial object
 * to it Color object representation.
 * Loads the information from 'bbr_color.txt'.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public class BlackBodyColor {

    private static final String BBR_COLOR = "/bbr_color.txt";
    private static final Interval tempInterval = ClosedInterval.of(1000, 40000);
    private static final Map<Integer, String> colorMap = loadColors();

    private BlackBodyColor() throws IOException {
    }


    private static Map loadColors() {

        Map<Integer, String> map = new HashMap<Integer, String>();

        try (InputStream inputStream = BlackBodyColor.class.getResourceAsStream(BBR_COLOR)) {

            InputStreamReader inStrReader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
            BufferedReader buffReader = new BufferedReader(inStrReader);

            String line;
            String[] charTable;
            String colorCode = null;

            for (int i = 0; i < 18; i++) buffReader.readLine();
            line = buffReader.readLine();
            int lineNb = 19;


            while (line != null) {

                System.out.println(lineNb);
                if (lineNb > 19 && lineNb < 802) {
                    StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < 6; i++) {
                        if (line.charAt(i) != ' ')
                            sb.append(line.charAt(i));

                    }

                    int temp = Integer.parseInt(sb.toString());
                    sb = new StringBuilder();

                    for (int i = 79; i < 87; i++) {
                        if (line.charAt(i) != ' ') {
                            sb.append(line.charAt(i));
                        }
                    }
                    colorCode = sb.toString();
                    map.put(temp, colorCode);
                }

                lineNb += 2;
                buffReader.readLine();
                line = buffReader.readLine();

            }

            return map;

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }


    /** Gets the corresponding web Color for the given tempetature in degrees.
     * @param temperatureInDeg
     * @return
     * @throws IllegalArgumentException if temp ( in kelvins ) is not in the interval [1000K, 40000K]
     */
    public static Color colorForTemperature(double temperatureInDeg) {


        //Check if the temperature is in the right interval
        Preconditions.checkInInterval(tempInterval, temperatureInDeg);

        int roundedTemp = (int) Math.round(temperatureInDeg / 100.0d) * 100;


        String colorCode = colorMap.get(roundedTemp);

        return Color.web(colorCode);


    }

}
