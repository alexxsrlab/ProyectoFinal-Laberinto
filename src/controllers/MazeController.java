package controllers;

import dao.AlgorithmResultDAO;
import dao.daoImpl.AlgorithmResultDAOFile;
import models.AlgorithmResult;
import models.Maze;
import solver.MazeSolver;
import solver.solverImpl.*;
import views.MazeFrame;

import javax.swing.*;

public class MazeController {
    private MazeFrame frame;
    private Maze laberinto;
    private final AlgorithmResultDAO resultDAO = new AlgorithmResultDAOFile();

    public void inicializar() {
        int filas, columnas;
        do {
            String f = JOptionPane.showInputDialog(
                null, "Ingrese la cantidad de filas (mínimo 4):", "Filas", JOptionPane.QUESTION_MESSAGE
            );
            if (f == null) System.exit(0);
            String c = JOptionPane.showInputDialog(
                null, "Ingrese la cantidad de columnas (mínimo 4):", "Columnas", JOptionPane.QUESTION_MESSAGE
            );
            if (c == null) System.exit(0);
            try {
                filas    = Integer.parseInt(f);
                columnas = Integer.parseInt(c);
                if (filas >= 4 && columnas >= 4) break;
            } catch (NumberFormatException ignored) {}
            JOptionPane.showMessageDialog(
                null, "Valores inválidos. Deben ser enteros ≥ 4.", "Error", JOptionPane.ERROR_MESSAGE
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
                    frame, "Inicio o fin están en pared", "Error", JOptionPane.ERROR_MESSAGE
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
                        frame, "No se encontró solución", "Aviso", JOptionPane.WARNING_MESSAGE
                    )
                );
            }
        }).start();
    }

    public void resolverPorPasos(String tipoAlg) {
        new Thread(() -> {
            MazeSolver sol = crearSolucionador(tipoAlg);
            sol.setDelaySolucion(500);            // medio segundo de delay
            sol.solve(laberinto, frame.obtenerPanel());
        }).start();
    }

    private MazeSolver crearSolucionador(String tipo) {
        switch (tipo) {
            case "DFS":               return new MazeSolverDFS();
            case "BFS":               return new MazeSolverBFS();
            case "Recursive":         return new MazeSolverRecursivo();
            case "RecursiveComplete": return new MazeSolverRecursivoCompleto();
            case "RecursiveBT":       return new MazeSolverRecursivoCompletoBT();
            case "Backtracking":      return new MazeSolverBackTracking();
            default: throw new IllegalArgumentException("Algoritmo desconocido: " + tipo);
        }
    }

    public void limpiarLaberinto() {
        // 1) Quitar todas las paredes
        for (int i = 0; i < laberinto.rows; i++) {
            for (int j = 0; j < laberinto.cols; j++) {
                laberinto.grid[i][j].wall = false;
            }
        }
        
        // 2) Limpiar visitados y soluciones (mantiene filas/cols)
        laberinto.limpiarCeldas();
        
        // 3) Restablecer inicio y fin
        laberinto.setStartPosition(0, 0);
        laberinto.setEndPosition(laberinto.rows - 1, laberinto.cols - 1);
    
        // 4) Refrescar la vista
        frame.obtenerPanel().repaint();
        
        // 5) Mensaje de confirmación
        SwingUtilities.invokeLater(() ->
            JOptionPane.showMessageDialog(
                frame,
                "Laberinto reiniciado",
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
}