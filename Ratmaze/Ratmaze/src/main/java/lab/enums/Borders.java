package lab.enums;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Borders {
    private double x;
    private double y;
    private double width;
    private double height;
    private Color color;

    public Borders(double x, double y, double width, double height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(color); // Set the border color
        gc.fillRect(x, y, width, height);
    }

    public Rectangle2D getBB() {
        return new Rectangle2D(x, y, width, height);
    }
}
