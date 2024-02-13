package lab.others;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

public class Score {
    private final String playerName;
    private final int scoreValue;

    public Score(String playerName, int scoreValue) {
        this.playerName = playerName;
        this.scoreValue = scoreValue;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScoreValue() {
        return scoreValue;
    }
}
