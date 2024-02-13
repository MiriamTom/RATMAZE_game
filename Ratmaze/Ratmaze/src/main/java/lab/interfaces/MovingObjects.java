package lab.interfaces;

import javafx.scene.canvas.GraphicsContext;

public interface MovingObjects {
    int SIZE = 50;
    int getX();
    int getY();
    void draw(GraphicsContext gc);

}
