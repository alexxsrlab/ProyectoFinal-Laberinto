package solver;

import models.Maze;
import views.MazePanel;

public interface MazeSolver {
    boolean solve(Maze maze, MazePanel panel);
}
