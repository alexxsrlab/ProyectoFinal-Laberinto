package models;

public class Maze {
    public Cell[][] grid;
    public int rows, cols;
    public int startX = 0, startY = 0, endX = 9, endY = 9;

    public Maze(int r, int c) {
        this.rows = r;
        this.cols = c;
        this.grid = new Cell[r][c];
        generarFijo();
    }

    private void generarFijo() {
        int[][] mapa = new int[][] {
            {0,1,0,0,0,1,0,0,0,0},
            {0,1,0,1,0,1,0,1,1,0},
            {0,0,0,1,0,0,0,0,1,0},
            {0,1,1,1,1,1,1,0,1,0},
            {0,0,0,0,0,0,1,0,1,0},
            {0,1,1,1,1,0,1,0,1,0},
            {0,0,0,0,1,0,1,0,0,0},
            {1,1,1,0,1,0,1,1,1,0},
            {0,0,0,0,0,0,0,0,1,0},
            {0,1,1,1,1,1,1,0,1,0}
        };
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell c = new Cell();
                c.wall = (mapa[i][j] == 1);
                grid[i][j] = c;
            }
        }
    }
}
