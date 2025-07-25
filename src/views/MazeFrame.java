package views;

import controllers.MazeController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MazeFrame extends JFrame {
    private MazePanel panel;
    private MazeController controller;

    public MazeFrame(MazeController controller) {
        this.controller = controller;
        setTitle("Maze Solver");
        setSize(600, 700); // Aumentamos un poco el tamaño
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new MazePanel(controller);
        add(panel, BorderLayout.CENTER);

        // Panel superior para controles de edición
        JPanel editPanel = new JPanel(new GridLayout(1, 4));
        
        JButton setStartBtn = new JButton("Añadir Inicio");
        setStartBtn.addActionListener(e -> panel.setStartPositionMode());
        
        JButton setEndBtn = new JButton("Añadir Fin");
        setEndBtn.addActionListener(e -> panel.setEndPositionMode());
        
        JButton addWallBtn = new JButton("Agregar Paredes");
        addWallBtn.addActionListener(e -> panel.setWallMode());
        
        JButton clearWallBtn = new JButton("Quitar Paredes");
        clearWallBtn.addActionListener(e -> panel.setRemoveWallMode());
        
        editPanel.add(setStartBtn);
        editPanel.add(setEndBtn);
        editPanel.add(addWallBtn);
        editPanel.add(clearWallBtn);

        // Panel inferior con botones de resolución
        JPanel solvePanel = new JPanel();
        
        JButton dfsBtn = createAlgorithmButton("Solve DFS", "DFS");
        JButton bfsBtn = createAlgorithmButton("Solve BFS", "BFS");
        JButton recBtn = createAlgorithmButton("Recursive", "Recursive");
        JButton recCompBtn = createAlgorithmButton("Recursive Complete", "RecursiveComplete");
        JButton recBTBtn = createAlgorithmButton("Recursive BT", "RecursiveBT");
        JButton clearBtn = new JButton("Clear Maze");
        clearBtn.addActionListener(e -> controller.limpiarLaberinto());

        solvePanel.add(dfsBtn);
        solvePanel.add(bfsBtn);
        solvePanel.add(recBtn);
        solvePanel.add(recCompBtn);
        solvePanel.add(recBTBtn);
        solvePanel.add(clearBtn);

        // Panel principal que contiene ambos paneles
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(editPanel, BorderLayout.NORTH);
        controlPanel.add(solvePanel, BorderLayout.SOUTH);

        add(controlPanel, BorderLayout.SOUTH);
    }

    private JButton createAlgorithmButton(String text, String algorithmType) {
        JButton button = new JButton(text);
        button.addActionListener((ActionEvent e) -> {
            controller.resolverLaberinto(algorithmType);
        });
        return button;
    }

    public MazePanel obtenerPanel() {
        return panel;
    }
}