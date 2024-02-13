package lab.entity;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import lab.enums.Constants;
import lab.interfaces.MovingObjects;

import java.util.Random;

public class Cat implements MovingObjects {
    private Image catImage = new Image("Cat.gif");
    private Point2D position;
    private Point2D velocity;
    private boolean isFacingRight; // Indicates the current direction
    private Random random = new Random();
    private long lastCollisionTime;
    public Cat(Point2D initialPosition) {
        position = initialPosition;
        velocity = new Point2D(2, 0); // Adjust the speed and direction
        isFacingRight = true; // Initially, the cat faces right
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
        ImageView imageView = new ImageView(catImage);
        if (!isFacingRight) {
            imageView.setScaleX(-1);
        }
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT); // or your desired background color
        Image flippedCatImage = imageView.snapshot(params, null);
        gc.drawImage(flippedCatImage, position.getX(), position.getY(), Constants.CAT_SIZE, Constants.CAT_SIZE);
    }

    public void move() {
        double newX = position.getX() + velocity.getX();
        double newY = position.getY() + velocity.getY();

        double canvasWidth = Constants.GAME_WIDTH;
        if (newX < 0 || newX + Constants.CAT_SIZE > canvasWidth) {
            long currentTime = System.currentTimeMillis();
            long collisionCooldown = 500;

            if (currentTime - lastCollisionTime > collisionCooldown) {
                changeDirectionRandomly();
                lastCollisionTime = currentTime;
            }
        }
        position = new Point2D(newX, newY);
    }

    public void changeDirectionRandomly() {
        Random random = new Random();
        int n = random.nextInt(2);
        switch (n) {
            case 0:
                this.velocity = new Point2D(random.nextBoolean() ? 1 : -1, 0);
                this.isFacingRight = !this.isFacingRight;
                break;
            case 1:
                // Change direction in the vertical axis
                this.velocity = new Point2D(0, random.nextBoolean() ? 1 : -1);
                break;
        }
    }

    public void simulate(double deltaT) {
        double newX = position.getX() + velocity.getX() * deltaT;
        double newY = position.getY() + velocity.getY() * deltaT;
        position = new Point2D(newX, newY);
    }

    public Rectangle2D getBB() {
        double x = position.getX();
        double y = position.getY();
        double width = Constants.CAT_SIZE -5;
        double height = Constants.CAT_SIZE -5;

        return new Rectangle2D(x, y, width, height);
    }
}