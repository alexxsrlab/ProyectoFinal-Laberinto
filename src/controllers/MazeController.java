package controllers;

import models.*;
import solver.*;
import solver.solverImpl.*;
import views.*;

import javax.swing.SwingUtilities;

public class MazeController {
    private MazeFrame frame;
    private Maze maze;

    public void inicializar() {
        this.maze = new Maze(10, 10);
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
}
