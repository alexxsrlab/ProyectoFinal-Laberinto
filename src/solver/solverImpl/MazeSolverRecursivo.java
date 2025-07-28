package solver.solverImpl;

import models.Cell;
import models.Maze;
import solver.MazeSolver;
import views.MazePanel;

import java.util.concurrent.Semaphore;

public class MazeSolverRecursivo implements MazeSolver {
    // ——— paso a paso
    private Semaphore pasoSem = null;
    @Override
    public void setPasoSemaphore(Semaphore pasoSem) {
        this.pasoSem = pasoSem;
    }
    private void esperarPaso() {
        if (pasoSem != null) {
            try { pasoSem.acquire(); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }
    // ————————————————————

    private Maze maze;
    private MazePanel panel;
    private int solutionDelay = 100;
    private boolean solutionFound = false;

    @Override
    public boolean solve(Maze maze, MazePanel panel) {
        this.maze = maze;
        this.panel = panel;
        limpiarMaze();
        solutionFound = false;

        // primer paso manual
        esperarPaso();

        boolean result = dfsLimited(maze.startX, maze.startY);
        panel.repaint();
        
        // if (!result) {
        //     panel.showMessage("No se encontró camino (solo busca derecha/abajo)");
        // }
        return result;
    }

    private boolean dfsLimited(int x, int y) {
        if (solutionFound) return true;
        if (x < 0 || x >= maze.rows || y < 0 || y >= maze.cols) return false;
        
        Cell cell = maze.grid[x][y];
        if (cell.wall || cell.visited) return false;

        // Marcar como visitado
        cell.visited = true;
        esperarPaso();
        panel.repaint();
        pausa();

        if (x == maze.endX && y == maze.endY) {
            cell.solution = true;
            solutionFound = true;
            return true;
        }

        // Solo explorar derecha (y+1) y abajo (x+1)
        if (dfsLimited(x, y + 1) || dfsLimited(x + 1, y)) {
            cell.solution = true;
            return true;
        }

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
        panel.repaint();
    }

    private void pausa() {
        try { 
            Thread.sleep(solutionDelay); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }
    }
}