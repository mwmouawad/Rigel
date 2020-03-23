package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;

import java.util.Objects;

public final class Star extends CelestialObject {

    private int hipparcosId;
    private final ClosedInterval interval = ClosedInterval.of(-0.5, 5.5);
    private int colorTemperature;
    /**
     * @param name          of the celestial object
     * @param equatorialPos equatorial position of the object
     * @param angularSize   size of the object
     * @param magnitude     magnitude of the object
     * @throws NullPointerException     if the name or the equatorial position are null
     * @throws IllegalArgumentException if the angular size is less than 0.
     */
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex){
        super(name, equatorialPos, 0, magnitude);
        Preconditions.checkArgument(hipparcosId >= 0);

        this.hipparcosId = hipparcosId;
        float color = (float) Preconditions.checkInInterval(interval, colorIndex);
        colorTemperature = (int) (4600d*((1d/(0.92*color + 1.7))+ (1d/(0.92*color + 0.62))));
    }

    public int hipparcosId() { return hipparcosId; }

    public int colorTemperature() { return colorTemperature; }
}
