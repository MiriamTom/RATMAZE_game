package lab.interfaces;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import lab.App;

public interface Controller {
    void init(App app, Pane pane);
    Scene getScene();
    Canvas getCanvas();
}
