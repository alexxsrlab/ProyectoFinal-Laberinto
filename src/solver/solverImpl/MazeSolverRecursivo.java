package solver.solverImpl;

import solver.MazeSolver;
import models.*;
import views.MazePanel;

public class MazeSolverRecursivo implements MazeSolver {
    private Maze maze;
    private MazePanel panel;

    @Override
    public boolean solve(Maze maze, MazePanel panel) {
        this.maze = maze;
        this.panel = panel;
        limpiarMaze();
        boolean res = dfs(maze.startX, maze.startY);
        panel.repaint();
        return res;
    }

    private boolean dfs(int x, int y) {
        if (x < 0 || x >= maze.rows || y < 0 || y >= maze.cols) return false;
        Cell cell = maze.grid[x][y];
        if (cell.wall || cell.visited) return false;

        cell.visited = true;
        panel.repaint();
        pausa();

        if (x == maze.endX && y == maze.endY) {
            cell.solution = true;
            return true;
        }

        if (dfs(x + 1, y) || dfs(x - 1, y) || dfs(x, y + 1) || dfs(x, y - 1)) {
            cell.solution = true;
            return true;
        }

        return false;
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
