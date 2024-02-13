package lab.entity;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.geometry.Point2D;
import lab.enums.Constants;
import lab.interfaces.Drawable;

import java.util.Random;

public class Cheese implements Drawable {
    private Image cheeseImage;
    private Point2D position;

    public Cheese(Image cheeseImage, Point2D position) {
        this.cheeseImage = cheeseImage;
        this.position = position;
    }
    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(cheeseImage, position.getX(), position.getY(), Constants.CHESSE_SIZE,Constants.CHESSE_SIZE);
    }

    @Override
    public void simulate(double deltaT) {

    }
    public Rectangle2D getBB() {
        double x = position.getX();
        double y = position.getY();
        double width = Constants.CHESSE_SIZE;
        double height = Constants.CHESSE_SIZE;
        return new Rectangle2D(x, y, width, height);
    }
}

