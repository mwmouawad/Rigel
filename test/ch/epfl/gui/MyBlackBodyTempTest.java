package ch.epfl.gui;
import ch.epfl.rigel.gui.BlackBodyColor;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MyBlackBodyTempTest {

    @Test
    void temperatureColorWorksForKnownValues() {
        Color result1 = BlackBodyColor.colorForTemperature(35225 + 273.15);
        assertEquals(255.0/255.0, result1.getBlue());
        assertEquals(189.0/255.0, result1.getGreen(), 1e-6);
        assertEquals(157.0/255.0, result1.getRed(), 1e-6);

        Color result2 = BlackBodyColor.colorForTemperature(1456 );
        assertEquals(0/255.0, result2.getBlue());
        assertEquals(109.0/255.0, result2.getGreen(), 1e-6);
        assertEquals(255.0/255.0, result2.getRed(), 1e-6);

        Color result3 = BlackBodyColor.colorForTemperature(40000);
        assertEquals(255.0/255.0, result3.getBlue());
        assertEquals(188.0/255.0, result3.getGreen(), 1e-6);
        assertEquals(155.0/255.0, result3.getRed(), 1e-6);

    }

}
