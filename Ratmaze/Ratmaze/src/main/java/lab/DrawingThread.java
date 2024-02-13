package lab;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import lab.logic.Game;
import lab.logic.controllers.GameController;


public class DrawingThread extends AnimationTimer {
	private  Canvas canvas;
	private  GraphicsContext gc;
	private  Game game;
	private Scene scene;
	private long lasttime = -1;

	public DrawingThread(Canvas canvas, Scene scene, GameController gameController) {
		this.canvas = canvas;
		this.gc = canvas.getGraphicsContext2D();
		this.game = new Game(canvas, scene, "src/main/resources/Map1.txt", false, gameController);
		this.scene = scene;
		game.addObjects();
		game.moveObjects();
	}

	@Override
	public void handle(long now) {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		game.moveObjects();
		game.drawObjects(gc);
		Routines.sleep(5);
		scene.setOnKeyPressed(event -> {
			KeyCode keyCode = event.getCode();
			game.handleKeyPress(keyCode);
		});

		if (lasttime > 0) {
			//time are in nanoseconds and method simulate expects seconds
			game.simulate((now - lasttime) / 1e9);
		}
		lasttime = now;

	}
}