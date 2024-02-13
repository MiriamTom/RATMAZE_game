package lab.entity;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import lab.enums.Constants;
import lab.interfaces.Drawable;
import lab.interfaces.MovingObjects;

public class Rat implements MovingObjects, Drawable {
    private Image ratImage;
    private Point2D position;
    private Point2D velocity ;
    private int width;
    private boolean checker;

    public Rat(Point2D initialPosition) {
        this.ratImage = new Image("Rat.gif");
        this.position = initialPosition;
        this.velocity = new Point2D(2,0);
        this.width = 58;
        this.checker = true;
    }
    @Override
    public int getX() {
        return (int) position.getX();
    }
    @Override
    public int getY() {
        return (int) position.getY();
    }
    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(ratImage, position.getX() + (width < 0 ? -width : 0), position.getY(),width, Constants.RAT_SIZE);
    }
    public void move(KeyCode keyPressed) {
        double newX = position.getX();
        double newY = position.getY();

        if (keyPressed == KeyCode.W) {
            newY -= 5; // Move up
        } else if (keyPressed == KeyCode.S) {
            newY += 5; // Move down
        } else if (keyPressed == KeyCode.A) {
            newX -= 5; // Move left
            if(checker){
            this.width *=-1;

            checker = false;
            }

        } else if (keyPressed == KeyCode.D) {
            newX += 5; // Move right
            if(!checker){
                this.width *=-1;

                checker = true;
            }
        }
        position = new Point2D(newX, newY);
    }
    public Rectangle2D getBB() {
        double x = position.getX() ;
        double y = position.getY() ;
        double width = Math.abs(this.width);
        double height = SIZE -20;

        return new Rectangle2D(x, y, width -8 , height);
    }
    @Override
    public void simulate(double deltaT){
        double newX = position.getX() + velocity.getX() * deltaT;
        double newY = position.getY() + velocity.getY() * deltaT;

        position = new Point2D(newX, newY);

    }
    public void setPosition(Point2D newPosition) {
        this.position = newPosition;
    }
}