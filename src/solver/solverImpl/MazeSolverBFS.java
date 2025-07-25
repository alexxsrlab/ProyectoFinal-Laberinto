package solver.solverImpl;

import solver.MazeSolver;
import models.*;
import views.MazePanel;

import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MazeSolverBFS implements MazeSolver {
    private Maze maze;
    private MazePanel panel;
    private Map<String, String> parentMap = new HashMap<>();

    @Override
    public boolean solve(Maze maze, MazePanel panel) {
        this.maze = maze;
        this.panel = panel;
        limpiarMaze();
        parentMap.clear();

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{maze.startX, maze.startY});
        maze.grid[maze.startX][maze.startY].visited = true;

        boolean found = false;

        // Fase 1: Búsqueda BFS normal
        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int x = pos[0];
            int y = pos[1];

            if (x == maze.endX && y == maze.endY) {
                found = true;
                break;
            }

            explorarVecino(queue, x, y, x + 1, y); // Abajo
            explorarVecino(queue, x, y, x - 1, y); // Arriba
            explorarVecino(queue, x, y, x, y + 1); // Derecha
            explorarVecino(queue, x, y, x, y - 1); // Izquierda

            panel.repaint();
            pausa();
        }

        if (found) {
            // Fase 2: Mostrar camino solución paso a paso
            mostrarCaminoAnimado();
        // } else {
        //     panel.showMessage("No se encontró solución con BFS");
         }
        return found;
    }

    private void explorarVecino(Queue<int[]> queue, int parentX, int parentY, int x, int y) {
        if (x >= 0 && x < maze.rows && y >= 0 && y < maze.cols && 
            !maze.grid[x][y].wall && !maze.grid[x][y].visited) {
            
            maze.grid[x][y].visited = true;
            parentMap.put(x + "," + y, parentX + "," + parentY);
            queue.add(new int[]{x, y});
        }
    }

    private void mostrarCaminoAnimado() {
        Stack<int[]> camino = new Stack<>();
        int x = maze.endX;
        int y = maze.endY;
        
        // Reconstruir el camino (de final a inicio)
        while (x != maze.startX || y != maze.startY) {
            camino.push(new int[]{x, y});
            String parentKey = parentMap.get(x + "," + y);
            if (parentKey == null) break;
            
            String[] parentCoords = parentKey.split(",");
            x = Integer.parseInt(parentCoords[0]);
            y = Integer.parseInt(parentCoords[1]);
        }
        camino.push(new int[]{maze.startX, maze.startY});

        // Mostrar el camino paso a paso (de inicio a final)
        while (!camino.isEmpty()) {
            int[] pos = camino.pop();
            maze.grid[pos[0]][pos[1]].solution = true;
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
            Thread.sleep(50); // Ajusta este valor para cambiar la velocidad
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}