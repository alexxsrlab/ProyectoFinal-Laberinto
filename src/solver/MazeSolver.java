package solver;

import models.Maze;
import views.MazePanel;

public interface MazeSolver {
    /**
     * Resuelve el laberinto y repinta el panel.
     * @return true si encontró solución, false en caso contrario.
     */
    boolean solve(Maze maze, MazePanel panel);

    /**
     * Ajusta el delay entre pasos (Paso a Paso).
     */
    default void setDelaySolucion(int milisegundos) {
        // Sobrescribir en las implementaciones
    }
}