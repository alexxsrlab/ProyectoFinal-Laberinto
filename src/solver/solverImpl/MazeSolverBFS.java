package solver.solverImpl;

import models.Cell;
import models.Maze;
import solver.MazeSolver;
import views.MazePanel;

import java.util.*;

public class MazeSolverBFS implements MazeSolver {
    private Maze maze;
    private MazePanel panel;
    private int solutionDelay = 50;               // valor por defecto
    private final Map<String, String> parentMap = new HashMap<>();

    @Override
    public void setDelaySolucion(int milisegundos) {
        this.solutionDelay = milisegundos;
    }

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
        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int x = pos[0], y = pos[1];

            if (x == maze.endX && y == maze.endY) {
                found = true;
                break;
            }

            for (int[] d : new int[][]{{1,0},{-1,0},{0,1},{0,-1}}) {
                int nx = x + d[0], ny = y + d[1];
                if (nx >= 0 && nx < maze.rows &&
                    ny >= 0 && ny < maze.cols &&
                    !maze.grid[nx][ny].wall &&
                    !maze.grid[nx][ny].visited) {

                    maze.grid[nx][ny].visited = true;
                    parentMap.put(nx + "," + ny, x + "," + y);
                    queue.add(new int[]{nx, ny});
                }
            }

            panel.repaint();
            pause();
        }

        if (found) {
            animarSolucion();
        }
        return found;
    }

    private void animarSolucion() {
        Deque<int[]> stack = new ArrayDeque<>();
        int x = maze.endX, y = maze.endY;
        while (x != maze.startX || y != maze.startY) {
            stack.push(new int[]{x, y});
            String key = parentMap.get(x + "," + y);
            if (key == null) break;
            String[] p = key.split(",");
            x = Integer.parseInt(p[0]);
            y = Integer.parseInt(p[1]);
        }
        stack.push(new int[]{maze.startX, maze.startY});

        while (!stack.isEmpty()) {
            int[] pos = stack.pop();
            maze.grid[pos[0]][pos[1]].solution = true;
            panel.repaint();
            pause();
        }
    }

    private void limpiarMaze() {
        for (int i = 0; i < maze.rows; i++) {
            for (int j = 0; j < maze.cols; j++) {
                Cell c = maze.grid[i][j];
                c.visited  = false;
                c.solution = false;
            }
        }
        panel.repaint();
    }

    private void pause() {
        try {
            Thread.sleep(solutionDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
