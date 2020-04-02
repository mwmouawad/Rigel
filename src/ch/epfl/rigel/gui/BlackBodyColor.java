package ch.epfl.rigel.gui;

import java.io.*;
import javafx.scene.paint.Color;

//TODO : non instanciable == constructeur prive ?, class should be final ?
public class BlackBodyColor {

    private BlackBodyColor() throws IOException {}


    public static Color colorForTemperature(double temperatureInDeg) {
        //if n appartient pas exception.
        try {
            InputStream file = new FileInputStream("resources/bbr_color.txt");
            // Creates an InputStreamReader
            InputStreamReader input = new InputStreamReader(file);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return Color.web(null);
    }

}
