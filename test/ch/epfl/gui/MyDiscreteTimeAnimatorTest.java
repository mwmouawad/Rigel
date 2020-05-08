package ch.epfl.gui;

import ch.epfl.rigel.gui.DateTimeBean;
import ch.epfl.rigel.gui.NamedTimeAccelerator;
import ch.epfl.rigel.gui.TimeAccelerator;
import ch.epfl.rigel.gui.TimeAnimator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MyDiscreteTimeAnimatorTest extends Application {
    @Test
    public static void main(String[] args) { launch(args); }

    private LocalDate lastLocalDate;

    @Override
    public void start(Stage primaryStage) {
        ZonedDateTime simulatedStart =
                ZonedDateTime.parse("2020-06-01T23:55:00+01:00");
        TimeAccelerator accelerator =
                NamedTimeAccelerator.DAY.getAccelerator();


        DateTimeBean dateTimeB = new DateTimeBean();
        dateTimeB.setZonedDateTime(simulatedStart);

        TimeAnimator timeAnimator = new TimeAnimator(dateTimeB);
        timeAnimator.setAccelerator(accelerator);

        dateTimeB.dateProperty().addListener((p, o, n) -> {
            System.out.printf(" Nouvelle date : %s%n", n);
            if(lastLocalDate == null){
                System.out.println("Setting first local Date");
                this.lastLocalDate = n;
                return;
            }
            System.out.println(lastLocalDate.plusDays(1));
            assertEquals(lastLocalDate.plusDays(1), n);
            this.lastLocalDate = n;


            if(n.equals(simulatedStart.toLocalDate().plusDays(25))){
                Platform.exit();
            };
        });
        dateTimeB.timeProperty().addListener((p, o, n) -> {
            System.out.printf("Nouvelle heure : %s%n", n);
            //This should never be called.
            assertEquals(false, true);
        });


        timeAnimator.start();

    }




}
