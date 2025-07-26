package solver.solverImpl;

import models.Cell;
import models.Maze;
import solver.MazeSolver;
import views.MazePanel;

import java.util.*;

public class MazeSolverDFS implements MazeSolver {
    private Maze maze;
    private MazePanel panel;
    private int solutionDelay = 150;             // valor por defecto
    private final Map<String,int[]> parentMap = new HashMap<>();

    @Override
    public void setDelaySolucion(int milisegundos) {
        this.solutionDelay = milisegundos;
    }

    @Override
    public boolean solve(Maze maze, MazePanel panel) {
        this.maze = maze;
        this.panel = panel;
        limpiarMaze();

        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{maze.startX, maze.startY});

        while (!stack.isEmpty()) {
            int[] pos = stack.pop();
            int x = pos[0], y = pos[1];

            if (x < 0 || x >= maze.rows || y < 0 || y >= maze.cols) continue;
            Cell c = maze.grid[x][y];
            if (c.wall || c.visited) continue;

            c.visited = true;
            panel.repaint();
            pausa();

            if (x == maze.endX && y == maze.endY) {
                reconstruirSolucion(x, y);
                return true;
            }

            for (int[] d : new int[][]{{-1,0},{0,1},{1,0},{0,-1}}) {
                int nx = x + d[0], ny = y + d[1];
                if (nx>=0 && nx<maze.rows &&
                    ny>=0 && ny<maze.cols &&
                    !maze.grid[nx][ny].visited &&
                    !maze.grid[nx][ny].wall) {

                    parentMap.put(nx + "," + ny, new int[]{x, y});
                    stack.push(new int[]{nx, ny});
                }
            }
        }
        return false;
    }

    private void reconstruirSolucion(int endX, int endY) {
        Deque<int[]> camino = new ArrayDeque<>();
        String key = endX + "," + endY;
        while (key != null) {
            String[] p = key.split(",");
            int x = Integer.parseInt(p[0]), y = Integer.parseInt(p[1]);
            camino.push(new int[]{x, y});
            int[] padre = parentMap.get(key);
            key = padre == null ? null : padre[0] + "," + padre[1];
        }
        while (!camino.isEmpty()) {
            int[] pos = camino.pop();
            maze.grid[pos[0]][pos[1]].solution = true;
            panel.repaint();
            pausa();
        }
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
