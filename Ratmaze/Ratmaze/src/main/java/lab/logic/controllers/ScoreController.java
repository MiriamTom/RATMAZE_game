package lab.logic.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TableRow;
import javafx.stage.Stage;
import lab.others.Score;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ScoreController {

    @FXML
    private TableView<Score> scoreTable;

    @FXML
    private TableColumn<Score, String> nameColumn;

    @FXML
    private TableColumn<Score, Integer> scoreColumn;
    private String playerName;

    public void initialize() {
        // Initialize the columns
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPlayerName()));
        scoreColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getScoreValue()).asObject());

        // Load and display existing scores
        //loadScore();

        // Customize row appearance
        scoreTable.setRowFactory(tv -> {
            TableRow<Score> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldScore, newScore) -> {
                if (newScore != null) {
                    // Mark the latest score played in red color
                    if (scoreTable.getItems().indexOf(newScore) == scoreTable.getItems().size() - 1) {
                        row.setStyle("-fx-text-fill: red;");
                    } else {
                        row.setStyle("");
                    }
                }
            });
            return row;
        });
    }

    public void loadScore() {
        // Clear existing items before loading new scores
        scoreTable.getItems().clear();

        // Use a set to keep track of unique player names
        Set<String> uniquePlayerNames = new HashSet<>();

        // Read data from the CSV file
        try (BufferedReader br = new BufferedReader(new FileReader("data.csv"))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    String playerName = parts[0];

                    // Check for duplicate player names
                    if (!uniquePlayerNames.contains(playerName)) {
                        int scoreValue = Integer.parseInt(parts[1]);
                        Score score = new Score(playerName, scoreValue);
                        scoreTable.getItems().add(score);
                        uniquePlayerNames.add(playerName);
                    }

                    if (lineNumber == getLatestScoreIndex()) {
                        markLatestScore();
                    }
                    lineNumber++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getLatestScoreIndex() {
        return scoreTable.getItems().size() - 1;
    }

    @FXML
    private void restart(ActionEvent event) throws IOException {
        FXMLLoader load = new FXMLLoader(getClass().getResource("/lab/Game.fxml"));
        BorderPane pane = load.load();
        Scene scene = new Scene(pane);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.resizableProperty().set(false);
        stage.setTitle("Ratmaze - TOM0468");
        stage.show();

        GameController gameController = load.getController();
        gameController.setSetGame(true);
        gameController.startGame(scene);

    }
    private void markLatestScore() {
        // Clear styles for all rows
        scoreTable.setRowFactory(tv -> new TableRow<>());

        // Set the text color to red for the latest row
        scoreTable.setRowFactory(tv -> {
            TableRow<Score> row = new TableRow<>();
            if (!row.isEmpty() && row.getIndex() == getLatestScoreIndex()) {
                row.setStyle("-fx-text-fill: red;");
            }
            return row;
        });
    }

}
