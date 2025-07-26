package views;

import controllers.MazeController;
import models.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MazePanel extends JPanel implements MouseListener, MouseMotionListener {
    private MazeController controller;
    private static final int CELL_SIZE = 50;
    private boolean settingStart = false;
    private boolean settingEnd = false;
    private boolean settingWalls = false;
    private boolean removingWalls = false;

    public MazePanel(MazeController controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(
            controller.obtenerLaberinto().cols * CELL_SIZE,
            controller.obtenerLaberinto().rows * CELL_SIZE
        ));
        
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Maze maze = controller.obtenerLaberinto();

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int cellWidth = panelWidth / maze.cols;
        int cellHeight = panelHeight / maze.rows;
        int cellSize = Math.min(cellWidth, cellHeight);

        // Calcular offsets para centrar el laberinto
        int offsetX = (panelWidth - (cellSize * maze.cols)) / 2;
        int offsetY = (panelHeight - (cellSize * maze.rows)) / 2;

        for (int i = 0; i < maze.rows; i++) {
            for (int j = 0; j < maze.cols; j++) {
                Cell cell = maze.grid[i][j];
                int x = offsetX + j * cellSize;
                int y = offsetY + i * cellSize;

                if (cell.wall) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, cellSize, cellSize);
                } else if (cell.solution) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x, y, cellSize, cellSize);
                } else if (cell.visited) {
                    g.setColor(Color.YELLOW);
                    g.fillRect(x, y, cellSize, cellSize);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(x, y, cellSize, cellSize);
                }

                g.setColor(Color.GRAY);
                g.drawRect(x, y, cellSize, cellSize);
            }
        }

        // Dibujar inicio y fin
        drawStartAndEnd(g, cellSize, offsetX, offsetY);
    }

    private void drawStartAndEnd(Graphics g, int cellSize, int offsetX, int offsetY) {
        Maze maze = controller.obtenerLaberinto();
        if (maze.startX >= 0 && maze.startY >= 0) {
            int startX = offsetX + maze.startY * cellSize;
            int startY = offsetY + maze.startX * cellSize;
            g.setColor(Color.BLUE);
            g.fillOval(startX + cellSize / 4, startY + cellSize / 4, cellSize / 2, cellSize / 2);
        }
        if (maze.endX >= 0 && maze.endY >= 0) {
            int endX = offsetX + maze.endY * cellSize;
            int endY = offsetY + maze.endX * cellSize;
            g.setColor(Color.RED);
            g.fillOval(endX + cellSize / 4, endY + cellSize / 4, cellSize / 2, cellSize / 2);
        }
    }

    // Métodos para activar los diferentes modos
    public void setStartPositionMode() {
        settingStart = true;
        settingEnd = false;
        settingWalls = false;
        removingWalls = false;
    }

    public void setEndPositionMode() {
        settingStart = false;
        settingEnd = true;
        settingWalls = false;
        removingWalls = false;
    }

    public void setWallMode() {
        settingStart = false;
        settingEnd = false;
        settingWalls = true;
        removingWalls = false;
    }

    public void setRemoveWallMode() {
        settingStart = false;
        settingEnd = false;
        settingWalls = false;
        removingWalls = true;
    }

    // Métodos del MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {
        handleCellClick(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        handleCellClick(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        handleCellClick(e.getX(), e.getY());
    }

    private void handleCellClick(int mouseX, int mouseY) {
        Maze maze = controller.obtenerLaberinto();

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int cellWidth = panelWidth / maze.cols;
        int cellHeight = panelHeight / maze.rows;
        int cellSize = Math.min(cellWidth, cellHeight);

        int offsetX = (panelWidth - (cellSize * maze.cols)) / 2;
        int offsetY = (panelHeight - (cellSize * maze.rows)) / 2;

        int col = (mouseX - offsetX) / cellSize;
        int row = (mouseY - offsetY) / cellSize;

        if (row >= 0 && row < maze.rows && col >= 0 && col < maze.cols) {
            if (settingStart) {
                maze.startX = row;
                maze.startY = col;
            } else if (settingEnd) {
                maze.endX = row;
                maze.endY = col;
            } else if (settingWalls) {
                maze.grid[row][col].wall = true;
            } else if (removingWalls) {
                maze.grid[row][col].wall = false;
            }
            repaint();
        }
    }

    // Métodos no utilizados (pero requeridos por las interfaces)
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}

    public void showMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, 
                message, 
                "Resultado de la búsqueda", 
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
}