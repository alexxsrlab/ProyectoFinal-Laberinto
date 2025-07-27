package solver;

import models.Maze;
import views.MazePanel;
import java.util.concurrent.Semaphore;

public interface MazeSolver {
    /**  
     * Resuelve el laberinto. Devuelve true si encontró solución.  
     */
    boolean solve(Maze maze, MazePanel panel);

    /**  
     * Ajusta el delay “automático”. Tú ya lo tenías.  
     */
    default void setDelaySolucion(int milisegundos) { }

    /**  
     * Configura el semáforo para paso a paso manual.  
     */
    default void setPasoSemaphore(Semaphore pasoSem) { }
}
