package ch.epfl.rigel.gui;


import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.coordinates.StereographicProjection;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Transform;

public class SkyCanvasPainter {

    Canvas canvas;
    GraphicsContext graphic;

    public  SkyCanvasPainter(Canvas canvas) {
        this.canvas = canvas;
        graphic = canvas.getGraphicsContext2D();
    }

    //TODO : what do you mean by clear ?
    public void clear() { graphic.clearRect(0,0, canvas.getWidth(), canvas.getHeight());}

    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {}

    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {}

    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {}

    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {}

    public void drawHorizon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {}
}
