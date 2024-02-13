package lab;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Button;
import lab.logic.Game;
import lab.logic.controllers.GameController;

import java.io.IOException;

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	private Canvas canvas;
	private AnimationTimer timer;
	private Game game;

	GameController controller;


	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu3.fxml"));
			BorderPane pane = loader.load();
			Scene scene = new Scene(pane);

			primaryStage.setScene(scene);
			primaryStage.resizableProperty().set(false);
			primaryStage.setTitle("RATMAZE");
			primaryStage.show();

			controller = loader.getController();
			controller.startGame(scene);

			scene.setOnKeyPressed(event -> {
				KeyCode keyPressed = event.getCode();
				game.handleKeyPress(keyPressed);
			});

			Button playButton = new Button("PLAY");
			playButton.setOnAction(e -> {
				game.initializeGame();
			});

			primaryStage.setOnCloseRequest(this::exitProgram);

			// Set up key event handler for the scene
			scene.setOnKeyPressed(event -> {
				KeyCode keyPressed = event.getCode();
				// Pass the key press to your game for handling
				game.handleKeyPress(keyPressed);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void stop() throws Exception {
		timer.stop();
		super.stop();
	}
	
	private void exitProgram(WindowEvent evt) {
		System.exit(0);
	}
}