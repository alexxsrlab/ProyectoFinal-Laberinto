package solver.solverImpl;

import solver.MazeSolver;
import models.*;
import views.MazePanel;

import java.util.Stack;

public class MazeSolverDFS implements MazeSolver {
    private Maze maze;
    private MazePanel panel;

    @Override
    public boolean solve(Maze maze, MazePanel panel) {
        this.maze = maze;
        this.panel = panel;
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
                cell.solution = true;
                panel.repaint();
                return true;
            }

            stack.push(new int[]{x + 1, y});
            stack.push(new int[]{x - 1, y});
            stack.push(new int[]{x, y + 1});
            stack.push(new int[]{x, y - 1});
        }

        return false;
    }

    private void limpiarMaze(Maze maze) {
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
