package lab.interfaces;

import javafx.scene.canvas.GraphicsContext;

public interface Drawable {
    void draw(GraphicsContext gc);
    void simulate(double deltaT);
}
