package lab.logic;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lab.logic.controllers.GameController;
import lab.enums.Map;
import lab.logic.controllers.ScoreController;
import lab.others.SoundPlayer;
import lab.entity.Cat;
import lab.entity.Cheese;
import lab.entity.Rat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lab.enums.Borders;
import lab.enums.Constants;


public class Game {
    private  Rat rat;
    private  Canvas canvas;
    private  Scene scene;
    private int many, newObj;
    private  ArrayList<Cheese> cheese = new ArrayList<>();
    private  ArrayList<Cat> cats = new ArrayList<>();
    private ArrayList<Borders> mazeBorders;
    private Random rand = new Random();
    private boolean end;
    private int score;
    //private final Map map;
    private int currentMapIndex = 0;
    private List<Map> maps = new ArrayList<>();
    private SoundPlayer soundPlayer;
    private boolean isON;
    private  GameController gameController;
    private ScoreController scoreController;
    private Image catPianoImage;

    private boolean show;
    public Game(Canvas canvas, Scene scene, String mapFilePath, boolean isON, GameController gameController) {
        this.canvas = canvas;
        Point2D initialPosition = Constants.RAT_START;
        rat = new Rat(initialPosition);
        mazeBorders = new ArrayList<>();
        this.gameController = gameController;

        this.scene = scene;
        this.newObj = 0;
        this.many = 0;
        this.score = 0;
        this.end = false;

        this.show = isON;

        maps.add(new Map("src/main/resources/" + "Map1.txt", 0));
        maps.add(new Map("src/main/resources/" + "Map2.txt", 1));
        maps.add(new Map("src/main/resources/" + "Map3.txt", 2));
        maps.add(new Map("src/main/resources/" + "Map4.txt", 3));
        this.catPianoImage = new Image("Cat_piano.gif");
        String soundFilePath = "src/main/resources/Music.wav";
        soundPlayer = new SoundPlayer(soundFilePath);
        if (isON) {
            soundPlayer.play();


        }
    }

    public void update() {
        checkWinCondition();
    }
    private void checkWinCondition()  {
        double ratX = rat.getX();
        double ratY = rat.getY();

        if (isNearWinPoint(Constants.WINPOINT, ratX, ratY) && currentMapIndex > 2 ) {
            // Handle game over logic (e.g., show "Game Over" screen)
            handleGameOver();
        }
    }
    private void handleGameOver()  {
        gameController.setScore(this.score);
        gameController.gameOver();
    }

    private boolean isNearWinPoint(Point2D winPoint, double ratX, double ratY) {
        double distance = winPoint.distance(ratX, ratY);
        return distance < Constants.CHECKPOINT_RADIUS; // Adjust the radius as needed
    }

