package solver.solverImpl;

import models.Cell;
import models.Maze;
import solver.MazeSolver;
import views.MazePanel;

public class MazeSolverRecursivo implements MazeSolver {
    private Maze maze;
    private MazePanel panel;
    private int solutionDelay = 100;             // valor por defecto

    @Override
    public void setDelaySolucion(int milisegundos) {
        this.solutionDelay = milisegundos;
    }

    @Override
    public boolean solve(Maze maze, MazePanel panel) {
        this.maze = maze;
        this.panel = panel;
        limpiarMaze();

        boolean result = dfs(maze.startX, maze.startY);
        panel.repaint();
        return result;
    }

    private boolean dfs(int x, int y) {
        if (x<0||x>=maze.rows||y<0||y>=maze.cols) return false;
        Cell c = maze.grid[x][y];
        if (c.wall || c.visited) return false;

        c.visited = true;
        panel.repaint();
        pausa();

        if (x==maze.endX && y==maze.endY) {
            c.solution = true;
            return true;
        }

        for (int[] d : new int[][]{{1,0},{0,1},{-1,0},{0,-1}}) {
            if (dfs(x+d[0], y+d[1])) {
                maze.grid[x][y].solution = true;
                return true;
            }
        }
        return false;
    }

    private void limpiarMaze() {
        for (int i = 0; i < maze.rows; i++)
            for (int j = 0; j < maze.cols; j++) {
                Cell c = maze.grid[i][j];
                c.visited  = false;
                c.solution = false;
            }
        panel.repaint();
    }

    private void pausa() {
        try {
            Thread.sleep(solutionDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
