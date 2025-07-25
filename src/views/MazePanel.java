package views;

import controllers.MazeController;
import models.*;

import javax.swing.*;
import java.awt.*;

// Panel donde se dibuja el laberinto
public class MazePanel extends JPanel {
    private MazeController controller;
    private static final int CELL_SIZE = 50;

    public MazePanel(MazeController controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(controller.obtenerLaberinto().cols * CELL_SIZE,
                controller.obtenerLaberinto().rows * CELL_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Maze maze = controller.obtenerLaberinto();

        for (int i = 0; i < maze.rows; i++) {
            for (int j = 0; j < maze.cols; j++) {
                Cell cell = maze.grid[i][j];
                int x = j * CELL_SIZE;
                int y = i * CELL_SIZE;

                if (cell.wall) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                } else if (cell.solution) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                } else if (cell.visited) {
                    g.setColor(Color.YELLOW);
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                }

                // Dibujar bordes
                g.setColor(Color.GRAY);
                g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }

        // Dibujar inicio y fin
        int startX = maze.startY * CELL_SIZE;
        int startY = maze.startX * CELL_SIZE;
        int endX = maze.endY * CELL_SIZE;
        int endY = maze.endX * CELL_SIZE;

        g.setColor(Color.BLUE);
        g.fillOval(startX + CELL_SIZE / 4, startY + CELL_SIZE / 4, CELL_SIZE / 2, CELL_SIZE / 2);

        g.setColor(Color.RED);
        g.fillOval(endX + CELL_SIZE / 4, endY + CELL_SIZE / 4, CELL_SIZE / 2, CELL_SIZE / 2);
    }
}
