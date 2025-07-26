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
    private int solutionDelay = 50;              // valor por defecto
    private final List<Cell> finalPath = new ArrayList<>();

    @Override
    public void setDelaySolucion(int milisegundos) {
        this.solutionDelay = milisegundos;
    }

    @Override
    public boolean solve(Maze maze, MazePanel panel) {
        this.maze = maze;
        this.panel = panel;
        initialize();

        boolean result = backtrack(maze.startX, maze.startY);
        if (result) highlightFinalPath();

        panel.repaint();
        return result;
    }

    private void initialize() {
        visited = new boolean[maze.rows][maze.cols];
        finalPath.clear();
        for (int i = 0; i < maze.rows; i++) {
            for (int j = 0; j < maze.cols; j++) {
                Cell c = maze.grid[i][j];
                c.visited   = false;
                c.solution  = false;
                c.backtrack = false;
                visited[i][j] = false;
            }
        }
    }

    private boolean backtrack(int x, int y) {
        if (x < 0 || x >= maze.rows || y < 0 || y >= maze.cols) return false;
        if (maze.grid[x][y].wall || visited[x][y]) return false;

        visited[x][y] = true;
        maze.grid[x][y].visited = true;
        panel.repaint();
        pause(solutionDelay);

        if (x == maze.endX && y == maze.endY) {
            finalPath.add(maze.grid[x][y]);
            return true;
        }

        for (int[] d : new int[][]{{1,0},{0,1},{-1,0},{0,-1}}) {
            if (backtrack(x + d[0], y + d[1])) {
                finalPath.add(maze.grid[x][y]);
                return true;
            }
        }

        maze.grid[x][y].backtrack = true;
        panel.repaint();
        pause(solutionDelay/2);
        maze.grid[x][y].backtrack = false;
        return false;
    }

    private void highlightFinalPath() {
        for (int i = finalPath.size() - 1; i >= 0; i--) {
            finalPath.get(i).solution = true;
            panel.repaint();
            pause(solutionDelay);
        }
    }

    private void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}