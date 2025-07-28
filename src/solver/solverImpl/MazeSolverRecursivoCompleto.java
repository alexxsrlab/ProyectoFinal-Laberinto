package solver.solverImpl;

import models.Cell;
import models.Maze;
import solver.MazeSolver;
import views.MazePanel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class MazeSolverRecursivoCompleto implements MazeSolver {
    // ——— paso a paso ———
    private Semaphore pasoSem = null;

    @Override
    public void setPasoSemaphore(Semaphore pasoSem) {
        this.pasoSem = pasoSem;
    }

    private void esperarPaso() {
        if (pasoSem != null) {
            try {
                pasoSem.acquire();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    // ————————————————————

    private Maze maze;
    private MazePanel panel;
    private int solutionDelay = 100;
    private boolean solutionFound = false;
    private List<Cell> caminoFinal = new ArrayList<>(); // Guarda el camino final

    @Override
    public boolean solve(Maze maze, MazePanel panel) {
        this.maze = maze;
        this.panel = panel;
        limpiarMaze();
        solutionFound = false;
        caminoFinal.clear();

        esperarPaso();
        boolean found = dfsFull(maze.startX, maze.startY);

        if (found) {
            mostrarCaminoPasoAPaso(); // Paso a paso del camino final
        } else {
            panel.showMessage("No se encontró solución");
        }
        panel.repaint();
        return found;
    }

    private boolean dfsFull(int x, int y) {
        if (solutionFound) return true;
        if (x < 0 || x >= maze.rows || y < 0 || y >= maze.cols) return false;

        Cell cell = maze.grid[x][y];
        if (cell.wall || cell.visited) return false;

        // Marcar como visitado (amarillo)
        cell.visited = true;
        esperarPaso();
        panel.repaint();
        pausa();

        if (x == maze.endX && y == maze.endY) {
            solutionFound = true;
            caminoFinal.add(cell); // Agrega el final al camino
            return true;
        }

        // Exploración recursiva
        if (dfsFull(x + 1, y) || dfsFull(x, y + 1) || dfsFull(x - 1, y) || dfsFull(x, y - 1)) {
            caminoFinal.add(cell); // Solo agrega si es parte del camino correcto
            return true;
        }

        return false;
    }

    private void mostrarCaminoPasoAPaso() {
        for (int i = caminoFinal.size() - 1; i >= 0; i--) {
            Cell cell = caminoFinal.get(i);
            cell.solution = true; // Marcar como parte del camino final (verde)
            esperarPaso(); // Paso a paso para el camino final
            panel.repaint();
            pausa();
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
            Thread.sleep(solutionDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
