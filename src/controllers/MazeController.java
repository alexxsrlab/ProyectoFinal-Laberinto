package controllers;

import models.*;
import solver.*;
import solver.solverImpl.*;
import views.*;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class MazeController {
    private MazeFrame frame;
    private Maze maze;

    public void inicializar() {
        // Pedir filas y columnas al usuario
        int filas = 10;
        int columnas = 10;
        boolean valido = false;
        while (!valido) {
            String filasStr = JOptionPane.showInputDialog(null, "Ingrese la cantidad de filas (mínimo 2):", "Filas", JOptionPane.QUESTION_MESSAGE);
            if (filasStr == null) System.exit(0); // Cancelar
            String columnasStr = JOptionPane.showInputDialog(null, "Ingrese la cantidad de columnas (mínimo 2):", "Columnas", JOptionPane.QUESTION_MESSAGE);
            if (columnasStr == null) System.exit(0); // Cancelar
            try {
                filas = Integer.parseInt(filasStr);
                columnas = Integer.parseInt(columnasStr);
                if (filas >= 2 && columnas >= 2) {
                    valido = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe ingresar valores mayores o iguales a 2.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese solo números enteros.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        this.maze = new Maze(filas, columnas);
        this.frame = new MazeFrame(this);
        frame.setVisible(true);
    }

    public Maze obtenerLaberinto() {
        return maze;
    }

    // Ejemplo con recursivo con backtracking usando hilo y mensaje
    public void resolverRecursivoBT() {
        new Thread(() -> {
            MazeSolver solver = new MazeSolverRecursivoCompletoBT();
            boolean resultado = solver.solve(maze, frame.obtenerPanel());
            if (!resultado) {
                // Para mostrar diálogo en el hilo de la GUI
                SwingUtilities.invokeLater(() -> 
                    javax.swing.JOptionPane.showMessageDialog(frame,
                        "No se pudo encontrar una solución",
                        "Aviso",
                        javax.swing.JOptionPane.WARNING_MESSAGE)
                );
            }
        }).start();
    }

    // Similar para DFS
    public void resolverConDFS() {
        new Thread(() -> {
            MazeSolver solver = new MazeSolverDFS();
            boolean resultado = solver.solve(maze, frame.obtenerPanel());
            if (!resultado) {
                SwingUtilities.invokeLater(() -> 
                    javax.swing.JOptionPane.showMessageDialog(frame,
                        "No se pudo encontrar una solución",
                        "Aviso",
                        javax.swing.JOptionPane.WARNING_MESSAGE)
                );
            }
        }).start();
    }

    // Similar para BFS
    public void resolverConBFS() {
        new Thread(() -> {
            MazeSolver solver = new MazeSolverBFS();
            boolean resultado = solver.solve(maze, frame.obtenerPanel());
            if (!resultado) {
                SwingUtilities.invokeLater(() -> 
                    javax.swing.JOptionPane.showMessageDialog(frame,
                        "No se pudo encontrar una solución",
                        "Aviso",
                        javax.swing.JOptionPane.WARNING_MESSAGE)
                );
            }
        }).start();
    }

    // Similar para recursivo simple
    public void resolverRecursivo() {
        new Thread(() -> {
            MazeSolver solver = new MazeSolverRecursivo();
            boolean resultado = solver.solve(maze, frame.obtenerPanel());
            if (!resultado) {
                SwingUtilities.invokeLater(() -> 
                    javax.swing.JOptionPane.showMessageDialog(frame,
                        "No se pudo encontrar una solución",
                        "Aviso",
                        javax.swing.JOptionPane.WARNING_MESSAGE)
                );
            }
        }).start();
    }

    // Similar para recursivo completo
    public void resolverRecursivoCompleto() {
        new Thread(() -> {
            MazeSolver solver = new MazeSolverRecursivoCompleto();
            boolean resultado = solver.solve(maze, frame.obtenerPanel());
            if (!resultado) {
                SwingUtilities.invokeLater(() -> 
                    javax.swing.JOptionPane.showMessageDialog(frame,
                        "No se pudo encontrar una solución",
                        "Aviso",
                        javax.swing.JOptionPane.WARNING_MESSAGE)
                );
            }
        }).start();
    }

    public void limpiarLaberinto() {
    // Limpiar el laberinto manteniendo dimensiones y paredes
    maze.limpiarCeldas();
    
    // Restablecer posición inicial y final a valores por defecto
    maze.setStartPosition(0, 0);
    maze.setEndPosition(maze.rows - 1, maze.cols - 1);
    
    // Actualizar la vista
    frame.obtenerPanel().repaint();
    
    // Mostrar mensaje de confirmación
    SwingUtilities.invokeLater(() -> 
        JOptionPane.showMessageDialog(frame,
            "Laberinto reiniciado",
            "Información",
            JOptionPane.INFORMATION_MESSAGE)
    );
}

    public void resolverLaberinto(String algorithmType) {
        if (maze.grid[maze.startX][maze.startY].wall || 
        maze.grid[maze.endX][maze.endY].wall) {
        SwingUtilities.invokeLater(() -> 
            JOptionPane.showMessageDialog(frame,
                "El inicio o el final están en una pared",
                "Error",
                JOptionPane.ERROR_MESSAGE));
        return;
    }
    new Thread(() -> {
        MazeSolver solver;
        
        switch(algorithmType) {
            case "DFS":
                solver = new MazeSolverDFS();
                break;
            case "BFS":
                solver = new MazeSolverBFS();
                break;
            case "Recursive":
                solver = new MazeSolverRecursivo();
                break;
            case "RecursiveComplete":
                solver = new MazeSolverRecursivoCompleto();
                break;
            case "RecursiveBT":
                solver = new MazeSolverRecursivoCompletoBT();
                break;
            default:
                SwingUtilities.invokeLater(() -> 
                    JOptionPane.showMessageDialog(frame,
                        "Algoritmo no reconocido",
                        "Error",
                        JOptionPane.ERROR_MESSAGE));
                return;
        }
        
        boolean resultado = solver.solve(maze, frame.obtenerPanel());
        if (!resultado) {
            SwingUtilities.invokeLater(() -> 
                JOptionPane.showMessageDialog(frame,
                    "No se pudo encontrar una solución",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE));
        }
    }).start();
}

public void crearNuevoLaberinto(int filas, int columnas) {
    this.maze = new Maze(filas, columnas);
    this.frame = new views.MazeFrame(this);
    frame.setVisible(true);
}
}
