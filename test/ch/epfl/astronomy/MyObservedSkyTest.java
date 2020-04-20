package ch.epfl.astronomy;
import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.*;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.*;
import java.util.Optional;

import static ch.epfl.rigel.astronomy.HygDatabaseLoader.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyObservedSkyTest {

        String HYG_CATALOGUE_NAME ="/hygdata_v3.csv";
        String AST_CATALOGUE_NAME ="/asterisms.txt";
        StarCatalogue catalogue;
        StereographicProjection stereo= new StereographicProjection(HorizontalCoordinates.ofDeg(20, 22));
        GeographicCoordinates geographicCoordinates = GeographicCoordinates.ofDeg(30, 45);
        ZonedDateTime time = ZonedDateTime.of(LocalDate.of(2020, Month.APRIL, 4), LocalTime.of(0, 0), ZoneOffset.UTC);
        EquatorialToHorizontalConversion convEquToHor = new EquatorialToHorizontalConversion(time, geographicCoordinates);
        EclipticToEquatorialConversion convEcltoEqu = new EclipticToEquatorialConversion(time);
        StarCatalogue.Builder builder;

        {
            try(InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
                builder = new StarCatalogue.Builder().loadFrom(hygStream, INSTANCE);
            } catch (IOException e) {
                e.printStackTrace(); }
        }

        {
            try(InputStream hygStream1 = getClass().getResourceAsStream(AST_CATALOGUE_NAME)) {
                catalogue = builder.loadFrom(hygStream1, AsterismLoader.INSTANCE).build();
            } catch (IOException e) {
                e.printStackTrace(); } }

        ObservedSky sky = new ObservedSky(time,geographicCoordinates,stereo,catalogue);

        @Test
        void isObjectClosestTo(){
            assertEquals("Tau Phe",
                    sky.objectClosestTo(stereo.apply(new EquatorialToHorizontalConversion(time,geographicCoordinates)
                            .apply(EquatorialCoordinates.of(0.004696959812148989,-0.861893035343076))),0.1).get().name());

            assertEquals(Optional.empty(),
                    sky.objectClosestTo(stereo.apply(new EquatorialToHorizontalConversion(time,geographicCoordinates)
                            .apply(EquatorialCoordinates.of(0.004696959812148989,-0.8618930353430763))),0.001));
            //assertEquals("Lune", sky.objectClosestTo(stereo.apply(new EquatorialToHorizontalConversion(time,geographicCoordinates)
                    //  .apply(EquatorialCoordinates.of(sky.moonPosition().x(),sky.moonPosition().y()))),0.1).get().name());
            assertEquals("The Oct",sky.objectClosestTo(stereo.apply(new EquatorialToHorizontalConversion(time,geographicCoordinates)
                    .apply(EquatorialCoordinates.of(0.0069680833539428125,-1.3550506304308112))),0.1).get().name());


        }

}
