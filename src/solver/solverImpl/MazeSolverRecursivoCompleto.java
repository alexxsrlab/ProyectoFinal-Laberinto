package solver.solverImpl;

import solver.MazeSolver;
import models.*;
import views.MazePanel;

public class MazeSolverRecursivoCompleto implements MazeSolver {
    private Maze maze;
    private MazePanel panel;
    private boolean solutionFound = false;

    @Override
    public boolean solve(Maze maze, MazePanel panel) {
        this.maze = maze;
        this.panel = panel;
        limpiarMaze();
        solutionFound = false;
        
        boolean result = dfsFullExploration(maze.startX, maze.startY);
        
        if (result) {
            marcarCaminoSolucion();
        }
        panel.repaint();
        return result;
    }

    private boolean dfsFullExploration(int x, int y) {
        if (solutionFound) return true;
        if (x < 0 || x >= maze.rows || y < 0 || y >= maze.cols) return false;
        
        Cell cell = maze.grid[x][y];
        if (cell.wall || cell.visited) return false;

        // Marcar como visitado (amarillo)
        cell.visited = true;
        panel.repaint();
        pausa();

        if (x == maze.endX && y == maze.endY) {
            solutionFound = true;
            cell.solution = true;
            return true;
        }

        // ORDEN DE EXPLORACIÓN MODIFICADO: Abajo, Derecha, Arriba, Izquierda
        if (dfsFullExploration(x + 1, y) ||  // Abajo 
            dfsFullExploration(x, y + 1) ||  // Derecha 
            dfsFullExploration(x - 1, y) ||  // Arriba 
            dfsFullExploration(x, y - 1)) {  // Izquierda 
            
            cell.solution = true;
            panel.repaint();
            pausa();
            return true;
        }

        return false;
    }

    private void marcarCaminoSolucion() {
        for (int i = 0; i < maze.rows; i++) {
            for (int j = 0; j < maze.cols; j++) {
                maze.grid[i][j].visited = false;
            }
        }
    }

    private void limpiarMaze() {
        for (int i = 0; i < maze.rows; i++) {
            for (int j = 0; j < maze.cols; j++) {
                Cell c = maze.grid[i][j];
                c.visited = false;
                c.solution = false;
            }
        }
    }

    private void pausa() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}