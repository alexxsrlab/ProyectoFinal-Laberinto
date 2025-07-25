package solver.solverImpl;

import solver.MazeSolver;
import models.*;
import views.MazePanel;

import javax.swing.*;

// Recursivo con backtracking explícito y animación
public class MazeSolverRecursivoCompletoBT implements MazeSolver {
    private Maze maze;
    private MazePanel panel;

    @Override
    public boolean solve(Maze maze, MazePanel panel) {
        this.maze = maze;
        this.panel = panel;
        limpiarMaze(maze);
        boolean resultado = dfs(maze.startX, maze.startY);
        if (!resultado) {
            // El mensaje lo puede mostrar el controlador para evitar mezclar lógica y UI
            // Pero si quieres aquí está esta opción (recomendado controlarlo fuera)
            // JOptionPane.showMessageDialog(panel, "No se pudo encontrar una solución", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
        return resultado;
    }

    private boolean dfs(int x, int y) {
        if (x < 0 || x >= maze.rows || y < 0 || y >= maze.cols) return false;
        Cell cell = maze.grid[x][y];
        if (cell.wall || cell.visited) return false;

        cell.visited = true;
        cell.solution = true;

        panel.repaint();
        pausa();

        if (x == maze.endX && y == maze.endY) {
            return true;
        }

        if (dfs(x + 1, y) || dfs(x - 1, y) || dfs(x, y + 1) || dfs(x, y - 1)) {
            return true;
        }

        // Backtracking: desmarcar camino no válido
        cell.solution = false;
        panel.repaint();
        pausa();

        return false;
    }

    private void limpiarMaze(Maze maze) {
        for (int i = 0; i < maze.rows; i++)
            for (int j = 0; j < maze.cols; j++) {
                Cell c = maze.grid[i][j];
                c.visited = false;
                c.solution = false;
            }
        panel.repaint();
    }

    private void pausa() {
        try {
            Thread.sleep(100);  // pausa 100 ms, ajusta a tu gusto
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
