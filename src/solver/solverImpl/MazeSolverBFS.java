package solver.solverImpl;

import models.Cell;
import models.Maze;
import solver.MazeSolver;
import views.MazePanel;

import java.util.*;
import java.util.concurrent.Semaphore;

public class MazeSolverBFS implements MazeSolver {
    // ——— paso a paso
    private Semaphore pasoSem = null;
    @Override
    public void setPasoSemaphore(Semaphore pasoSem) {
        this.pasoSem = pasoSem;
    }
    private void esperarPaso() {
        if (pasoSem != null) {
            try { pasoSem.acquire(); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }
    // ——————————————————————

    private Maze maze;
    private MazePanel panel;
    private int solutionDelay = 50;
    private final Map<String, String> parentMap = new HashMap<>();

    @Override
    public boolean solve(Maze maze, MazePanel panel) {
        this.maze = maze;
        this.panel = panel;
        limpiarMaze();
        parentMap.clear();

        // primer paso manual
        esperarPaso();

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{maze.startX, maze.startY});
        maze.grid[maze.startX][maze.startY].visited = true;

        boolean found = false;
        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int x = pos[0], y = pos[1];

            if (x == maze.endX && y == maze.endY) {
                found = true; break;
            }

            for (int[] d : new int[][]{{1,0},{-1,0},{0,1},{0,-1}}) {
                int nx = x + d[0], ny = y + d[1];
                if (nx>=0 && nx<maze.rows && ny>=0 && ny<maze.cols
                 && !maze.grid[nx][ny].wall
                 && !maze.grid[nx][ny].visited) {
                    maze.grid[nx][ny].visited = true;
                    parentMap.put(nx + "," + ny, x + "," + y);
                    queue.add(new int[]{nx, ny});
                }
            }

            esperarPaso();
            panel.repaint();
            pause(solutionDelay);
        }

        if (found) animarSolucion();
        return found;
    }

    private void animarSolucion() {
        Deque<int[]> stack = new ArrayDeque<>();
        int x = maze.endX, y = maze.endY;
        while (x!=maze.startX || y!=maze.endY) {
            stack.push(new int[]{x,y});
            String key = parentMap.get(x+","+y);
            if (key==null) break;
            String[] p = key.split(",");
            x = Integer.parseInt(p[0]); y = Integer.parseInt(p[1]);
        }
        stack.push(new int[]{maze.startX, maze.startY});

        while (!stack.isEmpty()) {
            int[] pos = stack.pop();
            maze.grid[pos[0]][pos[1]].solution = true;
            esperarPaso();
            panel.repaint();
            pause(solutionDelay);
        }
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

    private void pause(int ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
