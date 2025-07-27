package controllers;

import dao.AlgorithmResultDAO;
import dao.daoImpl.AlgorithmResultDAOFile;
import models.AlgorithmResult;
import models.Maze;
import solver.MazeSolver;
import solver.solverImpl.*;
import views.MazeFrame;

import javax.swing.*;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class MazeController {
    private MazeFrame frame;
    private Maze laberinto;
    private final AlgorithmResultDAO resultDAO = new AlgorithmResultDAOFile();

    private Semaphore pasoSem;
    private boolean modoManualIniciado;
    private boolean forwardDone;
    private boolean modoRegreso;

    private List<Point> camino;
    private int backwardIndex;

    public void inicializar() {
        int filas, columnas;
        do {
            String f = JOptionPane.showInputDialog(
                null, "Ingrese la cantidad de filas (mínimo 4):",
                "Filas", JOptionPane.QUESTION_MESSAGE
            );
            if (f == null) System.exit(0);
            String c = JOptionPane.showInputDialog(
                null, "Ingrese la cantidad de columnas (mínimo 4):",
                "Columnas", JOptionPane.QUESTION_MESSAGE
            );
            if (c == null) System.exit(0);
            try {
                filas    = Integer.parseInt(f);
                columnas = Integer.parseInt(c);
                if (filas >= 4 && columnas >= 4) break;
            } catch (NumberFormatException ignored) {}
            JOptionPane.showMessageDialog(
                null, "Valores inválidos. Deben ser enteros ≥ 4.",
                "Error", JOptionPane.ERROR_MESSAGE
            );
        } while (true);

        laberinto = new Maze(filas, columnas);
        frame = new MazeFrame(this);
        frame.setVisible(true);
    }

    public Maze obtenerLaberinto() {
        return laberinto;
    }

    public void resolverLaberinto(String tipoAlg) {
        if (laberinto.grid[laberinto.startX][laberinto.startY].wall ||
            laberinto.grid[laberinto.endX][laberinto.endY].wall) {
            SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(
                    frame, "Inicio o fin están en pared",
                    "Error", JOptionPane.ERROR_MESSAGE
                )
            );
            return;
        }
        new Thread(() -> {
            MazeSolver sol = crearSolucionador(tipoAlg);
            long t0 = System.nanoTime();
            boolean ok = sol.solve(laberinto, frame.obtenerPanel());
            long t1 = System.nanoTime();
            if (ok) {
                int len = contarCamino();
                resultDAO.guardar(new AlgorithmResult(tipoAlg, len, t1 - t0));
            } else {
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(
                        frame, "No se encontró solución",
                        "Aviso", JOptionPane.WARNING_MESSAGE
                    )
                );
            }
        }).start();
    }

    public void pasoAPaso(String tipoAlg) {
        if (!modoManualIniciado) {
            modoManualIniciado = true;
            forwardDone       = false;
            modoRegreso       = false;
            camino            = null;

            pasoSem = new Semaphore(0);
            new Thread(() -> {
                MazeSolver sol = crearSolucionador(tipoAlg);
                sol.setPasoSemaphore(pasoSem);
                sol.setDelaySolucion(0);
                sol.solve(laberinto, frame.obtenerPanel());
                forwardDone = true;
                camino = capturarCamino();
                backwardIndex = camino.size() - 1;
            }).start();

        } else if (!forwardDone) {
            pasoSem.release();

        } else if (forwardDone && !modoRegreso) {
            modoRegreso = true;

        } else {
            if (backwardIndex >= 0) {
                Point p = camino.get(backwardIndex--);
                laberinto.grid[p.x][p.y].backtrack = true;
                SwingUtilities.invokeLater(() ->
                    frame.obtenerPanel().repaint()
                );
            }
            if (backwardIndex < 0) {
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(
                        frame,
                        "El recorrido ha finalizado",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE
                    )
                );
                modoManualIniciado = false;
            }
        }
    }

    private List<Point> capturarCamino() {
        List<Point> path = new ArrayList<>();
        int sx = laberinto.startX, sy = laberinto.startY;
        int ex = laberinto.endX,   ey = laberinto.endY;
        boolean[][] vis = new boolean[laberinto.rows][laberinto.cols];
        dfsPath(sx, sy, ex, ey, vis, path);
        return path;
    }

    private boolean dfsPath(int x, int y, int ex, int ey,
                            boolean[][] vis, List<Point> path) {
        if (x<0||x>=laberinto.rows||y<0||y>=laberinto.cols) return false;
        if (vis[x][y] || !laberinto.grid[x][y].solution) return false;
        vis[x][y] = true;
        if (x == ex && y == ey) {
            path.add(new Point(x,y));
            return true;
        }
        for (int[] d : new int[][]{{1,0},{-1,0},{0,1},{0,-1}}) {
            if (dfsPath(x+d[0], y+d[1], ex, ey, vis, path)) {
                path.add(0, new Point(x,y));
                return true;
            }
        }
        return false;
    }

    public void limpiarLaberinto() {
        for (int i = 0; i < laberinto.rows; i++)
            for (int j = 0; j < laberinto.cols; j++) {
                laberinto.grid[i][j].wall = false;
                laberinto.grid[i][j].visited = false;
                laberinto.grid[i][j].solution = false;
                laberinto.grid[i][j].backtrack = false;
            }

        laberinto.setStartPosition(-1, -1);
        laberinto.setEndPosition(-1, -1);
        frame.obtenerPanel().repaint();
        SwingUtilities.invokeLater(() ->
            JOptionPane.showMessageDialog(
                frame,
                "Laberinto reiniciado (todo fue borrado)",
                "Información",
                JOptionPane.INFORMATION_MESSAGE
            )
        );
    }


    private int contarCamino() {
        int cnt = 0;
        for (int i = 0; i < laberinto.rows; i++)
            for (int j = 0; j < laberinto.cols; j++)
                if (laberinto.grid[i][j].solution) cnt++;
        return cnt;
    }

    public void crearNuevoLaberinto(int f, int c) {
        frame.dispose();
        laberinto = new Maze(f, c);
        frame = new MazeFrame(this);
        frame.setVisible(true);
    }

    private MazeSolver crearSolucionador(String tipo) {
        switch (tipo) {
            case "DFS":               return new MazeSolverDFS();
            case "BFS":               return new MazeSolverBFS();
            case "Recursive":         return new MazeSolverRecursivo();
            case "RecursiveComplete": return new MazeSolverRecursivoCompleto();
            case "RecursiveBT":       return new MazeSolverRecursivoCompletoBT();
            case "Backtracking":      return new MazeSolverBackTracking();
            default:
                throw new IllegalArgumentException(
                    "Algoritmo desconocido: " + tipo
                );
        }
    }
}
