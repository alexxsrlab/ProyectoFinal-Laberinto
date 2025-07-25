package solver.solverImpl;

import solver.MazeSolver;
import models.*;
import views.MazePanel;

import java.util.Stack;
import java.util.HashMap;
import java.util.Map;

public class MazeSolverDFS implements MazeSolver {
    private Maze maze;
    private MazePanel panel;
    private Map<String, int[]> parentMap;
    private int solutionDelay = 150; // Tiempo entre pasos de solución (ms)

    @Override
    public boolean solve(Maze maze, MazePanel panel) {
        this.maze = maze;
        this.panel = panel;
        this.parentMap = new HashMap<>();
        limpiarMaze(maze);

        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{maze.startX, maze.startY});

        while (!stack.isEmpty()) {
            int[] pos = stack.pop();
            int x = pos[0];
            int y = pos[1];

            if (x < 0 || x >= maze.rows || y < 0 || y >= maze.cols) continue;
            Cell cell = maze.grid[x][y];
            if (cell.wall || cell.visited) continue;

            cell.visited = true;
            panel.repaint();
            pausa();

            if (x == maze.endX && y == maze.endY) {
                mostrarCaminoSolucion(x, y);
                return true;
            }

            int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                
                if (newX >= 0 && newX < maze.rows && newY >= 0 && newY < maze.cols) {
                    if (!maze.grid[newX][newY].visited && !maze.grid[newX][newY].wall) {
                        parentMap.put(newX + "," + newY, new int[]{x, y});
                        stack.push(new int[]{newX, newY});
                    }
                }
            }
        }
        return false;
    }

    private void mostrarCaminoSolucion(int endX, int endY) {
        Stack<int[]> camino = new Stack<>();
        int[] current = new int[]{endX, endY};
        
        // Reconstruir camino (del final al inicio)
        while (current != null) {
            camino.push(current);
            current = parentMap.get(current[0] + "," + current[1]);
        }
        
        // Mostrar camino (del inicio al final)
        while (!camino.isEmpty()) {
            current = camino.pop();
            int x = current[0];
            int y = current[1];
            
            if ((x != maze.startX || y != maze.startY) && 
                (x != maze.endX || y != maze.endY)) {
                maze.grid[x][y].solution = true;
                panel.repaint();
                try {
                    Thread.sleep(solutionDelay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void limpiarMaze(Maze maze) {
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
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}