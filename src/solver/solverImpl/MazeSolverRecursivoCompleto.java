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
        
        // Marcar el camino solución final
        if (result) {
            marcarCaminoSolucion();
        // } else {
        //     panel.showMessage("No se encontró camino con exploración completa");
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
            cell.solution = true; // Marcar final como parte de la solución
            return true;
        }

        // Explorar en 4 direcciones
        if (dfsFullExploration(x, y + 1) ||  // Derecha
            dfsFullExploration(x + 1, y) ||  // Abajo
            dfsFullExploration(x, y - 1) ||  // Izquierda
            dfsFullExploration(x - 1, y)) { // Arriba
            
            // Marcar como parte de la solución (verde)
            cell.solution = true;
            panel.repaint();
            pausa();
            return true;
        }

        return false;
    }

    private void marcarCaminoSolucion() {
        // Resetear todas las celdas visitadas para mostrar solo el camino solución
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
            Thread.sleep(100); // Ajusta este valor para cambiar la velocidad
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}