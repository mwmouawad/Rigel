package ch.epfl.gui;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.gui.ObserverLocationBean;
import org.junit.jupiter.api.Test;


public class ObserverLocationBeanTest {

    @Test
    void bindingWorksAsExpected(){
        ObserverLocationBean observerLocationBean = new ObserverLocationBean();
        observerLocationBean.setLonDeg(10.0);
        observerLocationBean.setLatDeg(13);
       // observerLocationBean.setCoordinates(GeographicCoordinates.ofDeg(12.5, 78));
        observerLocationBean.setLonDeg(5.0);
        assertEquals(5.00, observerLocationBean.getCoordinates().lonDeg());
        assertEquals(13, observerLocationBean.getCoordinates().latDeg());


    }

}
