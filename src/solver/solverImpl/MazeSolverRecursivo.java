package solver.solverImpl;

import solver.MazeSolver;
import models.*;
import views.MazePanel;

public class MazeSolverRecursivo implements MazeSolver {
    private Maze maze;
    private MazePanel panel;
    private boolean solutionFound = false;

    @Override
    public boolean solve(Maze maze, MazePanel panel) {
        this.maze = maze;
        this.panel = panel;
        limpiarMaze();
        solutionFound = false;
        
        boolean result = dfsRightDownOnly(maze.startX, maze.startY);
        
        // if (!result) {
        //     panel.showMessage("No se encontró camino (solo busca derecha/abajo)");
        // }
        panel.repaint();
        return result;
    }

    private boolean dfsRightDownOnly(int x, int y) {
        // Si ya encontramos solución, terminar
        if (solutionFound) return true;
        
        // Verificar límites del laberinto
        if (x < 0 || x >= maze.rows || y < 0 || y >= maze.cols) return false;
        
        Cell cell = maze.grid[x][y];
        // Verificar si es pared o ya visitado
        if (cell.wall || cell.visited) return false;

        // Marcar como visitado y parte de la solución
        cell.visited = true;
        cell.solution = true;
        
        // Actualizar interfaz
        panel.repaint();
        pausa();

        // Verificar si llegamos al final
        if (x == maze.endX && y == maze.endY) {
            solutionFound = true;
            return true;
        }

        // Solo explorar derecha (y+1) y abajo (x+1)
        if (dfsRightDownOnly(x, y + 1) || dfsRightDownOnly(x + 1, y)) {
            return true;
        }

        // Si no encontró solución, desmarcar (pero sin mostrar en UI)
        cell.solution = false;
        return false;
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