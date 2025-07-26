package models;

public class Maze {
    public Cell[][] grid;
    public int rows, cols;
    public int startX = -1, startY = -1, endX = -1, endY = -1; // Sin inicio ni fin

    public Maze(int r, int c) {
        this.rows = r;
        this.cols = c;
        this.grid = new Cell[r][c];
        generarVacio();
    }

    // Todas las celdas transitables, sin paredes
    private void generarVacio() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell cell = new Cell();
                cell.wall = false;
                grid[i][j] = cell;
            }
        }
    }

    public void limpiarCeldas() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j].visited = false;
                grid[i][j].solution = false;
                // Mantenemos el estado de las paredes (wall)
            }
        }
    }
    
    public void setStartPosition(int x, int y) {
        if (x >= 0 && x < rows && y >= 0 && y < cols && !grid[x][y].wall) {
            startX = x;
            startY = y;
        }
    }
    
    public void setEndPosition(int x, int y) {
        if (x >= 0 && x < rows && y >= 0 && y < cols && !grid[x][y].wall) {
            endX = x;
            endY = y;
        }
    }
}
