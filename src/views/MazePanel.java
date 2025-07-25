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

                g.setColor(Color.GRAY);
                g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }

        // Dibujar inicio y fin
        drawStartAndEnd(g);
    }

    private void drawStartAndEnd(Graphics g) {
        Maze maze = controller.obtenerLaberinto();
        int startX = maze.startY * CELL_SIZE;
        int startY = maze.startX * CELL_SIZE;
        int endX = maze.endY * CELL_SIZE;
        int endY = maze.endX * CELL_SIZE;

        g.setColor(Color.BLUE);
        g.fillOval(startX + CELL_SIZE / 4, startY + CELL_SIZE / 4, CELL_SIZE / 2, CELL_SIZE / 2);

        g.setColor(Color.RED);
        g.fillOval(endX + CELL_SIZE / 4, endY + CELL_SIZE / 4, CELL_SIZE / 2, CELL_SIZE / 2);
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
        int col = mouseX / CELL_SIZE;
        int row = mouseY / CELL_SIZE;
        Maze maze = controller.obtenerLaberinto();

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