package lab.enums;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lab.enums.Constants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Map {

    private int[][] mapData;
    private Color[][] borderColors; // Store colors for each wall
    private int borderColorIndex;
    public Map(String filePath, int borderColorIndex) {
        this.borderColorIndex = borderColorIndex;
        loadMap(filePath);
    }

    public int[][] getMapData() {
        return mapData;
    }
    public void loadMap(String filePath) {
        List<int[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(", ");
                int[] row = new int[values.length];
                for (int i = 0; i < values.length; i++) {
                    row[i] = Integer.parseInt(values[i]);
                }
                rows.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mapData = new int[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            mapData[i] = rows.get(i);
        }
        borderColors = new Color[mapData.length][mapData[0].length];
        for (int row = 0; row < mapData.length; row++) {
            for (int col = 0; col < mapData[row].length; col++) {
                if (mapData[row][col] == 1) {
                    borderColors[row][col] = getBorderColor(borderColorIndex);
                }
            }
        }
    }
    private Color getBorderColor(int index) {
        Color[] availableColors = Constants.BORDER_COLORS;
        if (index >= 0 && index < availableColors.length) {
            return availableColors[index];
        } else {
            return Color.BLACK;
        }
    }

    public void drawMap(GraphicsContext gc) {
        if (mapData != null) {
            double cellWidth = Constants.CELL_WIDTH;
            double cellHeight = Constants.CELL_HEIGHT;

            for (int row = 0; row < mapData.length; row++) {
                for (int col = 0; col < mapData[row].length; col++) {
                    double x = col * cellWidth;
                    double y = row * cellHeight;

                    if (mapData[row][col] == 1) {
                        drawBorder(gc, x, y, cellWidth, cellHeight, borderColors[row][col]);
                    }
                }
            }
        }
    }
    private void drawBorder(GraphicsContext gc, double x, double y, double width, double height, Color color) {
        gc.setFill(color);
        gc.fillRect(x, y, width, height);
    }
}