    public void addObjects() {
        int maxCheese = Constants.CHESSE_POINTS;
        int maxCats = Constants.CATS_ENEMIES;
        Image cheeseImage = new Image("Chesse.png");
        Random random = new Random();
        int canvasWidth = Constants.GAME_WIDTH; // Adjust this to your canvas width
        int canvasHeight = Constants.GAME_HEIGHT; // Adjust this to your canvas height

        for (int i = 0; i < maxCheese; i++) {
            double randomX = random.nextDouble() * canvasWidth;
            double randomY = random.nextDouble() * canvasHeight;
            Point2D randomPosition = new Point2D(randomX, randomY);

            if(checkChesseCollision((int) randomX,(int)randomY)){
                i--;
            }
            else{cheese.add(new Cheese(cheeseImage, randomPosition));
            }
        }

            for(int i = 0; i < maxCats; i++) {

                double randomX = random.nextDouble() * canvasWidth;
                double randomY = random.nextDouble() * canvasHeight;
                Point2D randomPosition = new Point2D(randomX, randomY);

                if(checkCatCollision((int) randomX,(int)randomY)){
                    i--;
                }
                else{cats.add(new Cat(randomPosition));
                }

            }
    }
    public boolean checkChesseCollision(int randX,int randY) {
        for (int i = 0; i < (int) Constants.GAME_WIDTH / Constants.CELL_WIDTH; i++) {
            for (int j = 0; j < (int) Constants.GAME_HEIGHT / Constants.CELL_HEIGHT; j++) {
                Rectangle2D wall = new Rectangle2D(i * Constants.CELL_WIDTH, j * Constants.CELL_HEIGHT, 60, 60);
                Rectangle2D chesse = new Rectangle2D(randX, randY, Constants.CHESSE_SIZE, Constants.CHESSE_SIZE);
                if (maps.get(currentMapIndex).getMapData()[j][i] == 1) {
                    if (chesse.intersects(wall)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private boolean checkCatCollision(int randX,int randY) {
        for (int i = 0; i < (int) Constants.GAME_WIDTH / Constants.CELL_WIDTH; i++) {
            for (int j = 0; j < (int) Constants.GAME_HEIGHT / Constants.CELL_HEIGHT; j++) {
                Rectangle2D wall = new Rectangle2D(i * Constants.CELL_WIDTH+5, j * Constants.CELL_HEIGHT+5, 60, 60);
                Rectangle2D cat = new Rectangle2D(randX, randY, Constants.CAT_SIZE, Constants.CAT_SIZE);
                if (maps.get(currentMapIndex).getMapData()[j][i] == 1) {
                    if (cat.intersects(wall)) {
                        return true;
                    }
                }
            }
        }

        // Check if the cat collides with mazeBorders
        for (Borders border : mazeBorders) {
            Rectangle2D cat = new Rectangle2D(randX, randY, Constants.CAT_SIZE, Constants.CAT_SIZE);
            if (cat.intersects(border.getBB())) {
                return true;
            }
        }

        return false;
    }

    public void moveObjects() {
        for (Cat cat : cats) {
            cat.move();
            // Check for collisions with borders or the map
            if (checkCatCollision(cat.getX(), cat.getY())) {
                // Change direction
                cat.changeDirectionRandomly();

            }
        }

        // Check for collisions with cheese
        for (int i = cheese.size() - 1; i >= 0; i--) {
            Cheese c = cheese.get(i);
            if (rat.getBB().intersects(c.getBB())) {
                // Remove the cheese from the list
                cheese.remove(i);
                // Increase the score
                score += Constants.CHESSE_POINTS;
            }
        }
    }
    public void handleKeyPress(KeyCode keyPressed) {
        // Check if the key is a valid input for the rat
        if (keyPressed == KeyCode.W || keyPressed == KeyCode.S || keyPressed == KeyCode.A || keyPressed == KeyCode.D) {
            // Call the rat's move method to update its position
            Point2D newPosition = new Point2D(rat.getX(), rat.getY());
            rat.move(keyPressed);
            if(checkCollision()){
                rat.setPosition(newPosition);
            }
        }
        // Handle other input events for your game
    }
    public void drawMaze(GraphicsContext gc) {

        mazeBorders.add(new Borders(0, 0, Constants.GAME_WIDTH, 10, Color.SADDLEBROWN));
        mazeBorders.add(new Borders(0, Constants.GAME_HEIGHT - 10, Constants.GAME_WIDTH, 20, Color.SADDLEBROWN));
        mazeBorders.add(new Borders(0, 0,  10,Constants.GAME_HEIGHT, Color.SADDLEBROWN));
        mazeBorders.add(new Borders(Constants.GAME_WIDTH-10, 0, 10, Constants.GAME_HEIGHT, Color.SADDLEBROWN));
        for (Borders border : mazeBorders) {
            border.draw(gc);
        }
        maps.get(currentMapIndex).drawMap(gc);
    }
    public void drawBackground(GraphicsContext gc) {
        // Set the background color
        gc.setFill(Color.LAVENDER); // Change to your desired background color

        // Fill a rectangle to cover the canvas
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void drawObjects(GraphicsContext gc) {

        // Clear the canvas
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw the background
        drawBackground(gc);
        gc.save();
        rat.draw(gc);
        gc.restore();

        // Draw the cats
        for (Cat cat : cats) {
            cat.draw(gc);
        }

        // Draw the cheese
        for (Cheese cheese : cheese) {
            cheese.draw(gc);
        }
        // Draw the score
        // Draw other game objects as needed
        drawMaze(gc);
        gc.setFill(Color.INDIGO);
        gc.setFont(Font.font(25));
        gc.fillText("Score: " + score, 35, 520);

        if (show){
            gc.drawImage(this.catPianoImage, Constants.GAME_WIDTH - 60, Constants.GAME_HEIGHT - 60, 60, 60);
        }

    }
    public boolean checkCollision() {

        for(int i = 0; i < ( int )Constants.GAME_WIDTH/Constants.CELL_WIDTH ; i++) {
            for (int j = 0; j < (int) Constants.GAME_HEIGHT / Constants.CELL_HEIGHT; j++) {
                Rectangle2D wall = new Rectangle2D ( i *Constants.CELL_WIDTH -5, j*Constants.CELL_HEIGHT-5,60,60 );
                if(maps.get(currentMapIndex).getMapData()[j][i]==1) {
                    if (rat.getBB().intersects(wall)){
                        return true;
                    }

                }
            }
        }

        // If no collision with borders, check the mazeBorders
        for (Borders border : mazeBorders) {
            if (rat.getBB().intersects(border.getBB())) {
                return true;
            }
        }
        for (Cat cat : cats) {
            if (rat.getBB().intersects(cat.getBB())) {
                // Destroy the cat
                cats.remove(cat);
                // Decrease the score
                score -= 10;
                return true;
            }
        }

        // Check for collision with checkpoints
        if (isNearCheckpoint(Constants.CHECKPOINT, rat.getX(), rat.getY()) && currentMapIndex==0) {
            switchMap(1); // Switch to Map 2
            initializeGame();
            rat.setPosition(new Point2D(40,360));

            return false;  // Make sure to return false to prevent further collisions in the current frame
        }
        if (isNearCheckpoint(Constants.CHECKPOINT2, rat.getX(), rat.getY()) && currentMapIndex==1) {
            switchMap(2); // Switch to Map 3
            initializeGame();
            rat.setPosition(new Point2D(600,40));
            return false;
        }
        if (isNearCheckpoint(Constants.CHECKPOINT3, rat.getX(), rat.getY()) && currentMapIndex==2) {
            switchMap(3); // Switch to Map 3
            initializeGame();
            rat.setPosition(new Point2D(900,100));
            return false;
        }

        return false;
    }
    private boolean isNearCheckpoint(Point2D checkpoint, double ratX, double ratY) {
        double distance = checkpoint.distance(ratX, ratY);
        return distance < Constants.CHECKPOINT_RADIUS; // Adjust the radius as needed
    }
    private void switchMap(int newIndex) {
        if (newIndex >= 0 && newIndex < maps.size()) {
            currentMapIndex = newIndex;
        }
    }
    public void simulate(double deltaT){
        if(!checkCollision()){
            rat.simulate(deltaT);
        }
        //rat.simulate(deltaT);
        for (Cat cat : cats) {
            cat.simulate(deltaT);
        }
        this.update();
    }
    public void initializeGame() {
        end = false; // Reset the game end flag

        // Clear existing objects
        cheese.clear();
        cats.clear();
        mazeBorders.clear();

        // Set  rat
        Point2D initialPosition = Constants.RAT_START;
        rat.setPosition(initialPosition);

        // Set maze borders
        mazeBorders.add(new Borders(0, 0, Constants.GAME_WIDTH, 10, Color.INDIGO));
        mazeBorders.add(new Borders(0, Constants.GAME_HEIGHT - 10, Constants.GAME_WIDTH, 20, Color.INDIGO));
        mazeBorders.add(new Borders(0, 0, 10, Constants.GAME_HEIGHT, Color.INDIGO));
        mazeBorders.add(new Borders(Constants.GAME_WIDTH - 10, 0, 10, Constants.GAME_HEIGHT, Color.INDIGO));

        // Add  cheese and cats
        addObjects();
    }
}