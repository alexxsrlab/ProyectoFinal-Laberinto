package solver.solverImpl;

import solver.MazeSolver;
import models.*;
import views.MazePanel;

import java.util.LinkedList;
import java.util.Queue;

public class MazeSolverBFS implements MazeSolver {
    private Maze maze;
    private MazePanel panel;

    @Override
    public boolean solve(Maze maze, MazePanel panel) {
        this.maze = maze;
        this.panel = panel;
        limpiarMaze();

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{maze.startX, maze.startY});
        boolean[][] parent = new boolean[maze.rows * maze.cols][];
        boolean found = false;

        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int x = pos[0];
            int y = pos[1];

            if (x < 0 || x >= maze.rows || y < 0 || y >= maze.cols) continue;
            Cell cell = maze.grid[x][y];
            if (cell.wall || cell.visited) continue;

            cell.visited = true;
            panel.repaint();
            pausa();

            if (x == maze.endX && y == maze.endY) {
                found = true;
                break;
            }

            queue.add(new int[]{x + 1, y});
            queue.add(new int[]{x - 1, y});
            queue.add(new int[]{x, y + 1});
            queue.add(new int[]{x, y - 1});
        }

        if (found) {
            // Marcar camino solución (si quieres implementar reconstrucción aquí)
            maze.grid[maze.endX][maze.endY].solution = true; // mínimo marcar el final
            panel.repaint();
        }
        return found;
    }

    private void limpiarMaze() {
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
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
