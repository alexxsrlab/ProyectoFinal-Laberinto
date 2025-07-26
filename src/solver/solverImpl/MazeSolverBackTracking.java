package solver.solverImpl;

import models.Cell;
import models.Maze;
import solver.MazeSolver;
import views.MazePanel;

import java.util.ArrayList;
import java.util.List;

public class MazeSolverBackTracking implements MazeSolver {
    private Maze maze;
    private MazePanel panel;
    private boolean[][] visited;
    private int solutionDelay = 50;
    private List<Cell> finalPath = new ArrayList<>();

    @Override
    public boolean solve(Maze maze, MazePanel panel) {
        this.maze = maze;
        this.panel = panel;
        initialize();

        boolean result = backtrack(maze.startX, maze.startY);

        if (result) {
            highlightFinalPath();
        }

        panel.repaint();
        return result;
    }

    private void initialize() {
        visited = new boolean[maze.rows][maze.cols];
        finalPath.clear();

        for (int i = 0; i < maze.rows; i++) {
            for (int j = 0; j < maze.cols; j++) {
                maze.grid[i][j].visited = false;
                maze.grid[i][j].solution = false;
                maze.grid[i][j].backtrack = false;
                visited[i][j] = false;
            }
        }
    }

    private boolean backtrack(int x, int y) {
        if (x < 0 || x >= maze.rows || y < 0 || y >= maze.cols)
            return false;

        if (maze.grid[x][y].wall || visited[x][y])
            return false;

        visited[x][y] = true;
        maze.grid[x][y].visited = true;
        panel.repaint();
        pause(solutionDelay);

        if (x == maze.endX && y == maze.endY) {
            finalPath.add(maze.grid[x][y]);
            return true;
        }

        int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            if (backtrack(newX, newY)) {
                finalPath.add(maze.grid[x][y]); // agregar parte del camino
                return true;
            }
        }

        maze.grid[x][y].backtrack = true;
        panel.repaint();
        pause(solutionDelay / 2);
        maze.grid[x][y].backtrack = false;

        return false;
    }

    private void highlightFinalPath() {
        for (int i = finalPath.size() - 1; i >= 0; i--) {
            Cell cell = finalPath.get(i);
            cell.solution = true;
            panel.repaint();
            pause(solutionDelay);
        }
    }

    private void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
