package lab.logic.controllers;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lab.DrawingThread;
import lab.others.Score;
import lab.logic.Game;


import java.io.FileWriter;
import java.io.IOException;

public class GameController {

    private Game game;
    private Game newGame;

    @FXML
    private Canvas canvas;
    @FXML
    private Canvas canvas2;

    private AnimationTimer animationTimer;
    private AnimationTimer newAnimationTimer;
    private Scene scene;
    private GameController win;
    private boolean setGame;
    private boolean canStart;

    @FXML
    private CheckBox music;
    private boolean isON;
    private int score;
    private String playerName;
    @FXML
    private TextField playerNameTextField;

    public GameController() {
        score = 0;
        win = this;
        setGame = false;
        canStart = true;
        isON=false;
    }

    public void startGame(Scene scene) throws IOException {
        if(setGame) {
            this.game = new Game(canvas2, scene, "src/main/resources/Map1.txt", isON, this);
            animationTimer = new DrawingThread(canvas2, scene, this);
            animationTimer.start();  // Start the thread immediately
        } else {
            this.game = new Game(canvas, scene, "src/main/resources/Map1.txt", isON, this);
            animationTimer = new DrawingThread(canvas, scene, this);
            animationTimer.start();  // Start the thread immediately
        }
    }

    public void setSetGame(boolean setGame) {
        this.setGame = setGame;
    }

    @FXML
    private void playButton(ActionEvent event) throws IOException {
        FXMLLoader load = new FXMLLoader(getClass().getResource("/lab/Game.fxml"));
        BorderPane pane = load.load();
        scene = new Scene(pane);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.resizableProperty().set(false);
        stage.setTitle("Ratmaze - TOM0468");
        stage.show();

        GameController newController = load.getController();
        newController.setGame = true;
        newController.playerName = playerNameTextField.getText();

        if (music.isSelected()) {
            newController.isON = true;

        }

        newController.startGame(scene);
        //newController.saveScore();

    }
    @FXML
    private void showScore(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lab/Over.fxml"));
            BorderPane pane = loader.load();
            Scene gameOverScene = new Scene(pane);

            Stage stage = (Stage) canvas.getScene().getWindow();
            stage.setScene(gameOverScene);

            ScoreController scoreController = loader.getController();
            scoreController.loadScore();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
    public void loadGameOverScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lab/Over.fxml"));
            BorderPane pane = loader.load();
            Scene gameOverScene = new Scene(pane);

           Stage stage = (Stage) canvas2.getScene().getWindow();
           stage.setScene(gameOverScene);

            ScoreController scoreController = loader.getController();
            scoreController.loadScore();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void gameOver() {
        saveScore();
        // Load "Over.fxml"
        loadGameOverScene();
    }
    public void saveScore() {
        try (FileWriter writer = new FileWriter("data.csv", true)) {
            writer.write(playerName + ";" + score + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setScore(int score) {
        this.score = score;
    }
}